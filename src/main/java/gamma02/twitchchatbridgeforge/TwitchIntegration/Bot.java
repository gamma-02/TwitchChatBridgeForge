package gamma02.twitchchatbridgeforge.TwitchIntegration;

import com.google.common.collect.ImmutableMap;
import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import net.minecraft.util.text.TextFormatting;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PingEvent;

import javax.net.ssl.SSLSocketFactory;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends ListenerAdapter {
    private final PircBotX ircBot;
    private final String username;
    private String channel;
    private ExecutorService myExecutor;
    private HashMap<String, TextFormatting> formattingColorCache; // Map of usernames to colors to keep consistency with usernames and colors

    public Bot(String username, String oauthKey, String channel) {
        this.channel = channel.toLowerCase();
        this.username = username.toLowerCase();
        formattingColorCache = new HashMap<String, TextFormatting>();

        Configuration.Builder builder = new Configuration.Builder()
                .setAutoNickChange(false) //Twitch doesn't support multiple users
                .setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
                .setEncoding(StandardCharsets.UTF_8) // Use UTF-8 on Windows.
                .setCapEnabled(true)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership")) //Twitch by default doesn't send JOIN, PART, and NAMES unless you request it, see https://dev.twitch.tv/docs/irc/guide/#twitch-irc-capabilities
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))

                .addServer("irc.chat.twitch.tv", 6697)
                .setSocketFactory(SSLSocketFactory.getDefault())
                .setName(this.username)
                .setServerPassword(oauthKey);

        if (!channel.equals("")) {
            builder.addAutoJoinChannel("#" + this.channel);
        }

        Configuration config = builder.addListener(this)
                .setAutoSplitMessage(false)
                .buildConfiguration();

        this.ircBot = new PircBotX(config);
        this.myExecutor = Executors.newCachedThreadPool();
    }
    public void start() {
        System.out.println("TWITCH BOT STARTED");
        myExecutor.execute(() -> {
            try {
                ircBot.startBot();
            } catch (IOException | IrcException e) {
                e.printStackTrace();
            }
        });
    }
    public void stop() {
        ircBot.stopBotReconnect();
        ircBot.close();
    }

    public boolean isConnected() {
        return ircBot.isConnected();
    }

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = event.getMessage();
        System.out.println("TWITCH MESSAGE: " + message);
        User user = event.getUser();
        if (user != null) {
            ImmutableMap<String, String> v3Tags = event.getV3Tags();
            if (v3Tags != null) {
                String nick = user.getNick();
                if (!ModConfig.CLIENT.getIgnoreList().contains(nick)) {
                    String colorTag = v3Tags.get("color");
                    TextFormatting formattingColor;

                    if (isFormattingColorCached(nick)) {
                        formattingColor = getFormattingColor(nick);
                    } else {
                        if (colorTag.equals("")) {
                            formattingColor = CalculateMinecraftColor.getDefaultUserColor(nick);
                        } else {
                            Color userColor = Color.decode(colorTag);
                            formattingColor = CalculateMinecraftColor.findNearestMinecraftColor(userColor);
                        }
                        putFormattingColor(nick, formattingColor);
                    }

                    String formattedTime = TwitchChatBridgeForge.formatTMISentTimestamp(v3Tags.get("tmi-sent-ts"));
                    TwitchChatBridgeForge.addTwitchMessage(formattedTime, nick, message, formattingColor, false);
                }
            } else {
                System.out.println("Message with no v3tags: " + event.getMessage());
            }
        } else {
            System.out.println("NON-USER MESSAGE" + event.getMessage());
        }
    }
    public void putFormattingColor(String nick, TextFormatting color) {
        formattingColorCache.put(nick.toLowerCase(), color);
    }
    public TextFormatting getFormattingColor(String nick) {
        return formattingColorCache.get(nick.toLowerCase());
    }
    public boolean isFormattingColorCached(String nick) {
        return formattingColorCache.containsKey(nick.toLowerCase());
    }
    public String getUsername() {
        return username;
    }
    @Override
    public void onPing(PingEvent event) {
        ircBot.sendRaw().rawLineNow(String.format("PONG %s\r\n", event.getPingValue()));
    }

    public void sendMessage(String message) {
        ircBot.sendIRC().message("#" + this.channel, message);
    }

    public void joinChannel(String channel) {
        String oldChannel = this.channel;
        this.channel = channel.toLowerCase();
        if (ircBot.isConnected()) {
            myExecutor.execute(() -> {
                ircBot.sendRaw().rawLine("PART #" + oldChannel); // Leave the channel
                ircBot.sendIRC().joinChannel("#" + this.channel); // Join the new channel
                ircBot.sendCAP().request("twitch.tv/membership", "twitch.tv/tags", "twitch.tv/commands"); // Ask for capabilities
            });
        }
    }
}

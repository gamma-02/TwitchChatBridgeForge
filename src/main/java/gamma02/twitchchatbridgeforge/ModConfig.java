package gamma02.twitchchatbridgeforge;

import com.electronwill.nightconfig.core.Config;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModConfig {
    public static class Client {
        public static final String DEFAULT_CHANNEL = "";
        public static final String DEFAULT_USERNAME = "";
        public static final String DEFAULT_OAUTH_KEY = "";
        public static final String DEFAULT_PREFIX = ":";
        public static final boolean DEFAULT_LOGGING = false;
        public static final String DEFAULT_DATE_FORMAT = "[H:mm] ";
        public static final List<String> DEFAULT_IGNORE_LIST = new ArrayList<>();
        public static final boolean DEFAULT_TWITCH_WATCH_SUGGESTIONS = false;
        public static final boolean DEFAULT_BROADCAST = false;
        public static final String DEFAULT_BROADCAST_PREFIX = "[Twitch] ";



        private final ForgeConfigSpec.ConfigValue<String> channel;
        private final ForgeConfigSpec.ConfigValue<String> username;
        private final ForgeConfigSpec.ConfigValue<String> oauthKey;
        private final ForgeConfigSpec.ConfigValue<String> prefix;
        private final ForgeConfigSpec.ConfigValue<Boolean> logging;
        private final ForgeConfigSpec.ConfigValue<String> dateFormat;
        private final ForgeConfigSpec.ConfigValue<List<String>> ignoreList;
        private final ForgeConfigSpec.ConfigValue<Boolean> twitchWatchSuggestions;
        private final ForgeConfigSpec.ConfigValue<Boolean> broadcast;
        private final ForgeConfigSpec.ConfigValue<String> broadcastPrefix;
        public Client(ForgeConfigSpec.Builder builder){

            builder.push(new TranslationTextComponent("config.twitchchatbridgeforge.title").getString());
            builder.comment(new TranslationTextComponent("config.twitchchatbridgeforge.apology").getString());
            this.channel = builder.comment("This is the default channel").define("default channel", DEFAULT_CHANNEL);
            this.username = builder.comment("This is the username you use to send a twitch chat message").define("username", DEFAULT_USERNAME);
            this.oauthKey = builder.comment("This is your OAuth key, VERY IMPORTANT!! to get one go to: https://twitchapps.com/tmi/").define("OAuth key", DEFAULT_OAUTH_KEY);
            this.broadcast = builder.comment("This allows the twitch bot to brodcast").define("brodcast", DEFAULT_BROADCAST);
            this.prefix = builder.comment("This is the prefix that you use to send a message in the Twitch chat").define("prefix", DEFAULT_PREFIX);
            this.broadcastPrefix = builder.comment("This is the prefix that you see in front of a Twitch message").define("broadcast prefix", DEFAULT_BROADCAST_PREFIX);
            this.dateFormat = builder.comment("This is the date format of the timestamps you see").define("date format", DEFAULT_DATE_FORMAT);
            this.ignoreList = builder.comment("this is the list of ignored users in Twitch chat").define("ignored users", DEFAULT_IGNORE_LIST);
            this.logging = builder.comment("This allows logging of Twitch commands in Minecraft").define("allow logging", DEFAULT_LOGGING);
            this.twitchWatchSuggestions = builder.comment("This allows you to suggest watchs of other users in the game").define("watch suggestions", DEFAULT_TWITCH_WATCH_SUGGESTIONS);
        }
        public String getChannel() {
            return channel.get();
        }

        public void setChannel(String channel) {
            this.channel.set(channel);
        }

        public String getUsername() {
            return username.get();
        }

        public boolean getLogging(){
            return logging.get();
        }

        public void setLogging(boolean logging){
            this.logging.set(logging);
        }

        public void setUsername(String username) {
            this.username.set(username);
        }

        public String getOauthKey() {
            return oauthKey.get();
        }

        public void setOauthKey(String oauthKey) {
            this.oauthKey.set(oauthKey);
        }

        public String getPrefix() {
            return prefix.get();
        }

        public void setPrefix(String prefix) {
            this.prefix.set(prefix);
        }

        public String getDateFormat() {
            return dateFormat.get();
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat.set(dateFormat);
        }

        public List<String> getIgnoreList() {
            return ignoreList.get();
        }

        public void setIgnoreList(List<String> ignoreList) {
            // Force all usernames to be lowercase
            this.ignoreList.set(ignoreList.parallelStream().map(String::toLowerCase).collect(Collectors.toList()));
        }

        public boolean areTwitchWatchSuggestionsEnabled() {
            return twitchWatchSuggestions.get();
        }

        public void setTwitchWatchSuggestions(boolean twitchWatchSuggestions) {
            this.twitchWatchSuggestions.set(twitchWatchSuggestions);
        }

        public boolean isBroadcastEnabled() {
            return broadcast.get();
        }

        public void setBroadcastEnabled(boolean broadcastEnabled) {
            this.broadcast.set(broadcastEnabled);
        }

        public String getBroadcastPrefix() {
            return broadcastPrefix.get();
        }

        public void setBroadcastPrefix(String broadcastPrefix) {
            this.broadcastPrefix.set(broadcastPrefix);
        }




    }
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;
    static {
        Pair<Client, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = commonSpecPair.getLeft();
        CLIENT_SPEC = commonSpecPair.getRight();
    }


}
package gamma02.twitchchatbridgeforge.mixin;

import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import gamma02.twitchchatbridgeforge.TwitchIntegration.CalculateMinecraftColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Date;

@Mixin(Screen.class)
public class ScreenMixin {
    @Inject(at = @At("HEAD"), method = "sendMessage(Ljava/lang/String;Z)V", cancellable = true)
    private void sendMessage(String text, boolean showInHistory, CallbackInfo info) {
        ModConfig.Client config = ModConfig.CLIENT;


        String prefix = config.getPrefix();

        // Allow users to write /twitch commands (such as disabling and enabling the mod) when their prefix is "".
        if (prefix.equals("") && text.startsWith("/twitch")) {
            return; // Don't cancel the message, return execution to the real method
        }

        // If the message is a twitch message
        if (text.startsWith(prefix)) {
            if (TwitchChatBridgeForge.bot != null && TwitchChatBridgeForge.bot.isConnected()) {
                String textWithoutPrefix = text.substring(text.indexOf(prefix) + prefix.length());
                TwitchChatBridgeForge.bot.sendMessage(textWithoutPrefix); // Send the message to the Twitch IRC Chat

                Date currentTime = new Date();
                String formattedTime = TwitchChatBridgeForge.formatDateTwitch(currentTime);

                String username = TwitchChatBridgeForge.bot.getUsername();
                TextFormatting userColor;
                if (TwitchChatBridgeForge.bot.isFormattingColorCached(username)) {
                    userColor = TwitchChatBridgeForge.bot.getFormattingColor(username);
                } else {
                    userColor = CalculateMinecraftColor.getDefaultUserColor(username);
                    TwitchChatBridgeForge.bot.putFormattingColor(username, userColor);
                }

                boolean isMeMessage = textWithoutPrefix.startsWith("/me");

                // Add the message to the Minecraft Chat
                TwitchChatBridgeForge.addTwitchMessage(formattedTime, username, isMeMessage ? textWithoutPrefix.substring(4) : textWithoutPrefix, userColor, isMeMessage);
                Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(text);
                info.cancel();
            } else {
                TwitchChatBridgeForge.addNotification(new TranslationTextComponent("text.twitchchat.chat.integration_disabled"));
            }
        }
    }
}

package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import gamma02.twitchchatbridgeforge.TwitchIntegration.Bot;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class TwitchEnableCommand implements SubCommand {
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return Commands.literal("enable")
                // The command to be executed if the command "twitch" is entered with the argument "enable"
                // It starts up the irc bot.
                .executes(ctx -> {
                    ModConfig.Client config = ModConfig.CLIENT;

                    if (TwitchChatBridgeForge.bot != null && TwitchChatBridgeForge.bot.isConnected()) {
                        ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.enable.already_enabled"), ModConfig.CLIENT.getLogging());
                        return 1;
                    }

                    if (config.getUsername().equals("") || config.getOauthKey().equals("")) {
                        ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.enable.set_config"), ModConfig.CLIENT.getLogging());
                        return -1;
                    }

                    if (config.getChannel().equals("")) {
                        ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.enable.select_channel"), ModConfig.CLIENT.getLogging());
                    }

                    TwitchChatBridgeForge.bot = new Bot(config.getUsername(), config.getOauthKey(), config.getChannel());
                    TwitchChatBridgeForge.bot.start();
                    ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.enable.connecting").mergeStyle(TextFormatting.DARK_GRAY), ModConfig.CLIENT.getLogging());
                    // Return a result. -1 is failure, 0 is a pass and 1 is success.
                    return 1;
                });
    }
}

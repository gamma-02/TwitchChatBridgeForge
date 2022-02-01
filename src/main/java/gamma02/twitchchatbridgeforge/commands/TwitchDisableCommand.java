package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;


public class TwitchDisableCommand implements SubCommand {
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return LiteralArgumentBuilder.<CommandSource>literal("disable")
                // The command to be executed if the command "twitch" is entered with the argument "disable"
                // It shuts down the irc bot.
                .executes(ctx -> {
                    if (TwitchChatBridgeForge.bot == null || !TwitchChatBridgeForge.bot.isConnected()) {
                        ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.disable.already_disabled"), ModConfig.CLIENT.getLogging());
                        return 1;
                    }

                    TwitchChatBridgeForge.bot.stop();
                    ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.disable.disabled").mergeStyle(
                            TextFormatting.DARK_GRAY), ModConfig.CLIENT.getLogging());

                    // Return a result. -1 is failure, 0 is a pass and 1 is success.
                    return 1;
                });
    }
}

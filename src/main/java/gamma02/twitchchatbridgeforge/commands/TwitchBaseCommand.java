package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.CommandDispatcher;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;


public class TwitchBaseCommand implements BaseCommand {
    public void registerCommands(CommandDispatcher<ClientCommandSource> dispatcher) {
        Commands.literal("twitch")
                // The command to be executed if the command "twitch" is entered with the argument "enable"
                .then(new TwitchEnableCommand().getArgumentBuilder())
                // The command to be executed if the command "twitch" is entered with the argument "disable"
                .then(new TwitchDisableCommand().getArgumentBuilder())
                .then(new TwitchWatchCommand().getArgumentBuilder())
                .then(new TwitchBroadcastCommand().getArgumentBuilder())
                .then(new TwitchBotUsernameCommand().getArgumentBuilder())
                .executes(source -> {
                    source.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.base.noargs1").mergeStyle(TextFormatting.DARK_GRAY), ModConfig.CLIENT.getLogging());
                    source.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.base.noargs2").mergeStyle(TextFormatting.DARK_GRAY), ModConfig.CLIENT.getLogging());
                    return 1;
                });
    }
}

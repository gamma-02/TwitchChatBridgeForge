package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;


public class TwitchBroadcastCommand implements SubCommand {
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return Commands.literal("broadcast")
                // The command to be executed if the command "twitch" is entered with the argument "broadcast"
                // It requires true/false as an argument.
                // It will toggle the broadcast flag in the config and
                // if enabled, will relay twitch messages as say-chat messages to the server.
                .then(Commands.argument("enabled", BoolArgumentType.bool())
                        .executes(ctx -> {
                            boolean enabled = BoolArgumentType.getBool(ctx, "enabled");

                            ModConfig.CLIENT.setBroadcastEnabled(enabled);
                            // Also switch channels if the bot has been initialized
                            if (enabled) {
                                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.broadcast.enabled"), ModConfig.CLIENT.getLogging());
                            } else {
                                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.broadcast.disabled"), ModConfig.CLIENT.getLogging());
                            }

                            return 1;
                        }));
    }
}

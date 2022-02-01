package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;

public class TwitchWatchCommand implements SubCommand {
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return LiteralArgumentBuilder.<CommandSource>literal("watch")
                // The command to be executed if the command "twitch" is entered with the argument "watch"
                // It requires channel_name as an argument.
                // It will switch channels in the config to the channel name provided and
                // if the bot is connected to some channel, it will switch channels on the fly.
                .then(Commands.argument("channel_name", StringArgumentType.string())
                        .suggests(new TwitchWatchSuggestionProvider())
                        .executes(ctx -> {
                            String channelName = StringArgumentType.getString(ctx, "channel_name");

                            ModConfig.CLIENT.setChannel(channelName);
                            // Also switch channels if the bot has been initialized
                            if (TwitchChatBridgeForge.bot != null) {
                                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.watch.switching", channelName), ModConfig.CLIENT.getLogging());
                                TwitchChatBridgeForge.bot.joinChannel(channelName);
                            } else {
                                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.watch.connect_on_enable", channelName), ModConfig.CLIENT.getLogging());
                            }
//                            ModConfig.CLIENT_SPEC.save();
                            return 1;
                        }));
    }
}

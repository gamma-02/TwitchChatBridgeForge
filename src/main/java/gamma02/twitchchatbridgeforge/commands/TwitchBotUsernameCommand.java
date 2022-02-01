package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import gamma02.twitchchatbridgeforge.ModConfig;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.Mod;

public class TwitchBotUsernameCommand implements SubCommand {
    @Override
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return Commands.literal("watch").then(Commands.argument("bot username", StringArgumentType.string()).suggests(new TwitchWatchSuggestionProvider()).executes((ctx) ->{
            if(ModConfig.CLIENT.getUsername().equals(StringArgumentType.getString(ctx, "bot username"))){
                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.username_change_error_already_named"), ModConfig.CLIENT.getLogging());
                return 0;
            }else {
                ModConfig.CLIENT.setUsername(StringArgumentType.getString(ctx, "bot username"));
                ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.username_change_regular"), ModConfig.CLIENT.getLogging());
                return Command.SINGLE_SUCCESS;
            }
        }));
    }
}

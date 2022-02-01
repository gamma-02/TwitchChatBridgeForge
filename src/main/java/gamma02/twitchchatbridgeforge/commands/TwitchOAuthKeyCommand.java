package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import gamma02.twitchchatbridgeforge.ModConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.server.command.ConfigCommand;

public class TwitchOAuthKeyCommand implements SubCommand {
    @Override
    public ArgumentBuilder<CommandSource, ?> getArgumentBuilder() {
        return Commands.literal("oauth_key").executes((ctx) -> {
            ctx.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.oauth_warn"), ModConfig.CLIENT.getLogging());
            return Command.SINGLE_SUCCESS;
        });
    }
}

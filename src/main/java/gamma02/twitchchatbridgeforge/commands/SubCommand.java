package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import net.minecraft.command.CommandSource;

public interface SubCommand {
    ArgumentBuilder<CommandSource, ?> getArgumentBuilder();
}

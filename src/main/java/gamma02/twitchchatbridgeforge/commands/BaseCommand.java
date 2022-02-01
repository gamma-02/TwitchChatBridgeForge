package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.CommandDispatcher;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;


public interface BaseCommand {
    void registerCommands(CommandDispatcher<ClientCommandSource> dispatcher);
}
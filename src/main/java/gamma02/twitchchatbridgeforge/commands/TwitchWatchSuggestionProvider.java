package gamma02.twitchchatbridgeforge.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TwitchWatchSuggestionProvider implements SuggestionProvider<CommandSource> {

    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        if (Minecraft.getInstance().world != null && ModConfig.CLIENT.areTwitchWatchSuggestionsEnabled()) {
            List<AbstractClientPlayerEntity> players = Minecraft.getInstance().world.getPlayers();

            for (AbstractClientPlayerEntity player : players) {
                builder.suggest(player.getName().getString());
            }
        }

        return builder.buildFuture();
    }
}

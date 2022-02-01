package gamma02.twitchchatbridgeforge.ClientCommandHelpers;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gamma02.twitchchatbridgeforge.TwitchChatBridgeForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static gamma02.twitchchatbridgeforge.TwitchChatBridgeForge.*;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientCommandsChatListener {
    @SubscribeEvent
    public static void playerChat(ClientChatEvent event){
        if(!isClientSideCommandsPresent) {
            if (event.getMessage().startsWith(TwitchChatBridgeForge.getMarker() + "")) {
                try {
                    ClientPlayerEntity player = Minecraft.getInstance().player;
                    ClientCommandSource source = new ClientCommandSource(player, player.getPositionVec(), player.getPitchYaw(), null, 4, player.getName().getString(), player.getDisplayName(), null, player);

                    ParseResults<CommandSource> parse = TwitchChatBridgeForge.getDispatcher().parse(event.getMessage().substring(1), source);
                    if (parse.getContext().getNodes().size() > 0) {
                        event.setCanceled(true);
                        Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(event.getOriginalMessage());
                        TwitchChatBridgeForge.getDispatcher().execute(parse);
                    }
                } catch (CommandSyntaxException e) {
                }
            }
        }
    }
}

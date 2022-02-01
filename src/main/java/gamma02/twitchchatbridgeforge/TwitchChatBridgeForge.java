package gamma02.twitchchatbridgeforge;

import com.ibm.icu.impl.Pair;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import gamma02.twitchchatbridgeforge.ClientCommandHelpers.ClientCommandSource;
import gamma02.twitchchatbridgeforge.TwitchIntegration.Bot;
import gamma02.twitchchatbridgeforge.commands.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.*;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.CallbackI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TwitchChatBridgeForge.MODID)
public class TwitchChatBridgeForge {
    public static Bot bot;
    public static final String MODID = "twitchchatbridgeforge";
    private static final CommandDispatcher<CommandSource> COMMANDS_DISPATCHER = new CommandDispatcher();
    public static boolean isClientSideCommandsPresent;


    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public TwitchChatBridgeForge() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

//        ForgeConfigSpec configBuilder = new ForgeConfigSpec.Builder().
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }


    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        if(!isClientSideCommandsPresent) {
            COMMANDS_DISPATCHER.register(Commands.literal("twitch")
                    // The command to be executed if the command "twitch" is entered with the argument "enable"
                    .then(new TwitchEnableCommand().getArgumentBuilder())
                    // The command to be executed if the command "twitch" is entered with the argument "disable"
                    .then(new TwitchDisableCommand().getArgumentBuilder())
                    .then(new TwitchWatchCommand().getArgumentBuilder())
                    .then(new TwitchBroadcastCommand().getArgumentBuilder())
                    .executes(source -> {
                        source.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.base.noargs1"), ModConfig.CLIENT.getLogging());
                        source.getSource().sendFeedback(new TranslationTextComponent("text.twitchchatbridgeforge.command.base.noargs2"), ModConfig.CLIENT.getLogging());
                        return 1;
                    })
            );
        }
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("TwitchChatBridgeForge", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });

//        if(isClientSideCommandsPresent){
//            InterModComms.sendTo("clientcommands", "register_command", (CommandDispatcher<CommandSource> dispatcher) -> {
//                (new TwitchBaseCommand()).registerCommands(dispatcher);
//                return;
//            });
//        }

    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
//        @SubscribeEvent
//        public static void onCommandRegistry(final RegisterCommandsEvent){
//            CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
//            if(event.getEnvironment() == Commands.EnvironmentType.INTEGRATED)
//        }

    }
    public static char getMarker(){
        return '!';
    }

    public static CommandDispatcher<CommandSource> getDispatcher(){

        return COMMANDS_DISPATCHER;
    }
    public static void addTwitchMessage(String time, String username, String message, TextFormatting textColor, boolean isMeMessage) {
        IFormattableTextComponent timestampText = new StringTextComponent(time);
        IFormattableTextComponent usernameText = new StringTextComponent(username).mergeStyle(textColor);
        IFormattableTextComponent messageBodyText;

        if (!isMeMessage) {
            messageBodyText = new StringTextComponent(": " + message);
        } else {
            // '/me' messages have the same color as the username in the Twitch website.
            // And thus I set the color of the message to be the same as the username.
            // They also don't have a colon after the username.
            messageBodyText = new StringTextComponent(" " + message).mergeStyle(textColor);

            // In Minecraft, a '/me' message is marked with a star before the name, like so:
            //
            // <Player> This is a normal message
            // * Player this is a '/me' message
            //
            // The star is always white (that's why I don't format it).
            usernameText = new StringTextComponent("* ").appendSibling(usernameText);
        }

        if (ModConfig.CLIENT.isBroadcastEnabled()) {
            try {
                String plainTextMessage = ModConfig.CLIENT.getBroadcastPrefix() + username + ": " + message;
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.sendChatMessage(plainTextMessage);
                }
            } catch (NullPointerException e) {
                System.err.println("TWITCH BOT FAILED TO BROADCAST MESSAGE: " + e.getMessage());
            }
        } else {
            Minecraft.getInstance().ingameGUI.sendChatMessage(ChatType.CHAT,
                    timestampText
                            .appendSibling(usernameText)
                            .appendSibling(messageBodyText), UUID.randomUUID());
        }
    }
    public static void addNotification(IFormattableTextComponent message) {
        Minecraft.getInstance().ingameGUI.sendChatMessage(ChatType.CHAT, message.mergeStyle(TextFormatting.DARK_GRAY), UUID.randomUUID());
    }

    public static String formatTMISentTimestamp(String tmiSentTS) {
        return formatTMISentTimestamp(Long.parseLong(tmiSentTS));
    }
    public static String formatTMISentTimestamp(long tmiSentTS) {
        Date date = new Date(tmiSentTS);
        return formatDateTwitch(date);
    }
    public static String formatDateTwitch(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(ModConfig.CLIENT.getDateFormat());
        return sf.format(date);
    }


}

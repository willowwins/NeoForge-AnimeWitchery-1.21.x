package net.willowins.animewitchery;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.item.ModCreativeModeTabs;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.particle.ModParticles;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.network.ManaSyncPayload;
import net.willowins.animewitchery.network.ModMessages;
import net.willowins.animewitchery.recipe.ModRecipes;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.willowins.animewitchery.item.ModItems.BLAZESACK;

@Mod(AnimeWitchery.MOD_ID)
public class AnimeWitchery {
    public static final String MOD_ID = "animewitchery";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AnimeWitchery(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // This line registers "this" (the instance object) to the main game event bus
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        ModMessages.register(modEventBus);
        ModAttachments.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModParticles.register(modEventBus);
        net.willowins.animewitchery.fluid.ModFluids.register(modEventBus);
        ModDataComponentTypes.register(modEventBus); // Make sure your components are registered here too!
        ModRecipes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {


    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        // Passive mana regeneration disabled
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ManaData data = serverPlayer.getData(ModAttachments.MANA);
            PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(data.current(), data.max()));
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}

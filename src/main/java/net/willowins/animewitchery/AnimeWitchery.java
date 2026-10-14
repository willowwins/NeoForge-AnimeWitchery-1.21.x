package net.willowins.animewitchery;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.willowins.animewitchery.effect.KamikazeRitualEffect;
import net.willowins.animewitchery.effect.ModEffects;
import net.willowins.animewitchery.util.ModExplosionManager;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.item.ModCreativeModeTabs;
import net.willowins.animewitchery.item.ModArmorMaterials;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.particle.ModParticles;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.willowins.animewitchery.network.ModMessages;
import net.willowins.animewitchery.recipe.ModRecipes;
import net.willowins.animewitchery.mana.PlayerMana;
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
        net.willowins.animewitchery.menu.ModMenus.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModItems.register(modEventBus);
        ModArmorMaterials.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModParticles.register(modEventBus);
        net.willowins.animewitchery.fluid.ModFluids.register(modEventBus);
        ModDataComponentTypes.register(modEventBus); // Make sure your components are registered here too!
        ModRecipes.register(modEventBus);
        ModEffects.register(modEventBus);
        net.willowins.animewitchery.worldgen.ModFeatures.register(modEventBus);
        net.willowins.animewitchery.potion.ModPotions.register(modEventBus);

        NeoForge.EVENT_BUS.register(ModExplosionManager.class);
        NeoForge.EVENT_BUS.register(net.willowins.animewitchery.potion.ModBrewingRecipes.class);
        NeoForge.EVENT_BUS.register(net.willowins.animewitchery.event.EnchantmentBreakHandler.class);
        NeoForge.EVENT_BUS.register(net.willowins.animewitchery.event.ExpBoostHandler.class);
        NeoForge.EVENT_BUS.register(net.willowins.animewitchery.event.ChestplateElytraFlight.class);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        event.enqueueWork(AnimeWitchery::registerFlammables);
    }

    private static void registerFlammables() {
        net.minecraft.world.level.block.FireBlock fire = (net.minecraft.world.level.block.FireBlock) Blocks.FIRE;
        fire.setFlammable(ModBlocks.DARK_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.ROSEWILLOW_LOG.get(), 5, 5);
        fire.setFlammable(ModBlocks.ROSEWILLOW_LOG_BLOOMING.get(), 5, 5);
        fire.setFlammable(ModBlocks.DARK_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.ROSEWILLOW_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.DARK_PLANKS.get(), 5, 20);
        fire.setFlammable(ModBlocks.ROSEWILLOW_PLANKS.get(), 5, 20);
        fire.setFlammable(ModBlocks.DARK_PLANKS_STAIRS.get(), 5, 20);
        fire.setFlammable(ModBlocks.DARK_PLANKS_SLAB.get(), 5, 20);
        fire.setFlammable(ModBlocks.DARK_SAPLING.get(), 60, 100);
        fire.setFlammable(ModBlocks.RUIN_LEAVES.get(), 30, 60);
        fire.setFlammable(ModBlocks.RUIN_LEAF_LITTER.get(), 30, 60);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {


    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        // Passive mana regeneration: 5 mana every 20 ticks (1 second)
        if (serverPlayer.tickCount % 20 != 0) {
            return;
        }
        PlayerMana.regen(serverPlayer, 5);

        // Locked players slowly trickle up to double their max mana
        if (PlayerMana.isLockedPlayer(serverPlayer)) {
            int current = PlayerMana.getMana(serverPlayer);
            int max = PlayerMana.getMaxMana(serverPlayer);
            if (current < max * 2) {
                PlayerMana.setMana(serverPlayer, current + 1);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PlayerMana.sync(serverPlayer);
        }
    }

    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof net.minecraft.world.entity.player.Player player
                && KamikazeRitualEffect.onPlayerDeath(player, event.getSource())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
}

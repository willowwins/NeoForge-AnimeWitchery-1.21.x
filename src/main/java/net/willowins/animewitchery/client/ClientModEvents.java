package net.willowins.animewitchery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.ModBlockEntities;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STARDROP_LANTERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STARLIGHT_POOL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRYSTALLIZED_STARLIGHT.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityType.BEACON, LunaBeaconRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_POOL_BE.get(), StarlightPoolBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get(), StarlightInfusionAltarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_INFUSION_PEDESTAL_BE.get(), StarlightInfusionPedestalRenderer::new);
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        ManaOverlay overlay = new ManaOverlay();
        event.registerAbove(
                VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "mana_overlay"),
                overlay::render
        );
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = minecraft.level;

        if (level == null || minecraft.player == null) return;

        BlockPos playerPos = minecraft.player.blockPosition();
        int range = 32;

        for (BlockPos pos : BlockPos.betweenClosed(
                playerPos.offset(-range, -range, -range),
                playerPos.offset(range, range, range))) {



            if (level.getBlockState(pos).is(ModBlocks.STARDROP_LANTERN.get())) {
                if (minecraft.player.getRandom().nextDouble() < 0.05) {
                    double x = pos.getX() + 0.5;
                    double y = pos.getY() + 0.5;
                    double z = pos.getZ() + 0.5;

                    level.addParticle(
                            net.willowins.animewitchery.particle.ModParticles.STARDROP_PARTICLE.get(),
                            x + (minecraft.player.getRandom().nextDouble() - 0.5) * 0.9,
                            y + (minecraft.player.getRandom().nextDouble() - 0.5) * 0.65,
                            z + (minecraft.player.getRandom().nextDouble() - 0.5) * 0.9,
                            (minecraft.player.getRandom().nextDouble() - 0.5) * 0.1,
                            (minecraft.player.getRandom().nextDouble() - 0.5) * 0.1,
                            (minecraft.player.getRandom().nextDouble() - 0.5) * 0.1
                    );
                }
            }
        }
    }
}




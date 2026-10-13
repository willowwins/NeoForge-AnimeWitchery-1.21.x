package net.willowins.animewitchery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.client.renderer.AlchemyTableRenderer;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STARDROP_LANTERN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ROSEWILLOW_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.ROSEWILLOW_LEAVES.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RUIN_LEAF_LITTER.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STARLIGHT_POOL.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRYSTALLIZED_STARLIGHT.get(), RenderType.translucent());
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.ALCHEMY_TABLE_BE.get(), AlchemyTableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityType.BEACON, LunaBeaconRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_POOL_BE.get(), StarlightPoolBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get(), StarlightInfusionAltarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.STARLIGHT_INFUSION_PEDESTAL_BE.get(), StarlightInfusionPedestalRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DARK_FOUNTAIN_BE.get(), DarkFountainBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.DARK_PEDESTAL_BE.get(), DarkPedestalBlockEntityRenderer::new);

    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColors colors = event.getBlockColors();
        colors.register(new BlockColor() {
            @Override
            public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
                return 0x4488FF;
            }
        }, ModBlocks.DARK_LEAVES.get());
        colors.register(new BlockColor() {
            @Override
            public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
                return 0x334466;
            }
        }, ModBlocks.RUIN_LEAVES.get());
        colors.register((state, level, pos, tintIndex) -> 0xC060D8, ModBlocks.ROSEWILLOW_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> 0xC060D8, ModBlocks.ROSEWILLOW_LEAVES.get().asItem());
        event.register((stack, tintIndex) -> 0x4488FF, ModBlocks.DARK_LEAVES.get().asItem());
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "mana_overlay"),
                new ManaOverlay()::render
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




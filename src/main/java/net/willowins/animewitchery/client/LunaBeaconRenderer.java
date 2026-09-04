package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.willowins.animewitchery.block.ModBlocks;

public class LunaBeaconRenderer extends BeaconRenderer {
    private static final int LUNA_PINK = 0xFFFFC6F9;
    private static final int LUNA_BLUE = 0xFFCBEEFF;
    private static final int LUNA_WHITE = 0xFFFFFFFF;
    private static final int GRADIENT_HEIGHT = 18;

    public LunaBeaconRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BeaconBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getBeamSections().isEmpty() || !hasLunaCrystalInBeam(blockEntity)) {
            super.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
            return;
        }

        long gameTime = blockEntity.getLevel().getGameTime();
        for (int yOffset = 0; yOffset < MAX_RENDER_Y; yOffset++) {
            renderBeaconBeam(
                    poseStack,
                    bufferSource,
                    BEAM_LOCATION,
                    partialTick,
                    1.0F,
                    gameTime,
                    yOffset,
                    1,
                    gradientColor(yOffset),
                    0.2F,
                    0.25F);
        }
    }

    private static boolean hasLunaCrystalInBeam(BeaconBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        BlockPos beaconPos = blockEntity.getBlockPos();
        int topY = level.getHeight() + level.getMinBuildHeight();

        for (BlockPos pos = beaconPos.above(); pos.getY() < topY; pos = pos.above()) {
            if (level.getBlockState(pos).is(ModBlocks.LUNA_CRYSTAL.get())) {
                return true;
            }
        }

        return false;
    }

    private static int gradientColor(int yOffset) {
        int wrappedY = Math.floorMod(yOffset, GRADIENT_HEIGHT);
        float progress = (float) wrappedY / (float) GRADIENT_HEIGHT;

        if (progress < 0.5F) {
            return lerpColor(LUNA_PINK, LUNA_BLUE, progress * 2.0F);
        }

        return lerpColor(LUNA_BLUE, LUNA_WHITE, (progress - 0.5F) * 2.0F);
    }

    private static int lerpColor(int start, int end, float amount) {
        int red = lerp((start >> 16) & 0xFF, (end >> 16) & 0xFF, amount);
        int green = lerp((start >> 8) & 0xFF, (end >> 8) & 0xFF, amount);
        int blue = lerp(start & 0xFF, end & 0xFF, amount);
        return 0xFF000000 | red << 16 | green << 8 | blue;
    }

    private static int lerp(int start, int end, float amount) {
        return start + Math.round((end - start) * amount);
    }
}

package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.block.entity.DarkPedestalBlockEntity;
import net.willowins.animewitchery.block.ModBlocks;

import java.util.List;

public class DarkPedestalBlockEntityRenderer implements BlockEntityRenderer<DarkPedestalBlockEntity> {

    public DarkPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DarkPedestalBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() == null) return;

        BlockState state = blockEntity.getBlockState();
        var model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);

        poseStack.pushPose();
        // No translation - block model coordinates are 0-16 relative to block corner

        VertexConsumer consumer = bufferSource.getBuffer(RenderType.solid());
        renderModel(poseStack.last(), consumer, model, state, blockEntity.getBlockPos(), packedLight, packedOverlay);

        // Render held item on top of pedestal
        ItemStack heldItem = blockEntity.getHeldItem();
        if (!heldItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.15, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(heldItem, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private void renderModel(com.mojang.blaze3d.vertex.PoseStack.Pose pose, VertexConsumer consumer,
                             net.minecraft.client.resources.model.BakedModel model,
                             BlockState state, net.minecraft.core.BlockPos pos, int light, int overlay) {
        RandomSource random = RandomSource.create(pos.asLong());

        for (Direction dir : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(state, dir, random);
            for (BakedQuad quad : quads) {
                consumer.putBulkData(pose, quad, 1.0F, 1.0F, 1.0F, 1.0F, light, overlay, true);
            }
        }
        // Face-independent quads
        for (BakedQuad quad : model.getQuads(state, null, random)) {
            consumer.putBulkData(pose, quad, 1.0F, 1.0F, 1.0F, 1.0F, light, overlay, true);
        }
    }
}
package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.willowins.animewitchery.block.entity.StarlightInfusionPedestalBlockEntity;

/**
 * Renders the item held by a Starlight Infusion Pedestal, floating gently above the pedestal top.
 */
public class StarlightInfusionPedestalRenderer implements BlockEntityRenderer<StarlightInfusionPedestalBlockEntity> {

    public StarlightInfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarlightInfusionPedestalBlockEntity pedestal, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = pedestal.getHeldItem();
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.55F, 0.5F);

        // Gentle slow bob.
        float bob = (float) (Math.sin((pedestal.getLevel().getGameTime() + partialTick) * 0.12F) * 0.05);
        poseStack.translate(0.0F, bob, 0.0F);

        // Slow continuous spin around the vertical axis.
        float angle = (pedestal.getLevel().getGameTime() + partialTick) * 2.0F;
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));
        poseStack.scale(0.5F, 0.5F, 0.5F);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                poseStack, bufferSource, pedestal.getLevel(), 0);

        poseStack.popPose();
    }
}

package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.willowins.animewitchery.block.entity.StarlightInfusionAltarBlockEntity;

/**
 * Renders the base item sitting on top of a Starlight Infusion Altar. While a craft is running
 * the base item is consumed and the pending result is shown floating above the altar instead.
 */
public class StarlightInfusionAltarRenderer implements BlockEntityRenderer<StarlightInfusionAltarBlockEntity> {

    public StarlightInfusionAltarRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarlightInfusionAltarBlockEntity altar, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = !altar.getPendingResult().isEmpty() ? altar.getPendingResult() : altar.getBaseItem();
        if (stack.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5F, 1.52F, 0.5F);

        // Gentle slow bob; the amplitude is the offset, the frequency is small so it floats lazily.
        float speed = altar.getProgress() > 0 ? 0.25F : 0.12F;
        float bob = (float) (Math.sin((altar.getLevel().getGameTime() + partialTick) * speed) * 0.05);
        poseStack.translate(0.0F, bob, 0.0F);

        // Slow continuous spin around the vertical axis.
        float angle = (altar.getLevel().getGameTime() + partialTick) * 2.0F;
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(angle));
        poseStack.scale(0.6F, 0.6F, 0.6F);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                poseStack, bufferSource, altar.getLevel(), 0);

        poseStack.popPose();
    }
}

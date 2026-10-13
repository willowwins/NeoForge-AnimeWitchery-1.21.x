package net.willowins.animewitchery.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class HudRenderer implements ICurioRenderer {
    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        LivingEntity entity = slotContext.entity();
        EntityModel<T> model = renderLayerParent.getModel();
        if (model instanceof HeadedModel headedModel) {
            matrixStack.pushPose();
            headedModel.getHead().translateAndRotate(matrixStack);

            // Apply vanilla CustomHeadLayer transformations for standard items
            matrixStack.translate(0.0F, -0.25F, 0.0F);
            matrixStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F));
            matrixStack.scale(0.625F, -0.625F, -0.625F);

            // Render the item in the head context
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    entity,
                    stack,
                    ItemDisplayContext.HEAD,
                    false,
                    matrixStack,
                    renderTypeBuffer,
                    entity.level(),
                    light,
                    OverlayTexture.NO_OVERLAY,
                    entity.getId()
            );
            matrixStack.popPose();
        }
    }
}
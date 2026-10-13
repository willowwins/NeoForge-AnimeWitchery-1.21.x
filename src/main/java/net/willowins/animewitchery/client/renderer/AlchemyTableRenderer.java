package net.willowins.animewitchery.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.willowins.animewitchery.block.entity.AlchemyTableBlockEntity;
import net.willowins.animewitchery.client.model.AlchemyTableModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.ArrayList;
import java.util.List;

/** GeckoLib model renderer plus the Fabric table's floating alchemy items. */
public class AlchemyTableRenderer extends GeoBlockRenderer<AlchemyTableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public AlchemyTableRenderer(BlockEntityRendererProvider.Context context) {
        super(new AlchemyTableModel());
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(AlchemyTableBlockEntity table, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        super.render(table, partialTick, poseStack, buffers, packedLight, packedOverlay);
        if (table.getLevel() == null) return;

        ItemStack result = table.getItem(0);
        if (!result.isEmpty()) renderFloatingResult(result, table, partialTick, poseStack,
                buffers, packedLight, packedOverlay);
        renderOrbitingIngredients(table, partialTick, poseStack, buffers, packedLight, packedOverlay);
    }

    private void renderFloatingResult(ItemStack stack, AlchemyTableBlockEntity table, float partialTick,
                                      PoseStack poseStack, MultiBufferSource buffers,
                                      int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5 + Math.sin((table.getLevel().getGameTime() + partialTick) * 0.05) * 0.08, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees((table.getLevel().getGameTime() + partialTick) * 1.0F));
        poseStack.scale(1.4F, 1.4F, 1.4F);
        itemRenderer.renderStatic(stack, ItemDisplayContext.GROUND, packedLight, packedOverlay,
                poseStack, buffers, table.getLevel(), 0);
        poseStack.popPose();
    }

    private void renderOrbitingIngredients(AlchemyTableBlockEntity table, float partialTick,
                                           PoseStack poseStack, MultiBufferSource buffers,
                                           int packedLight, int packedOverlay) {
        List<ItemStack> ingredients = new ArrayList<>();
        for (int slot = 1; slot < AlchemyTableBlockEntity.SLOT_COUNT; slot++) {
            ItemStack stack = table.getItem(slot);
            if (!stack.isEmpty()) ingredients.add(stack);
        }
        if (ingredients.isEmpty()) return;

        double time = table.getLevel().getGameTime() + partialTick;
        double speed = table.isActivated() ? 0.07 : 0.02;
        double radius = table.isActivated() ? 1.0 : 1.25;
        double angleStep = Math.PI * 2.0 / ingredients.size();
        for (int i = 0; i < ingredients.size(); i++) {
            double angle = time * speed * (1.0 + ingredients.size() * 0.05) + i * angleStep;
            double bob = Math.sin(time * (0.06 + (i % 3) * 0.01) + i * 37.42) * 0.08;
            poseStack.pushPose();
            poseStack.translate(0.5 + Math.cos(angle) * radius, 1.10 + bob,
                    0.5 + Math.sin(angle) * radius);
            poseStack.mulPose(Axis.YP.rotation((float) (-angle + Math.PI / 2 + (time + i * 10) * 0.04)));
            poseStack.mulPose(Axis.XP.rotationDegrees(15));
            poseStack.scale(1.5F, 1.5F, 1.5F);
            itemRenderer.renderStatic(ingredients.get(i), ItemDisplayContext.GROUND, packedLight,
                    packedOverlay, poseStack, buffers, table.getLevel(), 0);
            poseStack.popPose();
        }
    }
}

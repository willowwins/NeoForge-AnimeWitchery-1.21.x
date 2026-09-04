package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.willowins.animewitchery.block.entity.StarlighPoolBlockEntity;

public class StarlightPoolBlockRenderer implements BlockEntityRenderer<StarlighPoolBlockEntity> {

    private static final ResourceLocation STARLIGHT_STILL =
            ResourceLocation.fromNamespaceAndPath("animewitchery", "block/starlight_still");

    private static final float MIN_X = 0.125F;
    private static final float MAX_X = 0.875F;
    private static final float MIN_Z = 0.125F;
    private static final float MAX_Z = 0.875F;
    private static final float MIN_Y = 0.795F;
    private static final float MAX_Y = 0.99F;

    public StarlightPoolBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(StarlighPoolBlockEntity pool, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        int amount = pool.fluidTank.getFluidAmount();
        if (amount <= 0) {
            return;
        }

        TextureAtlasSprite sprite = Minecraft.getInstance()
                .getTextureAtlas(net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS)
                .apply(STARLIGHT_STILL);

        int capacity = pool.fluidTank.getCapacity();
        float fillRatio = Mth.clamp((float) amount / capacity, 0.0F, 1.0F);
        float topY = MIN_Y + (MAX_Y - MIN_Y) * fillRatio;

        RenderType renderType = RenderType.translucent();
        VertexConsumer consumer = bufferSource.getBuffer(renderType);

        poseStack.pushPose();

        renderBox(consumer, poseStack, MIN_X, MIN_Y, MIN_Z, MAX_X, topY, MAX_Z, sprite);
        poseStack.popPose();
    }

    private static void renderBox(VertexConsumer consumer, PoseStack poseStack,
                                  float minX, float minY, float minZ,
                                  float maxX, float maxY, float maxZ,
                                  TextureAtlasSprite sprite) {
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        int fullbright = 0xF000F0;

        // UP - liquid surface
        addQuad(consumer, poseStack,
                minX, maxY, minZ,
                minX, maxY, maxZ,
                maxX, maxY, maxZ,
                maxX, maxY, minZ,
                u0, v0, u1, v1, fullbright, 0, 1, 0);

        // DOWN
        addQuad(consumer, poseStack,
                minX, minY, minZ,
                maxX, minY, minZ,
                maxX, minY, maxZ,
                minX, minY, maxZ,
                u0, v0, u1, v1, fullbright, 0, -1, 0);

        // NORTH (-Z)
        addQuad(consumer, poseStack,
                maxX, minY, minZ,
                minX, minY, minZ,
                minX, maxY, minZ,
                maxX, maxY, minZ,
                u0, v0, u1, v1, fullbright, 0, 0, -1);

        // SOUTH (+Z)
        addQuad(consumer, poseStack,
                minX, minY, maxZ,
                maxX, minY, maxZ,
                maxX, maxY, maxZ,
                minX, maxY, maxZ,
                u0, v0, u1, v1, fullbright, 0, 0, 1);

        // WEST (-X)
        addQuad(consumer, poseStack,
                minX, minY, minZ,
                minX, minY, maxZ,
                minX, maxY, maxZ,
                minX, maxY, minZ,
                u0, v0, u1, v1, fullbright, -1, 0, 0);

        // EAST (+X)
        addQuad(consumer, poseStack,
                maxX, minY, maxZ,
                maxX, minY, minZ,
                maxX, maxY, minZ,
                maxX, maxY, maxZ,
                u0, v0, u1, v1, fullbright, 1, 0, 0);
    }

    private static void addQuad(VertexConsumer consumer, PoseStack poseStack,
                                float x0, float y0, float z0,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float x3, float y3, float z3,
                                float u0, float v0, float u1, float v1,
                                int light, float nx, float ny, float nz) {
        var pose = poseStack.last().pose();
        consumer.addVertex(pose, x0, y0, z0).setColor(255, 255, 255, 200).setUv(u0, v0).setOverlay(0x10000).setLight(light).setNormal(nx, ny, nz);
        consumer.addVertex(pose, x1, y1, z1).setColor(255, 255, 255, 200).setUv(u0, v1).setOverlay(0x10000).setLight(light).setNormal(nx, ny, nz);
        consumer.addVertex(pose, x2, y2, z2).setColor(255, 255, 255, 200).setUv(u1, v1).setOverlay(0x10000).setLight(light).setNormal(nx, ny, nz);
        consumer.addVertex(pose, x3, y3, z3).setColor(255, 255, 255, 200).setUv(u1, v0).setOverlay(0x10000).setLight(light).setNormal(nx, ny, nz);
    }
}

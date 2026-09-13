package net.willowins.animewitchery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.block.entity.DarkFountainBlockEntity;
import org.lwjgl.opengl.GL11;

public class DarkFountainBlockEntityRenderer implements BlockEntityRenderer<DarkFountainBlockEntity> {

    private static final float BASE_HALF_WIDTH = 4.5F;

    /** How many times the 144x192 texture repeats per block of height. */
    private static final float TILES_PER_BLOCK = 1.0F / 12.0F;

    private static final float WAVE_AMPLITUDE = 0.0F;
    private static final float WAVE_FREQUENCY = 0.4F;
    private static final float WAVE_SPEED = -0.6F;

    public DarkFountainBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public boolean shouldRenderOffScreen(DarkFountainBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(DarkFountainBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        float time = (level.getGameTime() + partialTick) / 20.0F;

        int blockY = blockEntity.getBlockPos().getY();
        float y0 = 1.0F;
        float y1 = (level.getMaxBuildHeight() - blockY) + 1.0F;

        setRepeatWrap(DarkFountainRenderTypes.BEAM_LOCATION_1);

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);

        PoseStack.Pose pose = poseStack.last();
        renderPlane(bufferSource.getBuffer(DarkFountainRenderTypes.BEAM_1), pose,
                BASE_HALF_WIDTH, y0, y1, time, 0.0F);
        renderPlane(bufferSource.getBuffer(DarkFountainRenderTypes.BEAM_1), pose,
                BASE_HALF_WIDTH, y0, y1, time + 7.3F, 0.0F);

        poseStack.popPose();
    }

    private static void setRepeatWrap(ResourceLocation texture) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        RenderSystem.texParameter(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
    }

    private static void renderPlane(VertexConsumer consumer, PoseStack.Pose pose,
                                    float halfWidth, float y0, float y1, float time, float phase) {
        // One rising wave band per block; width undulates along the height so the
        // column travels upward like an energy wave.
        final float seg = 1.0F;
        float scroll = Mth.positiveModulo(-time * 0.3F, 32.0F);

        float yb = y0;
        while (yb < y1) {
            float yt = Math.min(yb + seg, y1);
            float waveB = WAVE_AMPLITUDE * Mth.sin(yb * WAVE_FREQUENCY + time * WAVE_SPEED + phase);
            float waveT = WAVE_AMPLITUDE * Mth.sin(yt * WAVE_FREQUENCY + time * WAVE_SPEED + phase);
            float vB = scroll + TILES_PER_BLOCK * (yb - y0);
            float vT = scroll + TILES_PER_BLOCK * (yt - y0);

            bandQuad(consumer, pose, halfWidth, yb, yt, waveB, waveT, vB, vT, 0);
            bandQuad(consumer, pose, halfWidth, yb, yt, waveB, waveT, vB, vT, 1);

            yb = yt;
        }
    }

    private static void bandQuad(VertexConsumer consumer, PoseStack.Pose pose,
                                 float halfWidth, float yb, float yt, float waveB, float waveT,
                                 float vB, float vT, int plane) {
        if (plane == 0) {
            // X-aligned plane (normal +Z), wobbling in Z
            vertex(consumer, pose, -halfWidth, yb, waveB, 0.0F, vB, 0.0F, 0.0F, 1.0F);
            vertex(consumer, pose, halfWidth, yb, waveB, 1.0F, vB, 0.0F, 0.0F, 1.0F);
            vertex(consumer, pose, halfWidth, yt, waveT, 1.0F, vT, 0.0F, 0.0F, 1.0F);
            vertex(consumer, pose, -halfWidth, yt, waveT, 0.0F, vT, 0.0F, 0.0F, 1.0F);
        } else {
            // Z-aligned plane (normal +X), wobbling in X
            vertex(consumer, pose, waveB, yb, -halfWidth, 0.0F, vB, 1.0F, 0.0F, 0.0F);
            vertex(consumer, pose, waveB, yb, halfWidth, 1.0F, vB, 1.0F, 0.0F, 0.0F);
            vertex(consumer, pose, waveT, yt, halfWidth, 1.0F, vT, 1.0F, 0.0F, 0.0F);
            vertex(consumer, pose, waveT, yt, -halfWidth, 0.0F, vT, 1.0F, 0.0F, 0.0F);
        }
    }

    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose,
                               float x, float y, float z, float u, float v,
                               float nx, float ny, float nz) {
        consumer.addVertex(pose.pose(), x, y, z)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setLight(LightTexture.pack(15, 15))
                .setNormal(nx, ny, nz);
    }
}
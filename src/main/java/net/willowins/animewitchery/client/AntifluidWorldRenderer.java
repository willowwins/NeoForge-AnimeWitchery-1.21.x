package net.willowins.animewitchery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.fluid.ModFluids;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class AntifluidWorldRenderer {

    private static final int RANGE = 16;
    private static boolean stencilEnabled = false;

    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        Minecraft mc = Minecraft.getInstance();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();

        if (!stencilEnabled) {
            mc.getMainRenderTarget().enableStencil();
            stencilEnabled = true;
        }

        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glStencilMask(0xFF);
        GL11.glClearStencil(0);
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT);
        GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);

        GL11.glColorMask(false, false, false, false);
        renderMaskPass(level, camPos, event.getPoseStack());
        GL11.glColorMask(true, true, true, true);

        GL11.glStencilMask(0x00);
        GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
        GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);

        renderOverlayPass(level, camPos, event.getPoseStack());

        GL11.glDisable(GL11.GL_STENCIL_TEST);
    }

    private static void renderMaskPass(ClientLevel level, Vec3 camPos, PoseStack eventPose) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bb = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        int ox = (int) camPos.x;
        int oy = (int) camPos.y;
        int oz = (int) camPos.z;

        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -RANGE; z <= RANGE; z++) {
                for (int y = -RANGE; y <= RANGE; y++) {
                    BlockPos pos = new BlockPos(ox + x, oy + y, oz + z);
                    FluidState fluidState = level.getFluidState(pos);
                    if (!isAntifluid(fluidState)) continue;
                    if (fluidState.getAmount() <= 0) continue;

                    buildFluidSurface(bb, level, pos, fluidState);
                }
            }
        }

        MeshData mesh = bb.build();
        if (mesh == null) return;

        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.identity();
        modelView.mul(eventPose.last().pose());
        modelView.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShader(() -> Minecraft.getInstance().gameRenderer.getShader("position"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferUploader.drawWithShader(mesh);

        modelView.popMatrix();
        RenderSystem.applyModelViewMatrix();
        tesselator.clear();
    }

    private static void renderOverlayPass(ClientLevel level, Vec3 camPos, PoseStack eventPose) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bb = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        int ox = (int) camPos.x;
        int oy = (int) camPos.y;
        int oz = (int) camPos.z;

        for (int x = -RANGE; x <= RANGE; x++) {
            for (int z = -RANGE; z <= RANGE; z++) {
                for (int y = -RANGE; y <= RANGE; y++) {
                    BlockPos pos = new BlockPos(ox + x, oy + y, oz + z);
                    FluidState fluidState = level.getFluidState(pos);
                    if (!isAntifluid(fluidState)) continue;
                    if (fluidState.getAmount() <= 0) continue;
                    buildFluidSurface(bb, level, pos, fluidState);
                }
            }
        }

        MeshData mesh = bb.build();
        if (mesh == null) return;

        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.identity();
        modelView.mul(eventPose.last().pose());
        modelView.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);
        RenderSystem.applyModelViewMatrix();

        RenderSystem.setShader(() -> Minecraft.getInstance().gameRenderer.getShader("rendertype_end_portal"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AntifluidShaders.END_PORTAL_LOCATION);
        RenderSystem.setShaderTexture(1, AntifluidShaders.END_SKY_LOCATION);

        BufferUploader.drawWithShader(mesh);

        modelView.popMatrix();
        RenderSystem.applyModelViewMatrix();
        tesselator.clear();
    }

    private static boolean isAntifluid(FluidState state) {
        return state.getType() == ModFluids.ANTIFLUID_SOURCE.get()
                || state.getType() == ModFluids.ANTIFLUID_FLOWING.get();
    }

    private static float getHeight(ClientLevel level, Fluid fluid, BlockPos neighborPos) {
        BlockState blockstate = level.getBlockState(neighborPos);
        FluidState neighborFluid = blockstate.getFluidState();
        if (fluid.isSame(neighborFluid.getType())) {
            BlockState aboveNeighbor = level.getBlockState(neighborPos.above());
            return fluid.isSame(aboveNeighbor.getFluidState().getType())
                    ? 1.0F
                    : neighborFluid.getOwnHeight();
        } else {
            return !blockstate.isSolid() ? 0.0F : -1.0F;
        }
    }

    private static void addWeightedHeight(float[] weighted, float height) {
        if (height >= 0.8F) {
            weighted[0] += height * 10.0F;
            weighted[1] += 10.0F;
        } else if (height >= 0.0F) {
            weighted[0] += height;
            weighted[1] += 1.0F;
        }
    }

    private static float calculateAverageHeight(ClientLevel level, Fluid fluid,
                                                 float centerHeight, float heightA, float heightB, BlockPos diagonalPos) {
        if (heightB >= 1.0F || heightA >= 1.0F) {
            return 1.0F;
        }
        float[] weighted = new float[2];
        if (heightB > 0.0F || heightA > 0.0F) {
            float diagHeight = getHeight(level, fluid, diagonalPos);
            if (diagHeight >= 1.0F) return 1.0F;
            addWeightedHeight(weighted, diagHeight);
        }
        addWeightedHeight(weighted, centerHeight);
        addWeightedHeight(weighted, heightB);
        addWeightedHeight(weighted, heightA);
        return weighted[1] > 0 ? weighted[0] / weighted[1] : 0.0F;
    }

    private static void buildFluidSurface(VertexConsumer consumer, ClientLevel level,
                                           BlockPos pos, FluidState fluidState) {
        Fluid fluid = fluidState.getType();

        float centerHeight = getHeight(level, fluid, pos);
        float cornerNE, cornerNW, cornerSE, cornerSW;

        if (centerHeight >= 1.0F) {
            cornerNE = 1.0F;
            cornerNW = 1.0F;
            cornerSE = 1.0F;
            cornerSW = 1.0F;
        } else {
            float northH = getHeight(level, fluid, pos.north());
            float southH = getHeight(level, fluid, pos.south());
            float eastH = getHeight(level, fluid, pos.east());
            float westH = getHeight(level, fluid, pos.west());
            cornerNE = calculateAverageHeight(level, fluid, centerHeight, northH, eastH, pos.north().east());
            cornerNW = calculateAverageHeight(level, fluid, centerHeight, northH, westH, pos.north().west());
            cornerSE = calculateAverageHeight(level, fluid, centerHeight, southH, eastH, pos.south().east());
            cornerSW = calculateAverageHeight(level, fluid, centerHeight, southH, westH, pos.south().west());
        }

        boolean renderNorth = !level.getBlockState(pos.north()).isSolid();
        boolean renderSouth = !level.getBlockState(pos.south()).isSolid();
        boolean renderWest = !level.getBlockState(pos.west()).isSolid();
        boolean renderEast = !level.getBlockState(pos.east()).isSolid();

        float wx = pos.getX();
        float wy = pos.getY();
        float wz = pos.getZ();

        float nW = cornerNW - 0.001F;
        float sW = cornerSW - 0.001F;
        float sE = cornerSE - 0.001F;
        float nE = cornerNE - 0.001F;

        consumer.addVertex(wx, wy + nW, wz);
        consumer.addVertex(wx, wy + sW, wz + 1.0F);
        consumer.addVertex(wx + 1.0F, wy + sE, wz + 1.0F);
        consumer.addVertex(wx + 1.0F, wy + nE, wz);

        if (renderNorth) {
            consumer.addVertex(wx, wy + cornerNW, wz + 0.001F);
            consumer.addVertex(wx + 1.0F, wy + cornerNE, wz + 0.001F);
            consumer.addVertex(wx + 1.0F, wy, wz + 0.001F);
            consumer.addVertex(wx, wy, wz + 0.001F);
        }
        if (renderSouth) {
            consumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 0.999F);
            consumer.addVertex(wx, wy + cornerSW, wz + 0.999F);
            consumer.addVertex(wx, wy, wz + 0.999F);
            consumer.addVertex(wx + 1.0F, wy, wz + 0.999F);
        }
        if (renderWest) {
            consumer.addVertex(wx + 0.001F, wy + cornerSW, wz + 1.0F);
            consumer.addVertex(wx + 0.001F, wy + cornerNW, wz);
            consumer.addVertex(wx + 0.001F, wy, wz);
            consumer.addVertex(wx + 0.001F, wy, wz + 1.0F);
        }
        if (renderEast) {
            consumer.addVertex(wx + 0.999F, wy + cornerNE, wz);
            consumer.addVertex(wx + 0.999F, wy + cornerSE, wz + 1.0F);
            consumer.addVertex(wx + 0.999F, wy, wz + 1.0F);
            consumer.addVertex(wx + 0.999F, wy, wz);
        }
    }
}

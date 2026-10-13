package net.willowins.animewitchery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.custom.StarlightPoolBlock;
import net.willowins.animewitchery.block.entity.StarlighPoolBlockEntity;
import net.willowins.animewitchery.fluid.ModFluids;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class AntifluidWorldRenderer {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("AntifluidWorldRenderer");
    private static int lastFoundPools;

    public static int[] getLastCounts() {
        return new int[]{lastFoundPools};
    }

    @SubscribeEvent
    public static void onRenderStage(RenderLevelStageEvent event) {
        RenderLevelStageEvent.Stage stage = event.getStage();
        if (stage == RenderLevelStageEvent.Stage.AFTER_SKY) {
            AntifluidTextureUsage.setLevelScope(true);
            AntifluidTextureUsage.clearCaptured();
            return;
        }
        if (stage != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            AntifluidTextureUsage.setLevelScope(false);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        Frustum frustum = event.getFrustum();

        int hrange = Math.min(Math.max(48, mc.options.getEffectiveRenderDistance() * 8), 96);
        int yrange = 32;

        renderPass(level, camPos, frustum, event.getModelViewMatrix(), hrange, yrange);
        AntifluidTextureUsage.setLevelScope(false);
    }

    private static void renderPass(ClientLevel level, Vec3 camPos, Frustum frustum,
                                   Matrix4f viewRotation, int hrange, int yrange) {
        int captured = AntifluidTextureUsage.getCapturedCount();
        AntifluidTextureUsage.clearCaptured();

        ByteBufferBuilder depthBuffer = new ByteBufferBuilder(1536);
        BufferBuilder depthBb = new BufferBuilder(depthBuffer, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        ByteBufferBuilder worldBuffer = new ByteBufferBuilder(1536);
        BufferBuilder worldBb = new BufferBuilder(worldBuffer, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        int found = buildGeometry(worldBb, depthBb, level, camPos, frustum, hrange, yrange);
        if (level.getGameTime() % 100L == 0L) {
            LOGGER.info("AF render scan: pools={} found={}", lastFoundPools, found);
        }
        MeshData depthMesh = depthBb.build();
        MeshData worldMesh = worldBb.build();

        if (worldMesh == null) return;

        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.identity();
        modelView.mul(viewRotation);
        modelView.translate((float) -camPos.x, (float) -camPos.y, (float) -camPos.z);
        RenderSystem.applyModelViewMatrix();

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enablePolygonOffset();
        RenderSystem.polygonOffset(-0.02F, -0.05F);
        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        setFrameUniforms(camPos);

        if (worldMesh != null && AntifluidShaders.antifluidPortalShader != null) {
            RenderSystem.setShader(() -> AntifluidShaders.antifluidPortalShader);
            BufferUploader.drawWithShader(worldMesh);
        }

        if (depthMesh != null && AntifluidShaders.antifluidSpaceShader != null) {
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            RenderSystem.setShader(() -> AntifluidShaders.antifluidSpaceShader);
            BufferUploader.drawWithShader(depthMesh);
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderSystem.depthMask(true);
        }

        RenderSystem.disableBlend();
        RenderSystem.disablePolygonOffset();
        RenderSystem.enableCull();

        modelView.popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    private static int buildGeometry(VertexConsumer portalConsumer, VertexConsumer spaceConsumer,
                                 ClientLevel level, Vec3 camPos,
                                 Frustum frustum, int hrange, int yrange) {
        int ox = (int) camPos.x;
        int oy = (int) camPos.y;
        int oz = (int) camPos.z;
        int found = 0;
        int foundPools = 0;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos probe = new BlockPos.MutableBlockPos();
        for (int y = -yrange; y <= yrange; y++) {
            for (int x = -hrange; x <= hrange; x++) {
                for (int z = -hrange; z <= hrange; z++) {
                    pos.set(ox + x, oy + y, oz + z);
                    if (!frustum.isVisible(new AABB(pos))) continue;

                    BlockState blockState = level.getBlockState(pos);
                    if (blockState.is(ModBlocks.POCKET_VEIL.get())
                            || blockState.is(ModBlocks.POCKET_RETURN.get())) {
                        buildVeilCube(portalConsumer, spaceConsumer, level, pos, probe);
                        found++;
                        continue;
                    }
                    if (blockState.getBlock() instanceof StarlightPoolBlock) {
                        if (level.getBlockEntity(pos) instanceof StarlighPoolBlockEntity pool) {
                            if (pool.fluidTank.getFluidAmount() > 0) {
                                Fluid stored = pool.fluidTank.getFluid().getFluid();
                                if (isAntifluid(stored)) {
                                    buildPoolSurface(portalConsumer, spaceConsumer, level, pos, pool);
                                    found++;
                                    foundPools++;
                                }
                            }
                        }
                        continue;
                    }

                    FluidState fluidState = blockState.getFluidState();
                    if (!isAntifluid(fluidState)) continue;
                    if (fluidState.getAmount() <= 0) continue;

                    buildFluidSurface(portalConsumer, spaceConsumer, level, pos, fluidState);
                    found++;
                }
            }
        }
        lastFoundPools = foundPools;
        return found;
    }

    static final long ANIM_START = System.nanoTime();

    static void setFrameUniforms(Vec3 camPos) {
        float anim = (float) ((System.nanoTime() - ANIM_START) / 1.0E9);
        if (AntifluidShaders.antifluidPortalShader != null) {
            com.mojang.blaze3d.shaders.Uniform animU = AntifluidShaders.antifluidPortalShader.getUniform("AnimTime");
            if (animU != null) {
                animU.set(anim);
            }
            com.mojang.blaze3d.shaders.Uniform cam = AntifluidShaders.antifluidPortalShader.getUniform("CamPos");
            if (cam != null) {
                cam.set((float) camPos.x, (float) camPos.y, (float) camPos.z);
            }
        }
        if (AntifluidShaders.antifluidSpaceShader != null) {
            com.mojang.blaze3d.shaders.Uniform animU = AntifluidShaders.antifluidSpaceShader.getUniform("AnimTime");
            if (animU != null) {
                animU.set(anim);
            }
            com.mojang.blaze3d.shaders.Uniform cam = AntifluidShaders.antifluidSpaceShader.getUniform("CamPos");
            if (cam != null) {
                cam.set((float) camPos.x, (float) camPos.y, (float) camPos.z);
            }
        }
    }

    private static boolean isAntifluid(FluidState state) {
        return isAntifluid(state.getType());
    }

    private static boolean isAntifluid(Fluid fluid) {
        return fluid.isSame(ModFluids.ANTIFLUID_SOURCE.get())
                || fluid.isSame(ModFluids.ANTIFLUID_FLOWING.get());
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

    private static void emitTopSurface(VertexConsumer consumer, float wx, float wy, float wz, float alpha,
                                       float cornerNW, float cornerNE, float cornerSE, float cornerSW) {
        float spread = Math.max(
                Math.max(Math.abs(cornerNW - cornerNE), Math.abs(cornerNE - cornerSE)),
                Math.max(Math.max(Math.abs(cornerSE - cornerSW), Math.abs(cornerSW - cornerNW)),
                        Math.max(Math.abs(cornerNW - cornerSE), Math.abs(cornerNE - cornerSW))));

        consumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx, wz);
        consumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx, wz + 1.0F);
        consumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx + 1.0F, wz + 1.0F);
        consumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx + 1.0F, wz);

        if (spread > 0.002F) {
            consumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx + 1.0F, wz);
            consumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx, wz);
            consumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx, wz + 1.0F);
            consumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(wx + 1.0F, wz + 1.0F);
        }
    }

    private static void buildPoolSurface(VertexConsumer consumer, VertexConsumer spaceConsumer,
                                         ClientLevel level, BlockPos pos, StarlighPoolBlockEntity pool) {
        float fill = Mth.clamp((float) pool.fluidTank.getFluidAmount() / pool.fluidTank.getCapacity(), 0.0F, 1.0F);

        float wx = pos.getX();
        float wy = pos.getY();
        float wz = pos.getZ();

        float minX = wx + 0.125F;
        float maxX = wx + 0.875F;
        float minZ = wz + 0.125F;
        float maxZ = wz + 0.875F;
        float minY = wy + 0.795F;
        float maxY = wy + 0.99F;
        float topY = Mth.lerp(fill, minY, maxY);

        float topAlpha = 1.0F;
        float sideAlpha = 0.6F;
        float bottomAlpha = 0.30F;

        emitVoidQuad(consumer, minX, topY, minZ, minX, topY, maxZ, maxX, topY, maxZ, maxX, topY, minZ, topAlpha);
        emitVoidQuad(spaceConsumer, minX, topY, minZ, minX, topY, maxZ, maxX, topY, maxZ, maxX, topY, minZ, topAlpha);
        emitVoidQuad(consumer, maxX, minY, minZ, minX, minY, minZ, minX, topY, minZ, maxX, topY, minZ, sideAlpha);
        emitVoidQuad(consumer, minX, minY, maxZ, maxX, minY, maxZ, maxX, topY, maxZ, minX, topY, maxZ, sideAlpha);
        emitVoidQuad(consumer, minX, minY, minZ, minX, minY, maxZ, minX, topY, maxZ, minX, topY, minZ, sideAlpha);
        emitVoidQuad(consumer, maxX, minY, maxZ, maxX, minY, minZ, maxX, topY, minZ, maxX, topY, maxZ, sideAlpha);
        emitVoidQuad(spaceConsumer, maxX, minY, minZ, minX, minY, minZ, minX, topY, minZ, maxX, topY, minZ, sideAlpha);
        emitVoidQuad(spaceConsumer, minX, minY, maxZ, maxX, minY, maxZ, maxX, topY, maxZ, minX, topY, maxZ, sideAlpha);
        emitVoidQuad(spaceConsumer, minX, minY, minZ, minX, minY, maxZ, minX, topY, maxZ, minX, topY, minZ, sideAlpha);
        emitVoidQuad(spaceConsumer, maxX, minY, maxZ, maxX, minY, minZ, maxX, topY, minZ, maxX, topY, maxZ, sideAlpha);
        emitVoidQuad(consumer, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, bottomAlpha);
    }

    private static void emitVoidQuad(VertexConsumer consumer,
                                     float x0, float y0, float z0,
                                     float x1, float y1, float z1,
                                     float x2, float y2, float z2,
                                     float x3, float y3, float z3, float alpha) {
        consumer.addVertex(x0, y0, z0).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(x0, z0);
        consumer.addVertex(x1, y1, z1).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(x1, z1);
        consumer.addVertex(x2, y2, z2).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(x2, z2);
        consumer.addVertex(x3, y3, z3).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(x3, z3);
    }

    private static boolean veilFaceOccluded(BlockPos.MutableBlockPos probe, ClientLevel level, BlockPos pos,
                                            int dx, int dy, int dz) {
        probe.set(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);
        BlockState neighbor = level.getBlockState(probe);
        return neighbor.is(ModBlocks.POCKET_VEIL.get()) || neighbor.is(ModBlocks.POCKET_RETURN.get());
    }

    private static void buildVeilCube(VertexConsumer consumer, VertexConsumer spaceConsumer,
                                      ClientLevel level, BlockPos pos, BlockPos.MutableBlockPos probe) {
        float wx = pos.getX();
        float wy = pos.getY();
        float wz = pos.getZ();
        float minX = wx;
        float maxX = wx + 1.0F;
        float minY = wy;
        float maxY = wy + 1.0F;
        float minZ = wz;
        float maxZ = wz + 1.0F;
        float alpha = 1.0F;

        if (!veilFaceOccluded(probe, level, pos, 0, 0, -1)) {
            emitVoidQuad(consumer, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, alpha);
            emitVoidQuad(spaceConsumer, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, alpha);
        }
        if (!veilFaceOccluded(probe, level, pos, 0, 0, 1)) {
            emitVoidQuad(consumer, maxX, minY, maxZ, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, alpha);
            emitVoidQuad(spaceConsumer, maxX, minY, maxZ, minX, minY, maxZ, minX, maxY, maxZ, maxX, maxY, maxZ, alpha);
        }
        if (!veilFaceOccluded(probe, level, pos, 0, -1, 0)) {
            emitVoidQuad(consumer, maxX, minY, minZ, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, alpha);
            emitVoidQuad(spaceConsumer, maxX, minY, minZ, minX, minY, minZ, minX, minY, maxZ, maxX, minY, maxZ, alpha);
        }
        if (!veilFaceOccluded(probe, level, pos, 0, 1, 0)) {
            emitVoidQuad(consumer, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, alpha);
            emitVoidQuad(spaceConsumer, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, alpha);
        }
        if (!veilFaceOccluded(probe, level, pos, -1, 0, 0)) {
            emitVoidQuad(consumer, minX, minY, maxZ, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, alpha);
            emitVoidQuad(spaceConsumer, minX, minY, maxZ, minX, minY, minZ, minX, maxY, minZ, minX, maxY, maxZ, alpha);
        }
        if (!veilFaceOccluded(probe, level, pos, 1, 0, 0)) {
            emitVoidQuad(consumer, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, alpha);
            emitVoidQuad(spaceConsumer, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, alpha);
        }
    }

    private static boolean occludesFace(ClientLevel level, BlockPos occluderPos, BlockState occluder,
                                        Direction face, float height) {
        if (!occluder.canOcclude()) return false;
        VoxelShape box = Shapes.box(0.0, 0.0, 0.0, 1.0, height, 1.0);
        VoxelShape occlusion = occluder.getOcclusionShape(level, occluderPos);
        return Shapes.blockOccudes(box, occlusion, face);
    }

private static void buildFluidSurface(VertexConsumer consumer, VertexConsumer spaceConsumer,
                                       ClientLevel level, BlockPos pos, FluidState fluidState) {
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

        BlockState northBlock = level.getBlockState(pos.north());
        BlockState southBlock = level.getBlockState(pos.south());
        BlockState westBlock = level.getBlockState(pos.west());
        BlockState eastBlock = level.getBlockState(pos.east());
        BlockState belowBlock = level.getBlockState(pos.below());

        boolean renderNorth = !fluid.isSame(northBlock.getFluidState().getType())
                && !occludesFace(level, pos.north(), northBlock, Direction.NORTH, Math.max(cornerNW, cornerNE));
        boolean renderSouth = !fluid.isSame(southBlock.getFluidState().getType())
                && !occludesFace(level, pos.south(), southBlock, Direction.SOUTH, Math.max(cornerSE, cornerSW));
        boolean renderWest = !fluid.isSame(westBlock.getFluidState().getType())
                && !occludesFace(level, pos.west(), westBlock, Direction.WEST, Math.max(cornerSW, cornerNW));
        boolean renderEast = !fluid.isSame(eastBlock.getFluidState().getType())
                && !occludesFace(level, pos.east(), eastBlock, Direction.EAST, Math.max(cornerNE, cornerSE));
        boolean renderTop = !fluid.isSame(level.getFluidState(pos.above()).getType());
        boolean renderBottom = !fluid.isSame(belowBlock.getFluidState().getType())
                && !occludesFace(level, pos.below(), belowBlock, Direction.DOWN, 0.8888889F);

        float wx = pos.getX();
        float wy = pos.getY();
        float wz = pos.getZ();
        float topAlpha = 1.0F;
        float sideAlpha = 0.6F;
        float bottomAlpha = 0.30F;

        if (renderTop) {
            emitTopSurface(consumer, wx, wy, wz, topAlpha, cornerNW, cornerNE, cornerSE, cornerSW);
            emitTopSurface(spaceConsumer, wx, wy, wz, topAlpha, cornerNW, cornerNE, cornerSE, cornerSW);
        }

        if (renderNorth) {
            consumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy + cornerNW);
            consumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy + cornerNE);
            consumer.addVertex(wx + 1.0F, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy);
            consumer.addVertex(wx, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy);
            spaceConsumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy + cornerNW);
            spaceConsumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy + cornerNE);
            spaceConsumer.addVertex(wx + 1.0F, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy);
            spaceConsumer.addVertex(wx, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy);
        }
        if (renderSouth) {
            consumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy + cornerSE);
            consumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy + cornerSW);
            consumer.addVertex(wx, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy);
            consumer.addVertex(wx + 1.0F, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy);
            spaceConsumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy + cornerSE);
            spaceConsumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy + cornerSW);
            spaceConsumer.addVertex(wx, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx, wy);
            spaceConsumer.addVertex(wx + 1.0F, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wx + 1.0F, wy);
        }
        if (renderWest) {
            consumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy + cornerSW);
            consumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy + cornerNW);
            consumer.addVertex(wx, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy);
            consumer.addVertex(wx, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy);
            spaceConsumer.addVertex(wx, wy + cornerSW, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy + cornerSW);
            spaceConsumer.addVertex(wx, wy + cornerNW, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy + cornerNW);
            spaceConsumer.addVertex(wx, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy);
            spaceConsumer.addVertex(wx, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy);
        }
        if (renderEast) {
            consumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy + cornerNE);
            consumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy + cornerSE);
            consumer.addVertex(wx + 1.0F, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy);
            consumer.addVertex(wx + 1.0F, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy);
            spaceConsumer.addVertex(wx + 1.0F, wy + cornerNE, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy + cornerNE);
            spaceConsumer.addVertex(wx + 1.0F, wy + cornerSE, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy + cornerSE);
            spaceConsumer.addVertex(wx + 1.0F, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz + 1.0F, wy);
            spaceConsumer.addVertex(wx + 1.0F, wy, wz).setColor(0.0F, 0.0F, 0.0F, sideAlpha).setUv(wz, wy);
        }
        if (renderBottom) {
            consumer.addVertex(wx, wy, wz).setColor(0.0F, 0.0F, 0.0F, bottomAlpha).setUv(wx, wz);
            consumer.addVertex(wx + 1.0F, wy, wz).setColor(0.0F, 0.0F, 0.0F, bottomAlpha).setUv(wx + 1.0F, wz);
            consumer.addVertex(wx + 1.0F, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, bottomAlpha).setUv(wx + 1.0F, wz + 1.0F);
            consumer.addVertex(wx, wy, wz + 1.0F).setColor(0.0F, 0.0F, 0.0F, bottomAlpha).setUv(wx, wz + 1.0F);
        }
    }
}

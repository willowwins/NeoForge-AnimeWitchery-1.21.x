package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.Vec3;
import net.willowins.animewitchery.AnimeWitchery;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Finds any draw that uses the antifluid texture and reroutes it through the void shader.
 *
 * <p>The antifluid still/flowing textures are unique to this mod. Every fluid renderer (vanilla
 * tanks, Create, Mekanism, ...) resolves those textures from {@code IClientFluidTypeExtensions}
 * and draws quads carrying the sprite's UVs on some shared block-atlas QUADS sheet with the BLOCK
 * vertex format. {@link #isTrackedFluidSheet} matches those sheets structurally (identity fails for
 * per-mod ephemeral render types); {@link #isAntifluidUv} detects quads whose UVs
 * fall inside the antifluid sprite's atlas region ("a usage of the antifluid texture").
 * Such quads are captured with {@link #captureQuad} instead of being drawn with the mod's own
 * shader, and re-rendered through the void shader by {@link #drainInto} regardless of which mod
 * produced them. Captures only happen while {@link #isLevelScope()} is true, so item/GUI draws are
 * replayed unchanged.</p>
 */
public final class AntifluidTextureUsage {

    public static final ResourceLocation STILL =
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/antifluid_still");
    public static final ResourceLocation FLOW =
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/antifluid_flow");

    private static final float EPS = 1.0F / 4096.0F;
    private static final int MAX_QUADS = 8192;

    private static final List<float[]> CAPTURED = new ArrayList<>();

    private static TextureAtlasSprite storedMissing;

    /**
     * True while the camera world pass is rendering (set by {@link AntifluidWorldRenderer} at the
     * AFTER_SKY stage, cleared at AFTER_LEVEL). Fluid quads are only captured in this window, so
     * item/GUI/held-item draws are left untouched.
     */
    private static boolean levelScope;

    private static long lastDrainLog;

    private static final Map<RenderType, long[]> SHEET_STATS = new IdentityHashMap<>();
    private static long lastStatsLog;
    private static final long STATS_INTERVAL = 2000;

    private static final java.util.Set<RenderType> WRAPPED_SHEETS = java.util.Collections.newSetFromMap(new IdentityHashMap<>());

    private static boolean spritesLogged;

    private AntifluidTextureUsage() {
    }

    public static void onWrapperCreated(RenderType sheet) {
        if (WRAPPED_SHEETS.add(sheet)) {
            AnimeWitchery.LOGGER.info("[AntifluidWitchery] wrapped sheet '{}' (mode={}, format={})",
                    sheet, sheet.mode(), sheet.format());
        }
    }

    public static boolean logAntifluidSpritesOnce() {
        TextureAtlasSprite still = sprite(STILL);
        TextureAtlasSprite flow = sprite(FLOW);
        if (still == null || flow == null) return false;
        if (spritesLogged) return true;
        spritesLogged = true;
        AnimeWitchery.LOGGER.info("[AntifluidWitchery] antifluid sprite UV bounds: still=[u{},u{} v{},v{}] flow=[u{},u{} v{},v{}]",
                still.getU0(), still.getU1(), still.getV0(), still.getV1(),
                flow.getU0(), flow.getU1(), flow.getV0(), flow.getV1());
        return true;
    }

    /**
     * Matches any block-atlas QUADS render type with the BLOCK vertex format, i.e. every fluid sheet
     * in use by vanilla tanks, Create ({@code PonderRenderTypes#fluid}), Mekanism and others. All
     * such sheets are ephemeral per-mod instances, so identity checks fail; the block atlas contains
     * the (unique) antifluid sprites, and only those UVs are ever suppressed.
     */
    public static boolean isTrackedFluidSheet(RenderType renderType) {
        if (renderType.mode() != VertexFormat.Mode.QUADS) return false;
        return renderType.format() == DefaultVertexFormat.BLOCK;
    }

    public static boolean isAntifluidUv(float u, float v) {
        return inSprite(sprite(STILL), u, v) || inSprite(sprite(FLOW), u, v);
    }

    public static void setLevelScope(boolean inLevel) {
        levelScope = inLevel;
    }

    public static boolean isLevelScope() {
        return levelScope;
    }

    public static void captureQuad(float[][] positions) {
        if (CAPTURED.size() >= MAX_QUADS) return;
        float[] quad = new float[12];
        for (int i = 0; i < 4; i++) {
            quad[i * 3] = positions[i][0];
            quad[i * 3 + 1] = positions[i][1];
            quad[i * 3 + 2] = positions[i][2];
        }
        CAPTURED.add(quad);
    }

    public static void noteQuad(RenderType sheet, boolean antifluid, boolean captured) {
        long[] s = SHEET_STATS.computeIfAbsent(sheet, k -> new long[3]);
        s[0]++;
        if (antifluid) s[1]++;
        if (captured) s[2]++;
        long now = System.currentTimeMillis();
        if (now - lastStatsLog >= STATS_INTERVAL) {
            lastStatsLog = now;
            AnimeWitchery.LOGGER.info("[AntifluidWitchery] per-sheet quad stats (2s window):");
            for (Map.Entry<RenderType, long[]> e : SHEET_STATS.entrySet()) {
                long[] v = e.getValue();
                if (v[0] > 0) {
                    AnimeWitchery.LOGGER.info("  sheet '{}': {} quads, {} antifluid-matched, {} captured",
                            e.getKey(), v[0], v[1], v[2]);
                }
            }
            SHEET_STATS.clear();
        }
    }

    public static void drainInto(VertexConsumer consumer, Matrix4f invViewRot, Vec3 camPos) {
        Vector3f p = new Vector3f();
        float[] firstWorld = null;
        for (float[] quad : CAPTURED) {
            float cx = (float) camPos.x;
            float cy = (float) camPos.y;
            float cz = (float) camPos.z;

            float[] world = new float[12];
            for (int i = 0; i < 4; i++) {
                p.set(quad[i * 3], quad[i * 3 + 1], quad[i * 3 + 2]);
                invViewRot.transformPosition(p);
                world[i * 3] = p.x + cx;
                world[i * 3 + 1] = p.y + cy;
                world[i * 3 + 2] = p.z + cz;
            }
            if (firstWorld == null) firstWorld = world;

            float alpha = alphaForQuad(world);

            consumer.addVertex(world[0], world[1], world[2]).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(world[0], world[2]);
            consumer.addVertex(world[3], world[4], world[5]).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(world[3], world[5]);
            consumer.addVertex(world[6], world[7], world[8]).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(world[6], world[8]);
            consumer.addVertex(world[9], world[10], world[11]).setColor(0.0F, 0.0F, 0.0F, alpha).setUv(world[9], world[11]);
        }
        logDrain(CAPTURED.size(), firstWorld);
        CAPTURED.clear();
    }

    public static void clearCaptured() {
        CAPTURED.clear();
    }

    public static int getCapturedCount() {
        return CAPTURED.size();
    }

    private static void logDrain(int count, float[] firstWorld) {
        long now = System.currentTimeMillis();
        if (now - lastDrainLog < 2000) return;
        lastDrainLog = now;
        if (count == 0 || firstWorld == null) return;
        float minx = min(firstWorld[0], firstWorld[3], firstWorld[6], firstWorld[9]);
        float maxx = max(firstWorld[0], firstWorld[3], firstWorld[6], firstWorld[9]);
        float miny = min(firstWorld[1], firstWorld[4], firstWorld[7], firstWorld[10]);
        float maxy = max(firstWorld[1], firstWorld[4], firstWorld[7], firstWorld[10]);
        float minz = min(firstWorld[2], firstWorld[5], firstWorld[8], firstWorld[11]);
        float maxz = max(firstWorld[2], firstWorld[5], firstWorld[8], firstWorld[11]);
        AnimeWitchery.LOGGER.info("[AntifluidWitchery] drained {} captured antifluid quads; first quad world bbox: "
                + "x[{},{}] y[{},{}] z[{},{}]", count, minx, maxx, miny, maxy, minz, maxz);
    }

    private static float min(float a, float b, float c, float d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    private static float max(float a, float b, float c, float d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    private static float alphaForQuad(float[] w) {
        float ax = w[3] - w[0];
        float ay = w[4] - w[1];
        float az = w[5] - w[2];
        float bx = w[6] - w[0];
        float by = w[7] - w[1];
        float bz = w[8] - w[2];
        float nx = ay * bz - az * by;
        float ny = az * bx - ax * bz;
        float nz = ax * by - ay * bx;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len < 1.0E-6F) return 0.18F;
        ny /= len;
        if (ny > 0.5F) return 1.0F;
        if (ny < -0.5F) return 0.30F;
        return 0.18F;
    }

    private static boolean inSprite(TextureAtlasSprite sprite, float u, float v) {
        if (sprite == null || isMissing(sprite)) return false;
        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();
        return u >= u0 - EPS && u <= u1 + EPS && v >= v0 - EPS && v <= v1 + EPS;
    }

    private static TextureAtlasSprite sprite(ResourceLocation location) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getModelManager() == null) return null;
        return mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    }

    private static boolean isMissing(TextureAtlasSprite sprite) {
        if (storedMissing == null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null || mc.getModelManager() == null) return sprite == null;
            storedMissing = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                    .apply(ResourceLocation.withDefaultNamespace("missingno"));
        }
        if (sprite == storedMissing) return true;
        return false;
    }
}
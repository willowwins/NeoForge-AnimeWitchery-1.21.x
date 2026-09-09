package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import org.joml.Vector3f;

/**
 * Wraps a {@link VertexConsumer} and holds back whole quads. When a full quad's UVs match the
 * antifluid texture sprite it is withheld from the delegate (the mod's own fluid draw) and
 * captured for re-rendering through the void shader; every other quad is replayed to the delegate
 * unchanged. Position/normal data are recorded after the caller's pose has been applied.
 *
 * <p>The 1.21.1 chained consumer API has no {@code endVertex()}: vertices are finalised lazily by
 * the next {@link #addVertex(float, float, float)} or handed off on the last BLOCK element,
 * {@link #setNormal(float, float, float)}. We mirror that; every abstract method of the interface
 * funnels through the six implemented here (the pose/colour/overlay/light convenience overloads
 * are interface defaults that end up calling these).</p>
 */
public class AntifluidVertexConsumer implements VertexConsumer {

    private static final int QUAD = 4;

    private final VertexConsumer delegate;
    private final RenderType sheet;

    private final float[][] positions = new float[QUAD][3];
    private final float[][] uvs = new float[QUAD][2];
    private final int[][] colors = new int[QUAD][4];
    private final int[][] overlays = new int[QUAD][2];
    private final int[][] lights = new int[QUAD][2];
    private final float[][] normals = new float[QUAD][3];
    private final boolean[] hasColor = new boolean[QUAD];
    private final boolean[] hasUv = new boolean[QUAD];
    private final boolean[] hasOverlay = new boolean[QUAD];
    private final boolean[] hasLight = new boolean[QUAD];
    private final boolean[] hasNormal = new boolean[QUAD];

    private final Vector3f curPos = new Vector3f();
    private int curR = 255;
    private int curG = 255;
    private int curB = 255;
    private int curA = 255;
    private float curU;
    private float curV;
    private int curOverlayU;
    private int curOverlayV;
    private int curLightU;
    private int curLightV;
    private float curNx;
    private float curNy = 1.0F;
    private float curNz;
    private boolean curStarted;
    private boolean curColorSet;
    private boolean curUvSet;
    private boolean curOverlaySet;
    private boolean curLightSet;
    private boolean curNormalSet;

    private int index;

    public AntifluidVertexConsumer(VertexConsumer delegate, RenderType sheet) {
        this.delegate = delegate;
        this.sheet = sheet;
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        if (curStarted) {
            commit();
        }
        curPos.set(x, y, z);
        curStarted = true;
        return this;
    }

    @Override
    public VertexConsumer setColor(int r, int g, int b, int a) {
        curR = r;
        curG = g;
        curB = b;
        curA = a;
        curColorSet = true;
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        curU = u;
        curV = v;
        curUvSet = true;
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        curOverlayU = u;
        curOverlayV = v;
        curOverlaySet = true;
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        curLightU = u;
        curLightV = v;
        curLightSet = true;
        return this;
    }

    @Override
    public VertexConsumer setNormal(float x, float y, float z) {
        curNx = x;
        curNy = y;
        curNz = z;
        commit();
        return this;
    }

    private void commit() {
        if (!curStarted) {
            return;
        }
        positions[index][0] = curPos.x;
        positions[index][1] = curPos.y;
        positions[index][2] = curPos.z;
        uvs[index][0] = curU;
        uvs[index][1] = curV;
        colors[index][0] = curR;
        colors[index][1] = curG;
        colors[index][2] = curB;
        colors[index][3] = curA;
        overlays[index][0] = curOverlayU;
        overlays[index][1] = curOverlayV;
        lights[index][0] = curLightU;
        lights[index][1] = curLightV;
        normals[index][0] = curNx;
        normals[index][1] = curNy;
        normals[index][2] = curNz;
        hasColor[index] = curColorSet;
        hasUv[index] = curUvSet;
        hasOverlay[index] = curOverlaySet;
        hasLight[index] = curLightSet;
        hasNormal[index] = curNormalSet;
        resetCurrent();

        index++;
        if (index == QUAD) {
            index = 0;
            boolean antifluid = usesAntifluidTexture();
            boolean captured = antifluid && AntifluidTextureUsage.isLevelScope();
            if (captured) {
                AntifluidTextureUsage.captureQuad(positions);
            } else {
                replay();
            }
            AntifluidTextureUsage.noteQuad(sheet, antifluid, captured);
        }
    }

    private void resetCurrent() {
        curStarted = false;
        curColorSet = false;
        curUvSet = false;
        curOverlaySet = false;
        curLightSet = false;
        curNormalSet = false;
    }

    private boolean usesAntifluidTexture() {
        for (int i = 0; i < QUAD; i++) {
            if (!hasUv[i] || !AntifluidTextureUsage.isAntifluidUv(uvs[i][0], uvs[i][1])) return false;
        }
        return true;
    }

    private void replay() {
        for (int i = 0; i < QUAD; i++) {
            int r = hasColor[i] ? colors[i][0] : 255;
            int g = hasColor[i] ? colors[i][1] : 255;
            int b = hasColor[i] ? colors[i][2] : 255;
            int a = hasColor[i] ? colors[i][3] : 255;
            float u = hasUv[i] ? uvs[i][0] : 0.0F;
            float v = hasUv[i] ? uvs[i][1] : 0.0F;
            int overlay = hasOverlay[i] ? (overlays[i][1] << 16) | (overlays[i][0] & 0xFFFF) : 0;
            int light = hasLight[i] ? (lights[i][1] << 16) | (lights[i][0] & 0xFFFF) : 0;
            float nx = hasNormal[i] ? normals[i][0] : 0.0F;
            float ny = hasNormal[i] ? normals[i][1] : 1.0F;
            float nz = hasNormal[i] ? normals[i][2] : 0.0F;
            delegate.addVertex(positions[i][0], positions[i][1], positions[i][2],
                    FastColor.ARGB32.color(a, r, g, b), u, v, overlay, light, nx, ny, nz);
        }
    }
}
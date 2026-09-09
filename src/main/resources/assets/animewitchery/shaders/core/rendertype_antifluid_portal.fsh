#version 150

uniform float GameTime;

in vec2 texCoord0;
in vec4 col0;
in vec3 vWorldPos;
out vec4 fragColor;

float hash13(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

vec3 hash33(vec3 p) {
    vec3 q = fract(p * vec3(123.34, 456.21, 345.45));
    q += dot(q, q + 45.32);
    return fract((q.xxy + q.yxx) * q.zyx);
}

vec3 rotateAround(vec3 v, vec3 k, float ang) {
    float c = cos(ang);
    float s = sin(ang);
    vec3 kxv = cross(k, v);
    return v * c + kxv * s + k * (dot(k, v) * (1.0 - c));
}

float starField(vec3 rd, vec3 grad, vec3 p, float tGame, float radius,
                float periodBase, float fadeIn, float fadeOutStart, float fadeOutLen) {
    float acc = 0.0;
    for (int i = 0; i < 27; i++) {
        int ix = i % 3 - 1;
        int iy = (i / 3) % 3 - 1;
        int iz = i / 9 - 1;
        vec3 c = grad + vec3(ix, iy, iz);
        vec3 sp = c + hash33(c);
        vec3 delta = sp - p;
        float t = dot(delta, rd);
        if (t < 0.0) continue;
        float d = length(cross(rd, delta));
        float r = radius * (1.0 - 0.40 * clamp(t * 0.55, 0.0, 1.0));
        if (d >= r) continue;

        float ph = hash13(c + 91.7);
        float period = periodBase + ph * 5.0;
        float life = fract(tGame / period + ph);
        float env = smoothstep(0.0, fadeIn, life) * (1.0 - smoothstep(fadeOutStart, fadeOutStart + fadeOutLen, life));
        env *= env;
        float b = env * (1.0 - d / r);
        b *= 1.0 - 0.55 * clamp(t * 0.55, 0.0, 1.0);
        acc += b;
    }
    return acc;
}

float galaxyBoost(vec3 rd, float tGame, float gscale) {
    vec3 base = floor(rd * gscale);

    float best = 1e9;
    vec3 bestDir = rd;
    vec3 bestCell = vec3(0.0);
    float bestSize = 0.3;
    float bestType = 0.5;
    for (int i = 0; i < 27; i++) {
        int ix = i % 3 - 1;
        int iy = (i / 3) % 3 - 1;
        int iz = i / 9 - 1;
        vec3 c = base + vec3(ix, iy, iz);
        vec3 gsp = c + hash33(c);
        vec3 dirr = normalize(gsp);
        float d = length(rd - dirr);
        if (d < best) {
            best = d;
            bestDir = dirr;
            bestCell = c;
            bestSize = 0.16 + hash13(c + 5.1) * 0.30;
            bestType = hash13(c + 9.7);
        }
    }

    float phase = hash13(bestCell + 33.1) * 6.2831;
    float d = best;
    float size = bestSize;
    float boost = 1.0;

    vec3 refA = vec3(0.0, 1.0, 0.0);
    if (abs(dot(bestDir, refA)) > 0.9) refA = vec3(1.0, 0.0, 0.0);
    vec3 axisA = normalize(cross(bestDir, refA));
    vec3 refB = vec3(0.0, 0.0, 1.0);
    if (abs(dot(bestDir, refB)) > 0.9) refB = vec3(1.0, 0.0, 0.0);
    vec3 axisB = normalize(cross(bestDir, refB));

    float r = d / size;
    float ang = atan(dot(rd, axisB), dot(rd, axisA));

    if (bestType < 0.20) {                  // elliptical
        float g = smoothstep(size, 0.0, d);
        boost = 1.0 + 2.8 * pow(g, 1.4) * (0.85 + 0.15 * sin(tGame * 0.25 + phase));
    } else if (bestType < 0.42) {           // spiral
        float g = smoothstep(size, 0.0, d);
        float winding = 2.6 * r;
        float arm = pow(max(0.0, cos(2.0 * ang - winding + tGame * 0.12)), 5.0);
        arm *= smoothstep(size * 0.15, size * 0.9, d);
        float nuc = pow(smoothstep(size * 0.35, size * 0.08, d), 1.5);
        float dk = smoothstep(size * 0.35, 0.0, d) * (0.30 - 0.30 * arm);
        boost = 1.0 + 2.8 * nuc + 2.4 * arm - dk;
    } else if (bestType < 0.64) {           // barred spiral
        float g = smoothstep(size, 0.0, d);
        float bar = pow(max(0.0, cos(ang + tGame * 0.05)), 30.0) * smoothstep(size * 0.5, size * 0.05, d);
        float winding = 2.4 * r;
        float arm = pow(max(0.0, cos(2.0 * ang - winding + tGame * 0.10)), 5.0) * smoothstep(size * 0.12, size * 0.95, d);
        float nuc = smoothstep(size * 0.30, size * 0.06, d);
        boost = 1.0 + 2.6 * nuc + 1.8 * bar + 2.0 * arm - 0.25 * g * (1.0 - arm);
    } else if (bestType < 0.84) {           // irregular
        float g = smoothstep(size, 0.0, d);
        float s1 = sin(5.0 * r * 6.2831 - tGame * 0.10 + phase * 2.0);
        float s2 = sin(9.0 * r * 6.2831 + tGame * 0.06 + phase * 5.0);
        float lump = 0.5 + 0.5 * clamp(s1 * s2 * 1.2, -1.0, 1.0);
        float seg = hash13(bestCell * 13.7 + floor(r * 6.0) * 3.3);
        boost = 1.0 + 1.8 * g * lump * seg;
    } else {                                // globular cluster
        float g = smoothstep(size, 0.0, d);
        boost = 1.0 + 3.4 * pow(g, 1.8) * (0.90 + 0.10 * sin(tGame * 0.3 + phase));
    }

    return boost;
}

void main() {
    float tGame = GameTime / 20.0;
    float exposure = 0.62 + 0.38 * col0.a;

    vec3 rd = normalize(vWorldPos);
    rd = rotateAround(rd, vec3(0.0, 1.0, 0.0), tGame * 0.004);

    float gb = galaxyBoost(rd, tGame, 3.0);

    vec3 col = vec3(0.005, 0.0025, 0.018);
    col *= 0.6 + 0.4 * gb;

    vec3 p = rd * 24.0;
    vec3 grad = floor(p);

    float dust = starField(rd, grad, p, tGame, 0.090, 2.0, 0.10, 0.50, 0.15);
    float bright = starField(rd, grad, p, tGame, 0.140, 3.0, 0.12, 0.45, 0.15);
    float spec = hash13(floor(p * 2.0) + 41.0);

    col += vec3(0.70, 0.35, 1.00) * dust * 0.34 * exposure * gb;
    col += vec3(0.90, 0.50, 1.00) * bright * 0.26 * exposure * gb;
    col += vec3(0.45, 0.25, 0.85) * spec * 0.045 * exposure;

    fragColor = vec4(col, 1.0);
}
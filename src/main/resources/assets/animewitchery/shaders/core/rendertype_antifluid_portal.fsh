#version 150

uniform float GameTime;
uniform float AnimTime;
uniform vec3 CamPos;

in vec2 texCoord0;
in vec4 col0;
in vec3 vWorldPos;
out vec4 fragColor;

float hash13(vec3 p) {
    p = fract(p * 0.1031);
    p += dot(p, p.zyx + 31.32);
    return fract((p.x + p.y) * p.z);
}

// Realistic purple starmap sampled in 3D directional space, so the sky is seamless
// (no azimuth wrapping, no seam). Returns accumulated RGB.
vec3 starDome(vec3 S, float tGame, float threshold) {
    const float scale = 30.0;
    vec3 p = S * scale;
    vec3 grad = floor(p);
    vec3 acc = vec3(0.0);
    for (int i = 0; i < 27; i++) {
        int ix = i % 3 - 1;
        int iy = (i / 3) % 3 - 1;
        int iz = i / 9 - 1;
        vec3 c = grad + vec3(float(ix), float(iy), float(iz));
        float keep = hash13(c * 0.73 + 7.7);
        if (keep > threshold) continue;

        float mag = hash13(c * 1.31 + 31.7);
        float bright = pow(mag, 3.0) * 1.5 + 0.10;
        float hue = hash13(c * 2.17 + 55.9);
        float cls = hash13(c * 3.59 + 91.7);

        vec3 off = vec3(
            hash13(c + 0.31),
            hash13(c + 1.57),
            hash13(c + 2.79));
        vec3 center = c + (off - 0.5);
        vec3 dc = p - center;
        float r2 = dot(dc, dc);
        if (r2 > 0.36) continue;

        float bph = hash13(c * 5.0 + 173.9) * 6.28318;
        float bper = 2.6 + hash13(c * 7.0 + 571.1) * 6.0;
        float tw = 0.5 + 0.5 * sin(6.28318 * (tGame / bper) + bph);
        float flick = 0.55 + 0.45 * tw * tw;

        float core = exp(-r2 * 520.0);
        float glow = core * 2.6 * bright * flick;

        vec3 starCol = mix(vec3(0.92, 0.86, 1.00), vec3(0.62, 0.35, 1.05), hue);
        starCol = mix(starCol, vec3(0.75, 0.50, 1.00), 0.25 * cls);
        acc += starCol * glow;
    }
    return acc;
}

void main() {
    float tGame = AnimTime;

    // Vanilla celestial frame: stars are fixed in the world and the whole dome rotates
    // slowly over the day like the real sky.
    float dayFrac = fract((GameTime - 6000.0) / 24000.0);
    float starFade = clamp(1.0 - (cos(dayFrac * 6.2831853) * 2.0 + 0.25), 0.0, 1.0);
    starFade = starFade * starFade * 0.5;

    vec3 W = normalize(vWorldPos - CamPos);
    float r = dayFrac * 6.2831853;
    float yA = W.y * cos(r) + W.z * sin(r);
    float zA = -W.y * sin(r) + W.z * cos(r);
    vec3 S = vec3(zA, yA, -W.x);

    vec3 stars = starDome(S, tGame, 0.90) * (0.55 + starFade * 1.9);

    vec3 col = vec3(0.012, 0.010, 0.042);
    col += vec3(0.02, 0.012, 0.05) * smoothstep(-0.15, 0.85, S.y) * 0.5;
    col += stars * 1.05;

    col = clamp(col, 0.0, 1.0);
    fragColor = vec4(col, 1.0);
}
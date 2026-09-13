#version 150

uniform float AnimTime;
uniform float GameTime;
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

vec3 hash33(vec3 p) {
    return vec3(
        hash13(p + 0.31),
        hash13(p + 1.57),
        hash13(p + 2.79));
}

void main() {
    float tGame = AnimTime;

    vec3 C = CamPos;
    vec3 dir = normalize(vWorldPos - C);
    float camDist = length(vWorldPos - C);

    float tTop = (60.888889 - C.y) / dir.y;

    float tNear = 0.005;
    float tFar = 3.5;
    if (dir.y < -0.02) {
        tNear = max(0.005, tTop + 0.005);
    } else if (abs(dir.y) >= 0.02) {
        if (tTop > 0.01 && tTop < camDist + 0.1) discard;
    }
    if (tNear >= tFar) discard;

    float dt = 0.10;
    vec3 acc = vec3(0.0);
    float T = 1.0;
    float starBase = fract((GameTime - 6000.0) / 24000.0);
    float starBrightness = clamp(1.0 - (cos(starBase * 6.2831853) * 2.0 + 0.25), 0.0, 1.0);
    starBrightness = starBrightness * starBrightness * 0.5;

    for (float t = tNear; t < tFar && T > 0.02; t += dt) {
        vec3 p = C + dir * t;
        vec3 sp = p * 3.0;
        vec3 cell = floor(sp);

        for (int i = 0; i < 27; i++) {
            int ix = i % 3 - 1;
            int iy = (i / 3) % 3 - 1;
            int iz = i / 9 - 1;
            vec3 c = cell + vec3(float(ix), float(iy), float(iz));

            float h1 = hash13(c * 3.1 + 7.7);
            if (h1 < 0.88) {
                vec3 off = hash33(c);
                vec3 center = c + (off - 0.5);
                vec3 dd = sp - center;
                float d2 = dot(dd, dd);
                if (d2 < 0.10) {
                    float mag = pow(h1 / 0.88, 3.0) * 1.4 + 0.12;
                    float bph = hash13(c * 5.0 + 173.9) * 6.28318;
                    float bper = 2.6 + hash13(c * 7.0 + 571.1) * 6.0;
                    float tw = 0.5 + 0.5 * sin(6.28318 * (tGame / bper) + bph);
                    float flick = 0.55 + 0.45 * tw * tw;
                    float core = exp(-d2 * 520.0);
                    vec3 starCol = mix(vec3(0.92, 0.86, 1.0), vec3(0.62, 0.35, 1.05), hash13(c * 2.17 + 55.9));
                    acc += starCol * core * mag * flick * T * dt * (0.5 + starBrightness * 1.9);
                }
            }

            float h2 = hash13(c * 3.77 + 457.1);
            if (h2 > 0.70) {
                vec3 off2 = hash33(c + 99.0);
                vec3 center2 = c + (off2 - 0.5);
                vec3 dd2 = sp - center2;
                float d2b = dot(dd2, dd2);
                float dens = exp(-d2b * 240.0) * 0.22;
                if (dens > 0.001) {
                    vec3 dustCol = vec3(0.55, 0.40, 0.82);
                    acc += dustCol * dens * T * dt;
                    T *= exp(-dens * 0.3 * dt);
                }
            }
        }
    }

    fragColor = vec4(acc, 1.0);
}

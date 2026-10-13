#version 150

in vec2 texCoord;
out vec4 fragColor;

uniform sampler2D DiffuseSampler;
uniform sampler2D DiffuseDepthSampler;
uniform float uTime;
uniform float uVoidPhase;
uniform vec2 uTexelSize;
uniform vec3 uPlayerPos;
uniform vec3 uCameraPos;

// Fallback values when uniforms aren't available
float getTime() {
    return uTime > 0.0 ? uTime : 0.0;
}

float getVoidPhase() {
    return uVoidPhase > 0.0 ? uVoidPhase : 0.0;
}

vec2 getTexelSize() {
    return uTexelSize.x > 0.0 ? uTexelSize : vec2(1.0/1920.0, 1.0/1080.0);
}

float linearize(float depth) {
    float n = 0.05;
    float f = 200.0;
    return (2.0 * n) / (f + n - depth * (f - n));
}

float noise(vec2 p) {
    return fract(sin(dot(p, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    // Sample depth at current pixel
    float depth = texture(DiffuseDepthSampler, texCoord).r;
    float linearDepth = linearize(depth);
    
    // Use built-in derivatives for edge detection
    float ddx = dFdx(linearDepth);
    float ddy = dFdy(linearDepth);
    
    // Compute edge strength from derivatives
    float edgeStrength = sqrt(ddx * ddx + ddy * ddy);
    
    // Adaptive threshold based on depth
    float threshold = mix(0.002, 0.0001, linearDepth);
    
    // Smooth edge detection
    float edgeFactor = smoothstep(threshold, threshold * 3.0, edgeStrength);
    
    // Add animated noise overlay
    float noiseValue = noise(texCoord * 50.0 + getTime() * 0.5);
    float animatedEdge = edgeFactor * (0.9 + 0.3 * noiseValue);
    
    // Boost nearby edges - closer objects get stronger void effects
    float nearbyBoost = 1.0 - linearDepth;
    animatedEdge *= (1.0 + nearbyBoost * 0.5);

    // Create pulsing void effect
    float pulse = 0.5 + 0.5 * sin(getTime() * 0.1);
    
    // Enhanced void colors with multiple layers
    vec3 voidColor = vec3(0.6, 0.0, 0.8); // Base purple
    vec3 voidAccent = vec3(0.9, 0.2, 1.0); // Bright accent
    vec3 voidDark = vec3(0.2, 0.0, 0.4); // Dark shadow
    
    // Mix colors based on edge strength and animation
    vec3 finalColor = mix(voidDark, voidColor, animatedEdge);
    finalColor = mix(finalColor, voidAccent, animatedEdge * pulse * 0.5);
    
    // Add subtle glow effect
    float glow = smoothstep(0.0, 0.1, animatedEdge);
    finalColor += voidAccent * glow * 0.3;
    
    // Void particles - floating energy orbs
    float particles = 0.0;
    
    // Screen-space particles
    for (int i = 0; i < 2; i++) {
        vec2 particlePos = vec2(
            sin(getTime() * 0.05 + float(i) * 2.39) * 0.4 + 0.5,
            cos(getTime() * 0.03 + float(i) * 1.73) * 0.3 + 0.5
        );
        
        particlePos.y += sin(getTime() * 0.02 + float(i) * 3.14) * 0.1;
        
        float dist = length(texCoord - particlePos);
        float particle = smoothstep(0.02, 0.0, dist);
        
        float particlePulse = sin(getTime() * 0.8 + float(i) * 2.0) * 0.3 + 0.7;
        particle *= particlePulse;
        
        particles += particle * 1.2;
    }
    
    // World-space particles
    for (int i = 0; i < 3; i++) {
        vec2 worldParticlePos = vec2(
            sin(getTime() * 0.03 + float(i) * 1.57) * 0.6 + 0.5,
            cos(getTime() * 0.04 + float(i) * 2.86) * 0.4 + 0.5
        );
        
        worldParticlePos.y += sin(getTime() * 0.015 + float(i) * 3.14) * 0.15;
        
        float worldDist = length(texCoord - worldParticlePos);
        float worldParticle = smoothstep(0.015, 0.0, worldDist);
        
        float worldPulse = sin(getTime() * 0.6 + float(i) * 1.5) * 0.4 + 0.6;
        worldParticle *= worldPulse;
        
        float targetDepth = 0.3 + float(i) * 0.2;
        float depthFade = 1.0 - smoothstep(targetDepth - 0.1, targetDepth + 0.1, linearDepth);
        
        particles += worldParticle * depthFade * 1.8;
    }
    
    // Add particles to the void effect
    finalColor += voidAccent * particles * getVoidPhase();
    
    // Sample the original screen
    vec3 originalScreen = texture(DiffuseSampler, texCoord).rgb;
    
    // Gradually fade out the normal Minecraft world as void phase increases
    vec3 fadedWorld = mix(originalScreen, vec3(0.0, 0.0, 0.0), getVoidPhase());
    
    // Only show the void edges when void phase is active
    vec3 finalOutput = mix(fadedWorld, finalColor, animatedEdge * getVoidPhase());
    
    // Add particles to the entire screen
    finalOutput += voidAccent * particles * getVoidPhase();
    
    fragColor = vec4(finalOutput, 1.0);
}

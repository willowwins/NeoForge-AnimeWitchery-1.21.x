package net.willowins.animewitchery.util;

import net.minecraft.util.Mth;

public final class VoidPhaseUtil {
    private VoidPhaseUtil() {
    }

    public static final float RISE_TIME_T = 40f;
    public static final float HOLD_MAX_T  = 200f;
    public static final float FALL_TIME_T = 40f;
    public static final float HOLD_MIN_T  = 40f;

    public static float amplifierScale(int amplifier) {
        return (float) Math.pow(1.10, amplifier);
    }

    public static float ease(float x) {
        x = Mth.clamp(x, 0f, 1f);
        return x * x * (3f - 2f * x);
    }

    public static float computePhase(long ageTicks, int amplifier) {
        float scale = amplifierScale(amplifier);
        float rise  = RISE_TIME_T * scale;
        float hold1 = HOLD_MAX_T  * scale;
        float fall  = FALL_TIME_T * scale;
        float hold0 = HOLD_MIN_T  * scale;

        float cycle = rise + hold1 + fall + hold0;
        float t = (ageTicks % (long) cycle);

        if (t < rise) {
            return ease(t / rise);
        }
        t -= rise;

        if (t < hold1) {
            return 1f;
        }
        t -= hold1;

        if (t < fall) {
            return 1f - ease(t / fall);
        }
        return 0f;
    }
}
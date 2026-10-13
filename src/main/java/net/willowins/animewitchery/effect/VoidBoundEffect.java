package net.willowins.animewitchery.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.willowins.animewitchery.util.VoidPhaseUtil;

public class VoidBoundEffect extends MobEffect {
    public VoidBoundEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x000000);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            float voidPhase = VoidPhaseUtil.computePhase(player.tickCount, amplifier);

            if (voidPhase > 0.99f) {
                player.setInvisible(true);
                player.setInvulnerable(true);
                if (player.level() != null && player.level().isClientSide) {
                    spawnVoidParticles(player);
                }
            } else {
                player.setInvisible(false);
                player.setInvulnerable(false);
            }
        }
        return true;
    }

    private void spawnVoidParticles(Player player) {
        if (player.level() != null && player.level().isClientSide) {
            for (int i = 0; i < 3; i++) {
                double offsetX = (player.level().random.nextDouble() - 0.5) * 0.8;
                double offsetY = player.level().random.nextDouble() * 0.6;
                double offsetZ = (player.level().random.nextDouble() - 0.5) * 0.8;

                player.level().addParticle(
                        ParticleTypes.END_ROD,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        0.0, 0.02, 0.0);
            }
        }
    }

    public static boolean canInteractWithWorld(Player player) {
        if (player.hasEffect(ModEffects.VOID_BOUND)) {
            MobEffectInstance effect = player.getEffect(ModEffects.VOID_BOUND);
            float voidPhase = VoidPhaseUtil.computePhase(player.tickCount, effect.getAmplifier());
            return voidPhase <= 0.99f;
        }
        return true;
    }
}
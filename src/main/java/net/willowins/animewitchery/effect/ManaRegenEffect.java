package net.willowins.animewitchery.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.willowins.animewitchery.mana.PlayerMana;

public class ManaRegenEffect extends MobEffect {
    public ManaRegenEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x0066ff);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            int amount = (int) (10 * Math.pow(4, amplifier));
            PlayerMana.regen(player, amount);
        }
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
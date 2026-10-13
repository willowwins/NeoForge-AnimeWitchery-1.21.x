package net.willowins.animewitchery.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.willowins.animewitchery.effect.ModEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class PacifismMixin {

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void handlePacifismDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.is(DamageTypes.THORNS)) {
            return;
        }

        Entity attacker = source.getEntity();

        if (attacker instanceof Player player) {
            if (player.hasEffect(ModEffects.PACIFISM)) {
                LivingEntity target = (LivingEntity) (Object) this;

                if (target instanceof Player || target instanceof Animal || target instanceof Villager) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}

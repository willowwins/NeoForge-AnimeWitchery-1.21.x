package net.willowins.animewitchery.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinGoldTrimMixin {

    @Inject(method = "isWearingGold", at = @At("HEAD"), cancellable = true)
    private static void checkGoldTrim(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack armorStack : entity.getArmorAndBodyArmorSlots()) {
            if (armorStack.getItem() instanceof ArmorItem) {
                ArmorTrim trim = armorStack.get(DataComponents.TRIM);
                if (trim != null && trim.material().is(TrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}

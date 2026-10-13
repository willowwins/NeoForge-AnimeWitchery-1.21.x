package net.willowins.animewitchery.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.willowins.animewitchery.item.ModToolMaterial;

/** Haloic sword effect ported from the Fabric edition. */
public class HaloicSwordItem extends SwordItem {
    public HaloicSwordItem(Properties properties) {
        super(ModToolMaterial.HALOIC, properties.attributes(
                SwordItem.createAttributes(ModToolMaterial.HALOIC, 3, -2.4F)));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.getType().is(net.minecraft.tags.EntityTypeTags.UNDEAD)) {
            target.hurt(attacker.damageSources().magic(), 10.0F);
            target.setRemainingFireTicks(Math.max(target.getRemainingFireTicks(), 100));
        }
        return super.hurtEnemy(stack, target, attacker);
    }
}

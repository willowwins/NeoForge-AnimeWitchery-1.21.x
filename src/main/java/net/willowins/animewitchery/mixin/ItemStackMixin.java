package net.willowins.animewitchery.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.extensions.IItemStackExtension;
import net.willowins.animewitchery.enchantments.EnchantmentUtils;
import net.willowins.animewitchery.enchantments.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IItemStackExtension {

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isEmpty();

    @Shadow
    public abstract boolean isDamageableItem();

    @Shadow
    public abstract int getMaxDamage();

    @Shadow
    public abstract int getDamageValue();

    @Override
    public boolean canElytraFly(LivingEntity entity) {
        if (animewitchery$canFlightEnchantFly(entity)) {
            return true;
        }
        return this.getItem().canElytraFly((ItemStack) (Object) this, entity);
    }

    @Override
    public boolean elytraFlightTick(LivingEntity entity, int flightTicks) {
        if (animewitchery$canFlightEnchantFly(entity)) {
            if (!entity.level().isClientSide) {
                int nextFlightTick = flightTicks + 1;
                if (nextFlightTick % 10 == 0) {
                    entity.gameEvent(GameEvent.ELYTRA_GLIDE);
                }
            }
            return true;
        }
        return this.getItem().elytraFlightTick((ItemStack) (Object) this, entity, flightTicks);
    }

    private boolean animewitchery$canFlightEnchantFly(LivingEntity entity) {
        if (this.isEmpty()) {
            return false;
        }
        if (!(this.getItem() instanceof ArmorItem armor)) {
            return false;
        }
        if (armor.getType() != ArmorItem.Type.CHESTPLATE) {
            return false;
        }
        if (this.isDamageableItem() && this.getMaxDamage() - this.getDamageValue() <= 1) {
            return false;
        }
        if (entity == null || entity.level() == null) {
            return false;
        }
        return EnchantmentUtils.getLevel(entity.level(), ModEnchantments.FLIGHT_ENCHANT, (ItemStack) (Object) this) > 0;
    }
}

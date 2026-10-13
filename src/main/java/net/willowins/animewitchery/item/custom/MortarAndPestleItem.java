package net.willowins.animewitchery.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class MortarAndPestleItem extends Item {
    public MortarAndPestleItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack remainder = stack.copy();
        remainder.setDamageValue(remainder.getDamageValue() + 1);
        return remainder.getDamageValue() >= remainder.getMaxDamage() ? ItemStack.EMPTY : remainder;
    }
}

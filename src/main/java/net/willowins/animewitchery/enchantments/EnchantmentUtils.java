package net.willowins.animewitchery.enchantments;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

/**
 * Small helper for reading a mod enchantment's level off an item stack without each caller
 * having to resolve the dynamic enchantment registry by hand.
 */
public final class EnchantmentUtils {
    private EnchantmentUtils() {
    }

    public static int getLevel(Level level, ResourceKey<Enchantment> key, ItemStack stack) {
        return getLevel(level.registryAccess(), key, stack);
    }

    public static int getLevel(RegistryAccess registryAccess, ResourceKey<Enchantment> key, ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        Holder<Enchantment> holder = registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(key);
        return EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
    }
}

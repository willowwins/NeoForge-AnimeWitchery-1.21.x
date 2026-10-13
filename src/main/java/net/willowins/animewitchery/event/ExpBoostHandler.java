package net.willowins.animewitchery.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.willowins.animewitchery.enchantments.EnchantmentUtils;
import net.willowins.animewitchery.enchantments.ModEnchantments;

/**
 * Port of the Fabric {@code ExpBoostHandler}: a weapon enchanted with {@code exp_boost} multiplies
 * the experience dropped by a killed mob. The Fabric version spawned extra orbs after the kill;
 * NeoForge exposes {@link LivingExperienceDropEvent}, so we simply scale the dropped amount
 * (Lv1 = 2x, Lv5 = 6x).
 */
public final class ExpBoostHandler {
    private ExpBoostHandler() {
    }

    @SubscribeEvent
    public static void onExperienceDrop(LivingExperienceDropEvent event) {
        Player player = event.getAttackingPlayer();
        if (player == null) {
            return;
        }
        ItemStack weapon = player.getMainHandItem();
        int level = EnchantmentUtils.getLevel(player.level(), ModEnchantments.EXP_BOOST, weapon);
        if (level <= 0) {
            return;
        }
        int dropped = event.getDroppedExperience();
        if (dropped <= 0) {
            return;
        }
        event.setDroppedExperience(Math.round(dropped * (1.0F + level)));
    }
}

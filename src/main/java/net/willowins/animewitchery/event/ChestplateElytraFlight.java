package net.willowins.animewitchery.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.willowins.animewitchery.enchantments.EnchantmentUtils;
import net.willowins.animewitchery.enchantments.ModEnchantments;

/**
 * Damage half of the chestplate flight enchantment. The actual gliding is enabled by
 * {@code LivingEntityElytraFlightMixin}; this handler applies durability damage to the chestplate
 * while the player is gliding, mirroring the Fabric {@code ChestplateElytraFlight} handler
 * (Lv1 = every 20 ticks, faster with higher levels, floored at every 5 ticks).
 */
public final class ChestplateElytraFlight {
    private ChestplateElytraFlight() {
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!(chest.getItem() instanceof ArmorItem)) {
            return;
        }
        int level = EnchantmentUtils.getLevel(player.level(), ModEnchantments.FLIGHT_ENCHANT, chest);
        if (level <= 0) {
            return;
        }
        if (chest.isDamageableItem() && chest.getMaxDamage() - chest.getDamageValue() <= 1) {
            return;
        }
        if (player.isFallFlying()) {
            int damageInterval = Math.max(20 - (level - 1) * 5, 5);
            if (player.tickCount % damageInterval == 0) {
                chest.hurtAndBreak(1, player, EquipmentSlot.CHEST);
            }
            return;
        }
        // Don't auto-start; rely on vanilla jump-in-air to start elytra flight. The mixin allows the enchanted chestplate to work like elytra.
    }
}

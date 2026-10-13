package net.willowins.animewitchery.mana;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.item.custom.LatentManaPendantItem;
import net.willowins.animewitchery.network.ManaSyncPayload;
import top.theillusivec4.curios.api.CuriosApi;

public final class PlayerMana {
    private PlayerMana() {
    }

    public static boolean isLockedPlayer(Player player) {
        if (player == null || player.getGameProfile() == null) {
            return false;
        }
        String name = player.getGameProfile().getName();
        return "willow".equalsIgnoreCase(name) || "willowins".equalsIgnoreCase(name);
    }

    public static int getMana(Player player) {
        return player.getData(ModAttachments.MANA).current();
    }

    public static int getMaxMana(Player player) {
        return player.getData(ModAttachments.MANA).max() + getPendantBonus(player);
    }

    public static int getPendantBonus(Player player) {
        return CuriosApi.getCuriosInventory(player)
                .map(handler -> handler.findCurios(stack -> stack.getItem() instanceof LatentManaPendantItem))
                .map(results -> results.size() * LatentManaPendantItem.getBonusMana(player))
                .orElse(0);
    }

    public static int getLimit(Player player) {
        int max = getMaxMana(player);
        return isLockedPlayer(player) ? max * 2 : max;
    }

    public static void setMana(Player player, int amount) {
        int clamped = Math.max(0, Math.min(amount, getLimit(player)));
        ManaData data = player.getData(ModAttachments.MANA);
        if (data.current() == clamped) {
            return;
        }
        player.setData(ModAttachments.MANA, new ManaData(clamped, data.max()));
        sync(player);
    }

    public static boolean consume(Player player, int amount) {
        if (getMana(player) < amount) {
            return false;
        }
        setMana(player, getMana(player) - amount);
        return true;
    }

    public static void regen(Player player, int amount) {
        setMana(player, Math.min(getMana(player) + amount, getMaxMana(player)));
    }

    public static void sync(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            ManaData data = player.getData(ModAttachments.MANA);
            PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(data.current(), data.max()));
        }
    }
}
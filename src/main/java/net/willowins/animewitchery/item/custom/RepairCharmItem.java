package net.willowins.animewitchery.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class RepairCharmItem extends Item {
    private static final int REPAIR_INTERVAL = 20;

    public RepairCharmItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack charm, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || !(entity instanceof ServerPlayer player)) {
            return;
        }

        CustomData customData = charm.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        int ticks = tag.getInt("repair_ticks") + 1;
        if (ticks >= REPAIR_INTERVAL) {
            ticks = 0;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack candidate = player.getInventory().getItem(i);
                if (candidate.isDamageableItem() && candidate.isDamaged()) {
                    candidate.setDamageValue(candidate.getDamageValue() - 1);
                }
            }
        }
        tag.putInt("repair_ticks", ticks);
        charm.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}

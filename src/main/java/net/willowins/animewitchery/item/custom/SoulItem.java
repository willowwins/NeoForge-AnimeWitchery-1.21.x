package net.willowins.animewitchery.item.custom;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

public class SoulItem extends Item {
    public SoulItem(Properties properties) { super(properties); }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!data.contains("EntityData", CompoundTag.TAG_COMPOUND)) {
            tooltip.add(Component.literal("Empty Soul").withStyle(net.minecraft.ChatFormatting.DARK_GRAY));
            return;
        }
        tooltip.add(Component.literal("Entity: " + data.getString("EntityName")).withStyle(net.minecraft.ChatFormatting.GRAY));
        CompoundTag entity = data.getCompound("EntityData");
        if (entity.contains("Health")) {
            tooltip.add(Component.literal("Health: " + String.format(java.util.Locale.ROOT, "%.1f", entity.getFloat("Health")))
                    .withStyle(net.minecraft.ChatFormatting.RED));
        }
    }
}

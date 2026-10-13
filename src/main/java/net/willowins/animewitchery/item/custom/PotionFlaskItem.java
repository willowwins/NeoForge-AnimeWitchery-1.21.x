package net.willowins.animewitchery.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.item.ModItems;

public class PotionFlaskItem extends PotionItem {
    private static final int MAX_CHARGES = 4;
    private static final int BAR_COLOR = 0x3D3DEC;

    public PotionFlaskItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        int charges = getCharges(stack);
        // Let vanilla PotionItem apply the potion effects and award the consumed-item stat,
        // while keeping the flask stack available until its last charge is used.
        super.finishUsingItem(stack.copy(), level, user);

        if (user instanceof Player player && !player.getAbilities().instabuild) {
            if (charges > 1) {
                setCharges(stack, charges - 1);
                return stack;
            }

            stack.shrink(1);
            ItemStack emptyFlask = new ItemStack(ModItems.EMPTY_FLASK.get());
            if (stack.isEmpty()) {
                return emptyFlask;
            }
            if (!player.getInventory().add(emptyFlask)) {
                player.drop(emptyFlask, false);
            }
        }
        return stack;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return getCharges(stack) < MAX_CHARGES;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getCharges(stack) / MAX_CHARGES * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return BAR_COLOR;
    }

    private static int getCharges(ItemStack stack) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return tag.contains("animewitchery_charges")
                ? Math.clamp(tag.getInt("animewitchery_charges"), 0, MAX_CHARGES) : MAX_CHARGES;
    }

    private static void setCharges(ItemStack stack, int charges) {
        CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.putInt("animewitchery_charges", Math.clamp(charges, 0, MAX_CHARGES));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}

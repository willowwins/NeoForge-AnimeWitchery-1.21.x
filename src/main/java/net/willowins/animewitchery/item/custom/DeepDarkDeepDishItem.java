package net.willowins.animewitchery.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DeepDarkDeepDishItem extends Item {
    public DeepDarkDeepDishItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.getCooldowns().addCooldown(this, 20);

        if (player.experienceLevel >= 1) {
            player.getFoodData().eat(4, 8f);
            player.giveExperienceLevels(-1);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.FAIL, stack);
        }
    }
}

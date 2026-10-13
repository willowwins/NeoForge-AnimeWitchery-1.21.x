package net.willowins.animewitchery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RepairEssenceItem extends Item {
    private static final int REPAIR_AMOUNT = 2000;

    public RepairEssenceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack essence = player.getItemInHand(hand);
        ItemStack target = player.getItemInHand(hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        if (target.isEmpty() || !target.isDamageableItem() || !target.isDamaged()) {
            return InteractionResultHolder.pass(essence);
        }

        if (!level.isClientSide) {
            int repaired = Math.min(target.getDamageValue(), REPAIR_AMOUNT);
            target.setDamageValue(target.getDamageValue() - repaired);
            essence.shrink(1);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_USE,
                    SoundSource.PLAYERS, 0.8F, 1.2F);
            player.displayClientMessage(Component.literal("Repaired " + target.getHoverName().getString()
                    + " by " + repaired + " durability."), true);
        }
        return InteractionResultHolder.sidedSuccess(essence, level.isClientSide);
    }
}

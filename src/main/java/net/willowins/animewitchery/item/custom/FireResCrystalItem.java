package net.willowins.animewitchery.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FireResCrystalItem extends Item {
    public FireResCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide) {
            apply(player);
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, 1200);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide) {
            apply(target);
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide);
    }

    private static void apply(LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2400, 1, true, true));
    }
}

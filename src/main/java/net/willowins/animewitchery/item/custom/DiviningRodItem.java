package net.willowins.animewitchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class DiviningRodItem extends Item {
    private static final int RADIUS = 10;

    public DiviningRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos origin = player.blockPosition();
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -RADIUS; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    BlockPos candidate = origin.offset(x, y, z);
                    if (isValuable(level, candidate)) {
                        double distance = origin.distSqr(candidate);
                        if (distance < nearestDistance) {
                            nearest = candidate;
                            nearestDistance = distance;
                        }
                    }
                }
            }
        }

        if (level.isClientSide) {
            if (nearest != null) {
                player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.0F);
            }
        } else {
            player.displayClientMessage(nearest == null
                    ? Component.literal("No valuable ore nearby.")
                    : Component.literal("Found valuable ore at (" + nearest.getX() + ", " + nearest.getY()
                            + ", " + nearest.getZ() + ")"), true);
            player.getCooldowns().addCooldown(this, 50);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    private static boolean isValuable(Level level, BlockPos pos) {
        var block = level.getBlockState(pos).getBlock();
        return block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE
                || block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE
                || block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE
                || block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE
                || block == Blocks.ANCIENT_DEBRIS;
    }
}

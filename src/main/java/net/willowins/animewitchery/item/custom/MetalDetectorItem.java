package net.willowins.animewitchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class MetalDetectorItem extends Item {
    private static final net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> DETECTABLE =
            BlockTags.create(ResourceLocation.fromNamespaceAndPath("animewitchery", "metal_detector_detectable_blocks"));

    public MetalDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        BlockPos clicked = context.getClickedPos();
        var player = context.getPlayer();
        if (!level.isClientSide) {
            BlockPos found = null;
            BlockState foundState = null;
            int bottomY = level.getMinBuildHeight();
            for (int y = clicked.getY(); y >= bottomY; y--) {
                BlockPos candidate = new BlockPos(clicked.getX(), y, clicked.getZ());
                BlockState state = level.getBlockState(candidate);
                if (state.is(DETECTABLE)) {
                    found = candidate;
                    foundState = state;
                    break;
                }
            }

            if (player != null) {
                if (found == null) {
                    player.displayClientMessage(Component.literal("No Valuables Found"), true);
                } else {
                    player.displayClientMessage(Component.literal("Found " + foundState.getBlock().getName().getString()
                            + " at (" + found.getX() + ", " + found.getY() + ", " + found.getZ() + ")"), true);
                }
            }
        }

        if (player != null) {
            EquipmentSlot slot = context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                    ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            context.getItemInHand().hurtAndBreak(1, player, slot);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

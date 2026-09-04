package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.block.entity.StarlightInfusionPedestalBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StarlightInfusionPedestalBlock extends Block implements EntityBlock {

    public StarlightInfusionPedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.STARLIGHT_INFUSION_PEDESTAL_BE.get().create(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof StarlightInfusionPedestalBlockEntity be) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), be.getHeldItem());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof StarlightInfusionPedestalBlockEntity be) {
            if (be.getHeldItem().isEmpty()) {
                ItemStack hand = player.getMainHandItem();
                if (!hand.isEmpty()) {
                    be.setHeldItem(hand.copyWithCount(1));
                    hand.shrink(1);
                }
            } else {
                if (!player.getInventory().add(be.getHeldItem())) {
                    player.drop(be.getHeldItem(), false);
                }
                be.setHeldItem(ItemStack.EMPTY);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

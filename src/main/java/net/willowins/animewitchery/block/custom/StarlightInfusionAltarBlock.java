package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.block.entity.StarlightInfusionAltarBlockEntity;
import org.jetbrains.annotations.Nullable;

public class StarlightInfusionAltarBlock extends Block implements EntityBlock {

    public StarlightInfusionAltarBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide || type != ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get()) {
            return null;
        }
        return (lvl, pos, bs, be) -> StarlightInfusionAltarBlockEntity.serverTick(lvl, pos, bs, (StarlightInfusionAltarBlockEntity) be);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, @Nullable net.minecraft.core.Direction direction) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof StarlightInfusionAltarBlockEntity be) {
                // Give back the base item and any in-progress result when the altar is broken.
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), be.getBaseItem());
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), be.getPendingResult());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof StarlightInfusionAltarBlockEntity be) {
            // Let buckets fill/drain starlight.
            if (player.getMainHandItem().getItem() instanceof net.minecraft.world.item.BucketItem || player.getMainHandItem().isEmpty()) {
                boolean handledFluid = FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, level, pos, hitResult.getDirection());
                if (handledFluid) {
                    return InteractionResult.SUCCESS;
                }
            }
            // Place / take the base item (only while not crafting).
            if (be.getProgress() <= 0) {
                if (be.getBaseItem().isEmpty()) {
                    ItemStack hand = player.getMainHandItem();
                    if (!hand.isEmpty()) {
                        be.setBaseItem(hand.copyWithCount(1));
                        hand.shrink(1);
                    }
                } else {
                    if (!player.getInventory().add(be.getBaseItem())) {
                        player.drop(be.getBaseItem(), false);
                    }
                    be.setBaseItem(ItemStack.EMPTY);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.block.entity.StarlighPoolBlockEntity;
import org.jetbrains.annotations.Nullable;

// If you want it to behave like a Glass/Transparent block, you can extend TransparentBlock instead
public class StarlightPoolBlock extends Block implements EntityBlock {

    public StarlightPoolBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        // Uses the registered BlockEntityType to safely instantiate your Block Entity
        return ModBlockEntities.STARLIGHT_POOL_BE.get().create(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        // BaseEntityBlock defaults to INVISIBLE, but normal Block defaults to MODEL.
        // If extending Block, keeping this or using super is fine.
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof StarlighPoolBlockEntity) {
                // Seamlessly handles right-click interactions with fluid buckets/containers
                boolean success = FluidUtil.interactWithFluidHandler(player, net.minecraft.world.InteractionHand.MAIN_HAND, level, pos, hitResult.getDirection());
                if (success) {
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.fluid.ModFluids;

public class StarlightLiquidBlock extends LiquidBlock {

    public StarlightLiquidBlock(Properties properties) {
        super(ModFluids.STARLIGHT_SOURCE.get(), properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        FluidState fluidState = level.getFluidState(pos);
        if (fluidState.getType() != ModFluids.STARLIGHT_FLOWING.get()) return;
        if (fluidState.getAmount() > 1) return;

        for (Direction dir : Direction.values()) {
            BlockPos adjacent = pos.relative(dir);
            if (level.getBlockState(adjacent).is(Blocks.MAGMA_BLOCK)) {
                level.setBlockAndUpdate(pos, ModBlocks.CRYSTALLIZED_STARLIGHT.get().defaultBlockState());
                return;
            }
        }
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}

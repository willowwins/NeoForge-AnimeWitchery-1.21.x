package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.block.ModBlocks;

public class BlumeBlock extends TallFlowerBlock {
    public BlumeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(ModBlocks.DARK_GRASS.get()) || super.mayPlaceOn(state, level, pos);
    }
}

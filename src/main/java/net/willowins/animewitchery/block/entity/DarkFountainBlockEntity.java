package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DarkFountainBlockEntity extends BlockEntity {

    public DarkFountainBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DARK_FOUNTAIN_BE.get(), pos, blockState);
    }
}
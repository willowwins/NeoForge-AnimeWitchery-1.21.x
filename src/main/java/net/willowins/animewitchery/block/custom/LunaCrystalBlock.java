package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import net.willowins.animewitchery.block.entity.LunaCrystalBlockEntity;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class LunaCrystalBlock extends TransparentBlock implements BeaconBeamBlock, EntityBlock {
    private static final int LUNA_PINK = 0xFFFFC6F9;
    private static final int LUNA_BLUE = 0xFFCBEEFF;
    private static final int LUNA_WHITE = 0xFFFFFFFF;
    private static final int GRADIENT_HEIGHT = 24;

    public LunaCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.LIGHT_BLUE;
    }

    @Nullable
    @Override
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        int relativeY = Math.floorMod(pos.getY() - beaconPos.getY(), GRADIENT_HEIGHT);
        float progress = (float) relativeY / (float) GRADIENT_HEIGHT;

        if (progress < 0.33F) {
            return LUNA_PINK;
        }

        if (progress < 0.66F) {
            return LUNA_BLUE;
        }

        return LUNA_WHITE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.LUNA_CRYSTAL_BE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }
        if (type != ModBlockEntities.LUNA_CRYSTAL_BE.get()) {
            return null;
        }
        return (lvl, pos, bs, be) -> LunaCrystalBlockEntity.serverTick(lvl, pos, bs, (LunaCrystalBlockEntity) be);
    }
}

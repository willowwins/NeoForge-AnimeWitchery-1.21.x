package net.willowins.animewitchery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

/**
 * The receiving counterpart to {@link PocketExpansionJigsawBlock}. Structure pieces place this
 * block to signal where the structure should generate from; {@code PocketExpansionPlacer} anchors
 * the piece's placement on it.
 */
public class PocketExpansionReceiverBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<PocketExpansionReceiverBlock> CODEC = simpleCodec(PocketExpansionReceiverBlock::new);

    public PocketExpansionReceiverBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
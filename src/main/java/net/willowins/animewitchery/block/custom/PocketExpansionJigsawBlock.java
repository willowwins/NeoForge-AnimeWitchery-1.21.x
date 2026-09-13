package net.willowins.animewitchery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.network.PocketOptionsRequestPayload;

public class PocketExpansionJigsawBlock extends HorizontalDirectionalBlock {

    public static final MapCodec<PocketExpansionJigsawBlock> CODEC = simpleCodec(PocketExpansionJigsawBlock::new);

    public PocketExpansionJigsawBlock(Properties properties) {
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

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.isClientSide) {
            PacketDistributor.sendToServer(new PocketOptionsRequestPayload(pos));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }
}
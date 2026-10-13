package net.willowins.animewitchery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.willowins.animewitchery.block.entity.AntifountainBlockEntity;
import org.jetbrains.annotations.Nullable;

public class AntifountainBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<AntifountainBlock> CODEC = simpleCodec(AntifountainBlock::new);

    public AntifountainBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AntifountainBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof AntifountainBlockEntity be) {
                be.restore();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AntifountainBlockEntity be) {
            boolean opening = !be.isActive();
            if (opening && !isMeleeWeapon(player.getMainHandItem())) {
                player.displayClientMessage(
                        Component.translatable("message.animewitchery.antifountain.melee"), true);
                return InteractionResult.sidedSuccess(true);
            }
            be.toggle();
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean isMeleeWeapon(ItemStack stack) {
        net.minecraft.world.item.Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof AxeItem;
    }
}
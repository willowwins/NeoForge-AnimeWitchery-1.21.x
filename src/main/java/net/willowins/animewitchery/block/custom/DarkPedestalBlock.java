package net.willowins.animewitchery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.willowins.animewitchery.block.entity.DarkPedestalBlockEntity;
import org.jetbrains.annotations.Nullable;

public class DarkPedestalBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final MapCodec<DarkPedestalBlock> CODEC = simpleCodec(DarkPedestalBlock::new);

    public DarkPedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DarkPedestalBlockEntity(pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DarkPedestalBlockEntity be) {
            ItemStack hand = player.getMainHandItem();
            
            // If player is holding an amethyst shard and pedestal is empty, place it
            if (hand.is(Items.AMETHYST_SHARD) && be.getHeldItem().isEmpty()) {
                if (!player.getAbilities().instabuild) {
                    hand.shrink(1);
                }
                be.setHeldItem(new ItemStack(Items.AMETHYST_SHARD));
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            
            // If pedestal has an item and player is not holding anything (or sneak), take it
            if (!be.getHeldItem().isEmpty() && (hand.isEmpty() || player.isShiftKeyDown())) {
                if (player.getAbilities().instabuild || player.getInventory().add(be.getHeldItem())) {
                    if (!player.getAbilities().instabuild && hand.isEmpty()) {
                        player.setItemInHand(player.getUsedItemHand(), be.getHeldItem().copy());
                    } else if (!player.getAbilities().instabuild) {
                        player.drop(be.getHeldItem(), false);
                    }
                    be.setHeldItem(ItemStack.EMPTY);
                    level.playSound(null, pos, net.minecraft.sounds.SoundEvents.AMETHYST_BLOCK_CHIME, net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
            
            // If empty hand and no item on pedestal, trigger the teleport
            if (hand.isEmpty() && be.getHeldItem().isEmpty()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    be.handleUse(serverPlayer);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
}
package net.willowins.animewitchery.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.willowins.animewitchery.block.entity.AlchemyTableBlockEntity;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.menu.AlchemyTableMenu;
import org.jetbrains.annotations.Nullable;

public class AlchemyTableBlock extends BaseEntityBlock {
    public static final MapCodec<AlchemyTableBlock> CODEC = simpleCodec(AlchemyTableBlock::new);
    private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 1.25, 1);

    public AlchemyTableBlock(Properties properties) {
        super(properties);
    }

    @Override protected MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }

    @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableBlockEntity(pos, state);
    }

    @Nullable
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type,
                ModBlockEntities.ALCHEMY_TABLE_BE.get(), AlchemyTableBlockEntity::serverTick);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack held, BlockState state, Level level, BlockPos pos,
                                          Player player, net.minecraft.world.InteractionHand hand,
                                          BlockHitResult hit) {
        if (!held.is(ModItems.ALCHEMICAL_CATALYST.get()) && !held.is(ModItems.RESONANT_CATALYST.get())) {
            return openMenu(level, pos, player) ? ItemInteractionResult.CONSUME
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table
                && table.activateWithCatalyst(player, held)) {
            held.shrink(1);
            level.playSound(null, pos, net.minecraft.sounds.SoundEvents.BEACON_ACTIVATE,
                    net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(net.minecraft.core.particles.ParticleTypes.PORTAL,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        20, 1.0, 0.6, 1.0, 0.05);
            }
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                               Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        return openMenu(level, pos, player) ? InteractionResult.CONSUME : InteractionResult.PASS;
    }

    private boolean openMenu(Level level, BlockPos pos, Player player) {
        if (level.isClientSide) return true;
        if (!(level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table) || table.isActivated()) return false;
        player.openMenu(new net.minecraft.world.SimpleMenuProvider(
                (id, inventory, user) -> new AlchemyTableMenu(id, inventory, table),
                Component.translatable("container.animewitchery.alchemy_table")),
                buffer -> buffer.writeBlockPos(pos));
        return true;
    }
}


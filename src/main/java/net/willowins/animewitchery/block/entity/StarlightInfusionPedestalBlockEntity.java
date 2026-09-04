package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * A pedestal holds a single item that can be fed into a bound infusion altar.
 * It holds no data of its own beyond the item and the altar it is bound to.
 */
public class StarlightInfusionPedestalBlockEntity extends BlockEntity {

    private ItemStack heldItem = ItemStack.EMPTY;
    @Nullable
    private BlockPos boundAltar;

    public StarlightInfusionPedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STARLIGHT_INFUSION_PEDESTAL_BE.get(), pos, blockState);
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack stack) {
        this.heldItem = stack;
        setChangedAndSync();
    }

    @Nullable
    public BlockPos getBoundAltar() {
        return boundAltar;
    }

    public void setBoundAltar(@Nullable BlockPos altarPos) {
        this.boundAltar = altarPos;
        setChangedAndSync();
    }

    private void setChangedAndSync() {
        setChanged();
        if (hasLevel() && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public boolean isBoundTo(BlockPos altarPos) {
        return altarPos != null && altarPos.equals(this.boundAltar);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!heldItem.isEmpty()) {
            tag.put("Item", heldItem.save(registries));
        }
        if (boundAltar != null) {
            tag.putInt("BAX", boundAltar.getX());
            tag.putInt("BAY", boundAltar.getY());
            tag.putInt("BAZ", boundAltar.getZ());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        heldItem = ItemStack.parse(registries, tag.getCompound("Item")).orElse(ItemStack.EMPTY);
        boundAltar = tag.contains("BAX")
                ? new BlockPos(tag.getInt("BAX"), tag.getInt("BAY"), tag.getInt("BAZ"))
                : null;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
        loadAdditional(packet.getTag(), registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }
}

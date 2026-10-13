package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import net.willowins.animewitchery.dimension.PocketDimensions;
import net.willowins.animewitchery.dimension.PocketRegistry;

import java.util.Optional;

public class DarkPedestalBlockEntity extends BlockEntity {

    private int pocketIndex = -1;
    private ItemStack heldItem = ItemStack.EMPTY;

    public DarkPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DARK_PEDESTAL_BE.get(), pos, state);
    }

    public int getPocketIndex() {
        return pocketIndex;
    }

    public void setPocketIndex(int pocketIndex) {
        this.pocketIndex = pocketIndex;
        setChanged();
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack stack) {
        this.heldItem = stack;
        setChangedAndSync();
    }

    private void setChangedAndSync() {
        setChanged();
        if (hasLevel() && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    /**
     * The four pedestals of this pocket's ritual: scans around a point in the same
     * level for pedestals linked to the same pocket index.
     */
    public static java.util.List<DarkPedestalBlockEntity> findConnected(Level level, BlockPos center, int pocketIndex) {
        java.util.List<DarkPedestalBlockEntity> result = new java.util.ArrayList<>();
        int radius = 16;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -8; dy <= 16; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = center.offset(dx, dy, dz);
                    if (level.getBlockEntity(pos) instanceof DarkPedestalBlockEntity pedestal
                            && pedestal.getPocketIndex() == pocketIndex) {
                        result.add(pedestal);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Closes the fountain's veil entrance in the overworld and sends the player back
     * to it. Only works once all four connected pedestals hold an amethyst shard.
     */
    public void handleUse(ServerPlayer player) {
        if (pocketIndex < 0 || level == null) {
            return;
        }
        java.util.List<DarkPedestalBlockEntity> pedestals = findConnected(level, worldPosition, pocketIndex);
        if (pedestals.size() != 4) {
            player.displayClientMessage(Component.literal(
                    "The fountain needs exactly 4 dark pedestals (found " + pedestals.size() + ")."), true);
            return;
        }
        boolean allHoldShards = pedestals.stream().allMatch(p -> p.getHeldItem().is(Items.AMETHYST_SHARD));
        if (!allHoldShards) {
            player.displayClientMessage(Component.literal(
                    "All 4 dark pedestals must hold an amethyst shard to close the fountain."), true);
            return;
        }
        MinecraftServer server = ((ServerLevel) level).getServer();
        PocketRegistry registry = PocketRegistry.get(server.overworld());
        Optional<PocketRegistry.PocketEntry> maybe = registry.get(pocketIndex);
        if (maybe.isEmpty()) {
            return;
        }
        PocketRegistry.PocketEntry entry = maybe.get();
        ServerLevel fountainLevel = server.getLevel(entry.fountainDimension());
        if (fountainLevel == null) {
            return;
        }
        BlockPos fountain = entry.fountainPos();
        fountainLevel.getChunk(fountain.getX() >> 4, fountain.getZ() >> 4, ChunkStatus.FULL, true);
        if (fountainLevel.getBlockEntity(fountain) instanceof AntifountainBlockEntity af) {
            af.restore();
        }
        PocketDimensions.teleportTo(player, fountainLevel,
                PocketDimensions.computeReturnPosition(fountainLevel, entry, player));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("PocketIndex", pocketIndex);
        if (!heldItem.isEmpty()) {
            tag.put("Item", heldItem.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        pocketIndex = tag.getInt("PocketIndex");
        heldItem = ItemStack.parse(registries, tag.getCompound("Item")).orElse(ItemStack.EMPTY);
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
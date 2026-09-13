package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.dimension.PocketDimensions;
import net.willowins.animewitchery.dimension.PocketRegistry;
import net.willowins.animewitchery.worldgen.PocketRoomBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AntifountainBlockEntity extends BlockEntity {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("AntifountainBlockEntity");

    /** Fill box: 21x21x5, shifted one block up from the fountain. */
    private static final int HALF_X = 10;
    private static final int HALF_Z = 10;
    private static final int FILL_Y_MIN = -1;
    private static final int FILL_Y_MAX = 3;

    private int pocketIndex = -1;
    @Nullable
    private BlockPos roomMin;
    @Nullable
    private BlockPos roomMax;

    public AntifountainBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ANTIFOUNTAIN_BE.get(), pos, blockState);
    }

    public boolean isActive() {
        return roomMin != null && roomMax != null;
    }

    public void toggle() {
        if (level == null || level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (isActive()) {
            if (countVeils(serverLevel) == 0) {
                LOGGER.info("Antifountain {}: active but empty, re-filling", worldPosition);
                fillVeil(serverLevel);
                setChangedAndSync();
                return;
            }
            restore();
            return;
        }

        BlockPos min = new BlockPos(
                worldPosition.getX() - HALF_X,
                Math.max(serverLevel.getMinBuildHeight(), worldPosition.getY() + FILL_Y_MIN),
                worldPosition.getZ() - HALF_Z);
        BlockPos max = new BlockPos(
                worldPosition.getX() + HALF_X,
                Math.min(serverLevel.getMaxBuildHeight() - 1, worldPosition.getY() + FILL_Y_MAX),
                worldPosition.getZ() + HALF_Z);
        roomMin = min;
        roomMax = max;
        LOGGER.info("Antifountain {}: filling {} .. {}", worldPosition, roomMin, roomMax);

        MinecraftServer server = serverLevel.getServer();
        PocketRegistry registry = PocketRegistry.get(server.overworld());
        if (pocketIndex < 0) {
            pocketIndex = registry.getOrCreateIndex(serverLevel.dimension(), worldPosition);
        }
        Optional<PocketRegistry.PocketEntry> maybe = registry.get(pocketIndex);
        if (maybe.isEmpty()) {
            roomMin = null;
            roomMax = null;
            return;
        }

        ServerLevel pocketLevel = PocketDimensions.getServerPocket(serverLevel);
        try {
            if (pocketLevel != null) {
                PocketRoomBuilder.build(pocketLevel, maybe.get());
            }
        } catch (Exception e) {
            LOGGER.error("Antifountain {}: pocket build failed, overworld veils still fill", worldPosition, e);
        }

        fillVeil(serverLevel);
        LOGGER.info("Antifountain {}: filled veils, pocketIndex={}", worldPosition, pocketIndex);
        setChangedAndSync();
    }

    private int countVeils(ServerLevel serverLevel) {
        if (roomMin == null || roomMax == null) {
            return 0;
        }
        int count = 0;
        for (BlockPos cell : BlockPos.betweenClosed(roomMin, roomMax)) {
            if (serverLevel.getBlockState(cell).is(ModBlocks.POCKET_VEIL.get())) {
                count++;
            }
        }
        return count;
    }

    public void restore() {
        if (level == null || level.isClientSide || roomMin == null || roomMax == null) {
            roomMin = null;
            roomMax = null;
            return;
        }
        for (BlockPos cell : BlockPos.betweenClosed(roomMin, roomMax)) {
            if (level.getBlockState(cell).is(ModBlocks.POCKET_VEIL.get())) {
                level.setBlock(cell.immutable(), Blocks.AIR.defaultBlockState(), 2);
            }
        }
        roomMin = null;
        roomMax = null;
        LOGGER.info("Antifountain {}: restored (veils removed)", worldPosition);
        setChangedAndSync();
    }

    private void fillVeil(ServerLevel serverLevel) {
        if (roomMin == null || roomMax == null) {
            return;
        }
        BlockState veilState = ModBlocks.POCKET_VEIL.get().defaultBlockState();
        for (BlockPos cell : BlockPos.betweenClosed(roomMin, roomMax)) {
            if (!serverLevel.getBlockState(cell).isAir()) {
                continue;
            }
            serverLevel.setBlock(cell.immutable(), veilState, 2);
            if (serverLevel.getBlockEntity(cell) instanceof PocketVeilBlockEntity be) {
                be.setPocketIndex(pocketIndex);
            }
        }
    }

    private void setChangedAndSync() {
        setChanged();
        if (hasLevel() && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("PocketIndex", pocketIndex);
        tag.putBoolean("Active", isActive());
        if (roomMin != null) {
            tag.putInt("MinX", roomMin.getX());
            tag.putInt("MinY", roomMin.getY());
            tag.putInt("MinZ", roomMin.getZ());
            tag.putInt("MaxX", roomMax.getX());
            tag.putInt("MaxY", roomMax.getY());
            tag.putInt("MaxZ", roomMax.getZ());
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        pocketIndex = tag.getInt("PocketIndex");
        if (tag.getBoolean("Active")) {
            roomMin = new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"));
            roomMax = new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
        } else {
            roomMin = null;
            roomMax = null;
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
}
package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.dimension.PocketDimensions;
import net.willowins.animewitchery.dimension.PocketRegistry;

import java.util.Optional;

public class PocketVeilBlockEntity extends BlockEntity {

    private int pocketIndex = -1;

    public PocketVeilBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.POCKET_VEIL_BE.get(), pos, blockState);
    }

    public int getPocketIndex() {
        return pocketIndex;
    }

    public void setPocketIndex(int pocketIndex) {
        this.pocketIndex = pocketIndex;
        setChanged();
    }

    public void teleport(Player player) {
        if (level == null || level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        if (!PocketDimensions.canTriggerVeil(serverPlayer)) {
            return;
        }
        if (pocketIndex < 0) {
            return;
        }
        MinecraftServer server = ((ServerLevel) level).getServer();
        PocketRegistry registry = PocketRegistry.get(server.overworld());
        Optional<PocketRegistry.PocketEntry> maybe = registry.get(pocketIndex);
        if (maybe.isEmpty()) {
            return;
        }
        PocketRegistry.PocketEntry entry = maybe.get();

        boolean inPocket = ((ServerLevel) level).dimension() == PocketDimensions.POCKET;
        if (inPocket) {
            ServerLevel target = server.getLevel(entry.fountainDimension());
            if (target == null) {
                return;
            }
            PocketDimensions.teleportTo(serverPlayer, target,
                    PocketDimensions.computeReturnPosition(target, entry, serverPlayer));
        } else {
            ServerLevel target = PocketDimensions.getServerPocket((ServerLevel) level);
            if (target == null) {
                return;
            }
            PocketDimensions.recordEntryPosition(serverPlayer, worldPosition);
            PocketDimensions.teleportTo(serverPlayer, target, PocketDimensions.computePocketSpawn(target, entry));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("PocketIndex", pocketIndex);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        pocketIndex = tag.getInt("PocketIndex");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
}
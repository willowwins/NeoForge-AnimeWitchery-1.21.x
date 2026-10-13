package net.willowins.animewitchery.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.block.ModBlocks;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PocketDimensions {
    private PocketDimensions() {}

    public static final ResourceKey<Level> POCKET =
            ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("animewitchery", "pocket"));

    public static final double MIN_DISTANCE = 1.0;

    /** How far outside a 21-wide box the fountain's doorway can be from the fountain block. */
    private static final int DOORWAY_RANGE = 6;

    private static final int TELEPORT_COOLDOWN_TICKS = 20;

    private static final Map<UUID, Long> lastVeilTeleport = new HashMap<>();

    private static final Map<UUID, BlockPos> lastEntryPosition = new HashMap<>();

    public static boolean canTriggerVeil(ServerPlayer player) {
        long now = player.serverLevel().getGameTime();
        Long last = lastVeilTeleport.get(player.getUUID());
        if (last != null && now - last < TELEPORT_COOLDOWN_TICKS) {
            return false;
        }
        lastVeilTeleport.put(player.getUUID(), now);
        if (lastVeilTeleport.size() > 256) {
            lastVeilTeleport.entrySet().removeIf(e -> now - e.getValue() > 200);
        }
        return true;
    }

    public static void recordEntryPosition(ServerPlayer player, BlockPos veilCell) {
        lastEntryPosition.put(player.getUUID(), veilCell.immutable());
        if (lastEntryPosition.size() > 256) {
            lastEntryPosition.clear();
        }
    }

    /**
     * Where to drop the player when they return from the pocket to the fountain room.
     * Prefers the cell they stepped through when entering (their doorway); otherwise
     * scans just outside the filled box for the first clear spot; otherwise the fountain.
     */
    public static BlockPos computeReturnPosition(ServerLevel fountainLevel, PocketRegistry.PocketEntry entry, ServerPlayer player) {
        BlockPos fountain = entry.fountainPos();
        BlockPos entryPos = lastEntryPosition.remove(player.getUUID());
        if (entryPos != null) {
            int dx = Integer.compare(entryPos.getX(), fountain.getX());
            int dz = Integer.compare(entryPos.getZ(), fountain.getZ());
            if (dx != 0 || dz != 0) {
                BlockPos candidate = entryPos.offset(dx, 0, dz);
                if (isStandingRoom(fountainLevel, candidate)) {
                    return candidate;
                }
            }
        }
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (int r = 11; r <= 11 + DOORWAY_RANGE; r++) {
                BlockPos candidate = fountain.relative(dir, r).atY(fountain.getY());
                if (isStandingRoom(fountainLevel, candidate)) {
                    return candidate;
                }
            }
        }
        return fountain.above();
    }

    private static boolean isStandingRoom(ServerLevel level, BlockPos pos) {
        BlockState here = level.getBlockState(pos);
        BlockState below = level.getBlockState(pos.below());
        BlockState above = level.getBlockState(pos.above());
        return here.isAir() && above.isAir() && !below.isAir();
    }

    public static void teleportTo(ServerPlayer player, ServerLevel targetLevel, BlockPos pos) {
        player.teleportTo(targetLevel, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, player.getYRot(), player.getXRot());
    }

    /**
     * Where to drop a player entering the pocket: on the dark-flagstone hub floor,
     * searching outward from the fountain for the nearest standing cell.
     */
    public static BlockPos computePocketSpawn(ServerLevel pocketLevel, PocketRegistry.PocketEntry entry) {
        BlockPos origin = entry.pocketOrigin();
        for (int r = 0; r <= 10; r++) {
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != r) {
                        continue;
                    }
                    BlockPos stand = findFlagstoneStand(pocketLevel, origin, dx, dz);
                    if (stand != null) {
                        return stand;
                    }
                }
            }
        }
        return origin.offset(0, 8, 1);
    }

    private static BlockPos findFlagstoneStand(ServerLevel level, BlockPos origin, int dx, int dz) {
        int x = origin.getX() + dx;
        int z = origin.getZ() + dz;
        for (int y = origin.getY(); y <= origin.getY() + 12; y++) {
            BlockState floor = level.getBlockState(new BlockPos(x, y, z));
            if (!floor.is(ModBlocks.DARK_FLAGSTONE.get()) && !floor.is(net.minecraft.world.level.block.Blocks.END_STONE)) {
                continue;
            }
            if (level.getBlockState(new BlockPos(x, y + 1, z)).isAir()
                    && level.getBlockState(new BlockPos(x, y + 2, z)).isAir()) {
                return new BlockPos(x, y + 1, z);
            }
        }
        return null;
    }

    public static ServerLevel getServerPocket(ServerLevel anyServerLevel) {
        return anyServerLevel.getServer().getLevel(POCKET);
    }
}

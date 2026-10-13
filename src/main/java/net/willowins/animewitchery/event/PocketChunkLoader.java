package net.willowins.animewitchery.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.dimension.PocketDimensions;
import net.willowins.animewitchery.dimension.PocketRegistry;

import java.util.HashSet;
import java.util.Set;

/**
 * Keeps each darkworld fountain (and hence the hub it sits in) chunk-loaded while any player is
 * inside the pocket dimension, so the fountain never unloads under you.
 */
@EventBusSubscriber(modid = AnimeWitchery.MOD_ID)
public final class PocketChunkLoader {

    private PocketChunkLoader() {}

    /** The hub spans 21x21 blocks around its origin, so a 2x2 chunk square covers it. */
    private static final int CHUNK_RADIUS = 1;

    private static final Set<ChunkPos> FORCED = new HashSet<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (serverLevel.dimension() != PocketDimensions.POCKET) {
            return;
        }

        Set<ChunkPos> desired = new HashSet<>();
        if (!serverLevel.players().isEmpty()) {
            MinecraftServer server = serverLevel.getServer();
            PocketRegistry registry = PocketRegistry.get(server.overworld());
            for (PocketRegistry.PocketEntry entry : registry.allEntries()) {
                if (!entry.hubPlaced(serverLevel)) {
                    continue;
                }
                BlockPos origin = entry.pocketOrigin();
                int cx = origin.getX() >> 4;
                int cz = origin.getZ() >> 4;
                for (int dx = -CHUNK_RADIUS; dx <= CHUNK_RADIUS; dx++) {
                    for (int dz = -CHUNK_RADIUS; dz <= CHUNK_RADIUS; dz++) {
                        desired.add(new ChunkPos(cx + dx, cz + dz));
                    }
                }
            }
        }

        for (ChunkPos pos : FORCED) {
            if (!desired.contains(pos)) {
                serverLevel.setChunkForced(pos.x, pos.z, false);
            }
        }
        for (ChunkPos pos : desired) {
            if (!FORCED.contains(pos)) {
                serverLevel.setChunkForced(pos.x, pos.z, true);
            }
        }
        FORCED.clear();
        FORCED.addAll(desired);
    }
}
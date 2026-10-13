package net.willowins.animewitchery.util;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.willowins.animewitchery.AnimeWitchery;

public final class ModExplosionManager {
    private ModExplosionManager() {
    }

    public interface TickableExplosion {
        boolean tickOnce(MinecraftServer server);
    }

    private static final Set<TickableExplosion> ACTIVE = new LinkedHashSet<>();

    public static void add(TickableExplosion explosion) {
        ACTIVE.add(explosion);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        ACTIVE.removeIf(explosion -> {
            try {
                return explosion.tickOnce(server);
            } catch (Throwable t) {
                AnimeWitchery.LOGGER.error("Error while ticking explosion", t);
                return true;
            }
        });
    }
}
package net.willowins.animewitchery.network;

import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.Event;

import java.util.List;

public class PocketOptionsEvent extends Event {

    private final BlockPos pos;
    private final List<String> names;

    public PocketOptionsEvent(BlockPos pos, List<String> names) {
        this.pos = pos;
        this.names = names;
    }

    public BlockPos getPos() {
        return pos;
    }

    public List<String> getNames() {
        return names;
    }
}
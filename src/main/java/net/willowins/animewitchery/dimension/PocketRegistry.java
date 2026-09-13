package net.willowins.animewitchery.dimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.util.datafix.DataFixTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PocketRegistry extends SavedData {

    private static final String DATA_NAME = "animewitchery_pocket_registry";
    private static final int SPACING = 64;

    private int nextIndex = 0;
    private final Map<Integer, PocketEntry> entries = new HashMap<>();

    public record PocketEntry(
            int index,
            ResourceKey<Level> fountainDimension,
            BlockPos fountainPos,
            BlockPos pocketOrigin) {
    }

    public static PocketRegistry load(CompoundTag tag, HolderLookup.Provider registries) {
        PocketRegistry registry = new PocketRegistry();
        registry.nextIndex = tag.getInt("NextIndex");
        ListTag list = tag.getList("Entries", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entryTag = list.getCompound(i);
            int index = entryTag.getInt("Index");
            ResourceKey<Level> fountainDim = ResourceKey.create(
                    Registries.DIMENSION,
                    ResourceLocation.parse(entryTag.getString("FountainDimension")));
            BlockPos fountainPos = new BlockPos(
                    entryTag.getInt("FX"), entryTag.getInt("FY"), entryTag.getInt("FZ"));
            BlockPos pocketOrigin = new BlockPos(
                    entryTag.getInt("PX"), entryTag.getInt("PY"), entryTag.getInt("PZ"));
            registry.entries.put(index, new PocketEntry(
                    index,
                    fountainDim,
                    fountainPos,
                    pocketOrigin));
        }
        return registry;
    }

    public static PocketRegistry get(ServerLevel overworld) {
        return overworld.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(PocketRegistry::new, PocketRegistry::load, DataFixTypes.LEVEL),
                DATA_NAME);
    }

    public int getOrCreateIndex(ResourceKey<Level> fountainDimension, BlockPos fountainPos) {
        for (Map.Entry<Integer, PocketEntry> entry : entries.entrySet()) {
            PocketEntry value = entry.getValue();
            if (value.fountainDimension().equals(fountainDimension)
                    && value.fountainPos().equals(fountainPos)) {
                return entry.getKey();
            }
        }
        int index = nextIndex++;
        entries.put(index, new PocketEntry(
                index,
                fountainDimension,
                fountainPos.immutable(),
                new BlockPos(index * SPACING, 0, 0).immutable()));
        setDirty();
        return index;
    }

    public Optional<PocketEntry> get(int index) {
        return Optional.ofNullable(entries.get(index));
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("NextIndex", nextIndex);
        ListTag list = new ListTag();
        for (Map.Entry<Integer, PocketEntry> entry : entries.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            PocketEntry value = entry.getValue();
            entryTag.putInt("Index", entry.getKey());
            entryTag.putString("FountainDimension", value.fountainDimension().location().toString());
            entryTag.putInt("FX", value.fountainPos().getX());
            entryTag.putInt("FY", value.fountainPos().getY());
            entryTag.putInt("FZ", value.fountainPos().getZ());
            entryTag.putInt("PX", value.pocketOrigin().getX());
            entryTag.putInt("PY", value.pocketOrigin().getY());
            entryTag.putInt("PZ", value.pocketOrigin().getZ());
            list.add(entryTag);
        }
        tag.put("Entries", list);
        return tag;
    }
}
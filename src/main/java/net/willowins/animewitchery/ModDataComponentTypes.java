package net.willowins.animewitchery;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModDataComponentTypes {
    // FIX: Replaced NeoForgeRegistries.DATA_COMPONENT_TYPES with Registries.DATA_COMPONENT_TYPE
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents("animewitchery");

    // Registers a simple integer component for stored mana
    public static final Supplier<DataComponentType<Integer>> STORED_MANA = DATA_COMPONENT_TYPES.register(
            "stored_mana",
            () -> DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    // Stores the position of the altar the wand is currently bound to (for pedestal binding)
    public static final Supplier<DataComponentType<BlockPos>> BOUND_ALTAR = DATA_COMPONENT_TYPES.register(
            "bound_altar",
            () -> DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).build()
    );

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENT_TYPES.register(modEventBus);
    }
}
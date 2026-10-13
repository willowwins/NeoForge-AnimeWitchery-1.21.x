package net.willowins.animewitchery.worldgen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.worldgen.feature.RosewillowMegaTreeFeature;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(BuiltInRegistries.FEATURE, AnimeWitchery.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>>
            ROSEWILLOW_MEGA_TREE = FEATURES.register("rosewillow_mega_tree", RosewillowMegaTreeFeature::new);

    private ModFeatures() {}

    public static void register(net.neoforged.bus.api.IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}

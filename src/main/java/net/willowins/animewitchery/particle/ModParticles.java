package net.willowins.animewitchery.particle;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = 
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, AnimeWitchery.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STARDROP_PARTICLE = 
            PARTICLES.register("stardrop_particle", () -> new SimpleParticleType(false));

    public static void register(IEventBus modEventBus) {
        PARTICLES.register(modEventBus);
    }
}

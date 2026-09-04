package net.willowins.animewitchery.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.particle.ModParticles;
import net.willowins.animewitchery.particle.StardropParticle;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class ClientParticleEvents {
    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.STARDROP_PARTICLE.get(), StardropParticle.Provider::new);
    }
}

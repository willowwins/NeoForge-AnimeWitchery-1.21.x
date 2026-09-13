package net.willowins.animewitchery.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.willowins.animewitchery.AnimeWitchery;

public class ModMessages {
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModMessages::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AnimeWitchery.MOD_ID);
        registrar.playToClient(
                ManaSyncPayload.TYPE,
                ManaSyncPayload.STREAM_CODEC,
                ManaSyncPayload::handle
        );
        registrar.playToServer(
                PocketOptionsRequestPayload.TYPE,
                PocketOptionsRequestPayload.STREAM_CODEC,
                PocketOptionsRequestPayload::handle
        );
        registrar.playToClient(
                PocketOptionsPayload.TYPE,
                PocketOptionsPayload.STREAM_CODEC,
                PocketOptionsPayload::handle
        );
        registrar.playToServer(
                PocketPlacePayload.TYPE,
                PocketPlacePayload.STREAM_CODEC,
                PocketPlacePayload::handle
        );
    }
}

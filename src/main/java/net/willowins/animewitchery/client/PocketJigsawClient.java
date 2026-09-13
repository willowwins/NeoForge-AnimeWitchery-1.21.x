package net.willowins.animewitchery.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.network.PocketOptionsEvent;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class PocketJigsawClient {

    @SubscribeEvent
    public static void onOptionsAvailable(PocketOptionsEvent event) {
        Minecraft.getInstance().setScreen(new PocketJigsawScreen(event.getPos(), event.getNames()));
    }
}
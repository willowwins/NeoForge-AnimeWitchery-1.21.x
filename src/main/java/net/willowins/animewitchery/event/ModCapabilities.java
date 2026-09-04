package net.willowins.animewitchery.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.willowins.animewitchery.AnimeWitchery; // Replace with your main mod class
import net.willowins.animewitchery.block.entity.ModBlockEntities;

@EventBusSubscriber(modid = "animewitchery", bus = EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.STARLIGHT_POOL_BE.get(), // Replace with your DeferredRegister reference
                (be, side) -> be.fluidTank
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get(),
                (be, side) -> be.fluidTank
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.LUNA_CRYSTAL_BE.get(),
                (be, side) -> be.buffer
        );
    }
}

package net.willowins.animewitchery.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.ModDataComponentTypes;
import net.willowins.animewitchery.block.entity.ModBlockEntities;
import net.willowins.animewitchery.fluid.ModFluids;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.item.custom.StarlightBottleItem;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.STARLIGHT_POOL_BE.get(),
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

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, side) -> new IFluidHandlerItem() {
            @Override public net.minecraft.world.item.ItemStack getContainer() { return stack; }
            @Override public int getTanks() { return 1; }
            @Override public int getTankCapacity(int t) { return StarlightBottleItem.CAPACITY; }
            @Override public boolean isFluidValid(int t, FluidStack s) { return s.getFluid() == ModFluids.STARLIGHT_SOURCE.get(); }

            @Override
            public FluidStack getFluidInTank(int t) {
                int amount = stack.getOrDefault(ModDataComponentTypes.STORED_STARLIGHT.get(), 0);
                if (amount <= 0) return FluidStack.EMPTY;
                return new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), amount);
            }

            @Override
            public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
                if (resource.getFluid() != ModFluids.STARLIGHT_SOURCE.get()) return 0;
                int current = stack.getOrDefault(ModDataComponentTypes.STORED_STARLIGHT.get(), 0);
                int canAccept = Math.min(resource.getAmount(), StarlightBottleItem.CAPACITY - current);
                if (canAccept <= 0) return 0;
                if (action.execute()) {
                    stack.set(ModDataComponentTypes.STORED_STARLIGHT.get(), current + canAccept);
                }
                return canAccept;
            }

            @Override
            public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
                if (resource.getFluid() != ModFluids.STARLIGHT_SOURCE.get()) return FluidStack.EMPTY;
                int current = stack.getOrDefault(ModDataComponentTypes.STORED_STARLIGHT.get(), 0);
                int toDrain = Math.min(resource.getAmount(), current);
                if (toDrain <= 0) return FluidStack.EMPTY;
                if (action.execute()) {
                    stack.set(ModDataComponentTypes.STORED_STARLIGHT.get(), current - toDrain);
                }
                return new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), toDrain);
            }

            @Override
            public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                int current = stack.getOrDefault(ModDataComponentTypes.STORED_STARLIGHT.get(), 0);
                int toDrain = Math.min(maxDrain, current);
                if (toDrain <= 0) return FluidStack.EMPTY;
                if (action.execute()) {
                    stack.set(ModDataComponentTypes.STORED_STARLIGHT.get(), current - toDrain);
                }
                return new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), toDrain);
            }
        }, ModItems.STARLIGHT_BOTTLE.get());
    }
}

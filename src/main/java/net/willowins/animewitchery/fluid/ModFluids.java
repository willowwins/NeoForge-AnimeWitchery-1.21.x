package net.willowins.animewitchery.fluid;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.item.ModItems;

import java.util.function.Consumer;

public class ModFluids {
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, AnimeWitchery.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(Registries.FLUID, AnimeWitchery.MOD_ID);

    public static final DeferredHolder<FluidType, FluidType> STARLIGHT_FLUID_TYPE = FLUID_TYPES.register("starlight",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.starlight")
                    .fallDistanceModifier(0F)
                    .canExtinguish(true)
                    .supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(1000)
                    .viscosity(1000)
                    .lightLevel(15)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/starlight_still");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/starlight_flow");

                        @Override
                        public ResourceLocation getStillTexture() {
                            return STILL;
                        }

                        @Override
                        public ResourceLocation getStillTexture(net.minecraft.world.level.material.FluidState state, net.minecraft.world.level.BlockAndTintGetter level, net.minecraft.core.BlockPos pos) {
                            if (pos != null) {
                                int gx = Math.floorMod(pos.getX(), 3);
                                int gz = Math.floorMod(pos.getZ(), 3);
                                return ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/starlight_still_" + gx + "_" + gz);
                            }
                            return STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {
                            return FLOW;
                        }

                        @Override
                        public int getTintColor() {
                            return 0xFFFFFFFF; // Pure white tint so custom texture colors (#FDECFF, #ECF2FF, #FFFFFF) display true
                        }
                    });
                }
            });

    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> STARLIGHT_SOURCE = FLUIDS.register("starlight",
            () -> new BaseFlowingFluid.Source(ModFluids.STARLIGHT_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> STARLIGHT_FLOWING = FLUIDS.register("flowing_starlight",
            () -> new BaseFlowingFluid.Flowing(ModFluids.STARLIGHT_PROPERTIES));

    public static final BaseFlowingFluid.Properties STARLIGHT_PROPERTIES = new BaseFlowingFluid.Properties(
            STARLIGHT_FLUID_TYPE,
            STARLIGHT_SOURCE,
            STARLIGHT_FLOWING
    ).block(ModBlocks.STARLIGHT_BLOCK).bucket(ModItems.STARLIGHT_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}

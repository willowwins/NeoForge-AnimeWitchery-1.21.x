package net.willowins.animewitchery.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

    // Liquified Skulk
    public static final DeferredHolder<FluidType, FluidType> LIQUIFIED_SKULK_FLUID_TYPE = FLUID_TYPES.register("liquified_skulk",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.liquified_skulk")
                    .fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(1200).viscosity(1500).lightLevel(5)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/liquified_sculk");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/liquified_sculk");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0xFF0D0D0D; }
                    });
                }
            });
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIQUIFIED_SKULK_SOURCE = FLUIDS.register("liquified_skulk",
            () -> new BaseFlowingFluid.Source(ModFluids.LIQUIFIED_SKULK_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> LIQUIFIED_SKULK_FLOWING = FLUIDS.register("flowing_liquified_skulk",
            () -> new BaseFlowingFluid.Flowing(ModFluids.LIQUIFIED_SKULK_PROPERTIES));
    public static final BaseFlowingFluid.Properties LIQUIFIED_SKULK_PROPERTIES = new BaseFlowingFluid.Properties(
            LIQUIFIED_SKULK_FLUID_TYPE, LIQUIFIED_SKULK_SOURCE, LIQUIFIED_SKULK_FLOWING
    ).block(ModBlocks.LIQUIFIED_SKULK_BLOCK).bucket(ModItems.LIQUIFIED_SKULK_BUCKET);

    // Liquid Experience
    public static final DeferredHolder<FluidType, FluidType> LIQUID_EXPERIENCE_FLUID_TYPE = FLUID_TYPES.register("liquid_experience",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.liquid_experience")
                    .fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(800).viscosity(600).lightLevel(10)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/liquid_experience_still");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/liquid_experience_flow");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0xFF7FFF00; }
                    });
                }
            });
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> LIQUID_EXPERIENCE_SOURCE = FLUIDS.register("liquid_experience",
            () -> new BaseFlowingFluid.Source(ModFluids.LIQUID_EXPERIENCE_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> LIQUID_EXPERIENCE_FLOWING = FLUIDS.register("flowing_liquid_experience",
            () -> new BaseFlowingFluid.Flowing(ModFluids.LIQUID_EXPERIENCE_PROPERTIES));
    public static final BaseFlowingFluid.Properties LIQUID_EXPERIENCE_PROPERTIES = new BaseFlowingFluid.Properties(
            LIQUID_EXPERIENCE_FLUID_TYPE, LIQUID_EXPERIENCE_SOURCE, LIQUID_EXPERIENCE_FLOWING
    ).block(ModBlocks.LIQUID_EXPERIENCE_BLOCK).bucket(ModItems.LIQUID_EXPERIENCE_BUCKET);

    // Obsidian Tears
    public static final DeferredHolder<FluidType, FluidType> OBSIDIAN_TEARS_FLUID_TYPE = FLUID_TYPES.register("obsidian_tears",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.obsidian_tears")
                    .fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(1500).viscosity(2000).lightLevel(3)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/obsidian_tears_still");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "block/obsidian_tears_flow");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0xFF1A0A2E; }
                    });
                }
            });
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> OBSIDIAN_TEARS_SOURCE = FLUIDS.register("obsidian_tears",
            () -> new BaseFlowingFluid.Source(ModFluids.OBSIDIAN_TEARS_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> OBSIDIAN_TEARS_FLOWING = FLUIDS.register("flowing_obsidian_tears",
            () -> new BaseFlowingFluid.Flowing(ModFluids.OBSIDIAN_TEARS_PROPERTIES));
    public static final BaseFlowingFluid.Properties OBSIDIAN_TEARS_PROPERTIES = new BaseFlowingFluid.Properties(
            OBSIDIAN_TEARS_FLUID_TYPE, OBSIDIAN_TEARS_SOURCE, OBSIDIAN_TEARS_FLOWING
    ).block(ModBlocks.OBSIDIAN_TEARS_BLOCK).bucket(ModItems.OBSIDIAN_TEARS_BUCKET);

    // Blood
    public static final DeferredHolder<FluidType, FluidType> BLOOD_FLUID_TYPE = FLUID_TYPES.register("blood",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.blood")
                    .fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(1100).viscosity(1300).lightLevel(0)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0xAAFF0000; }
                    });
                }
            });
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> BLOOD_SOURCE = FLUIDS.register("blood",
            () -> new BaseFlowingFluid.Source(ModFluids.BLOOD_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> BLOOD_FLOWING = FLUIDS.register("flowing_blood",
            () -> new BaseFlowingFluid.Flowing(ModFluids.BLOOD_PROPERTIES));
    public static final BaseFlowingFluid.Properties BLOOD_PROPERTIES = new BaseFlowingFluid.Properties(
            BLOOD_FLUID_TYPE, BLOOD_SOURCE, BLOOD_FLOWING
    ).block(ModBlocks.BLOOD_BLOCK).bucket(ModItems.BLOOD_BUCKET);

    // Antifluid
    public static final DeferredHolder<FluidType, FluidType> ANTIFLUID_FLUID_TYPE = FLUID_TYPES.register("antifluid",
            () -> new FluidType(FluidType.Properties.create()
                    .descriptionId("block.animewitchery.antifluid")
                    .fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .density(-1000).viscosity(500).lightLevel(12)
            ) {
                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still");
                        private static final ResourceLocation FLOW = ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow");
                        @Override public ResourceLocation getStillTexture() { return STILL; }
                        @Override public ResourceLocation getFlowingTexture() { return FLOW; }
                        @Override public int getTintColor() { return 0x00000000; }
                    });
                }
            });
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Source> ANTIFLUID_SOURCE = FLUIDS.register("antifluid",
            () -> new BaseFlowingFluid.Source(ModFluids.ANTIFLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> ANTIFLUID_FLOWING = FLUIDS.register("flowing_antifluid",
            () -> new BaseFlowingFluid.Flowing(ModFluids.ANTIFLUID_PROPERTIES));
    public static final BaseFlowingFluid.Properties ANTIFLUID_PROPERTIES = new BaseFlowingFluid.Properties(
            ANTIFLUID_FLUID_TYPE, ANTIFLUID_SOURCE, ANTIFLUID_FLOWING
    ).block(ModBlocks.ANTIFLUID_BLOCK).bucket(ModItems.ANTIFLUID_BUCKET);

    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
        FLUIDS.register(eventBus);
    }
}

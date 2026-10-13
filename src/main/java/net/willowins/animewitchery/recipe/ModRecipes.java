package net.willowins.animewitchery.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;

import java.util.function.Supplier;

public class ModRecipes {
    // FIX: Explicitly use the vanilla Registries keys so NeoForge maps them cleanly
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, AnimeWitchery.MOD_ID);

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, AnimeWitchery.MOD_ID);

    public static final Supplier<RecipeSerializer<CatalystBlockRecipe.PackAlchemical>> PACK_ALCHEMICAL_CATALYSTS =
            RECIPE_SERIALIZERS.register("alchemical_catalyst_block", () -> new SimpleCraftingRecipeSerializer<>(CatalystBlockRecipe.PackAlchemical::new));
    public static final Supplier<RecipeSerializer<CatalystBlockRecipe.PackResonant>> PACK_RESONANT_CATALYSTS =
            RECIPE_SERIALIZERS.register("resonant_catalyst_block", () -> new SimpleCraftingRecipeSerializer<>(CatalystBlockRecipe.PackResonant::new));
    public static final Supplier<RecipeSerializer<CatalystBlockRecipe.UnpackAlchemical>> UNPACK_ALCHEMICAL_CATALYST =
            RECIPE_SERIALIZERS.register("decompile_alchemical_catalyst", () -> new SimpleCraftingRecipeSerializer<>(CatalystBlockRecipe.UnpackAlchemical::new));
    public static final Supplier<RecipeSerializer<CatalystBlockRecipe.UnpackResonant>> UNPACK_RESONANT_CATALYST =
            RECIPE_SERIALIZERS.register("decompile_resonant_catalyst", () -> new SimpleCraftingRecipeSerializer<>(CatalystBlockRecipe.UnpackResonant::new));

    public static final Supplier<RecipeType<AlchemyRecipe>> ALCHEMY_TYPE =
            RECIPE_TYPES.register("alchemy", () -> new RecipeType<>() {
                @Override public String toString() { return "alchemy"; }
            });

    public static final Supplier<RecipeSerializer<AlchemyRecipe>> ALCHEMY_SERIALIZER =
            RECIPE_SERIALIZERS.register("alchemy", () -> new RecipeSerializer<>() {
                @Override public com.mojang.serialization.MapCodec<AlchemyRecipe> codec() {
                    return AlchemyRecipe.CODEC;
                }
                @Override public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, AlchemyRecipe> streamCodec() {
                    return AlchemyRecipe.STREAM_CODEC;
                }
            });

    // This registers the 'animewitchery:blocktransmutation' type identifier
    public static final Supplier<RecipeType<BlockTransmutationRecipe>> BLOCK_TRANSMUTATION_TYPE =
            RECIPE_TYPES.register("blocktransmutation", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "blocktransmutation";
                }
            });

    public static final Supplier<RecipeSerializer<BlockTransmutationRecipe>> BLOCK_TRANSMUTATION_SERIALIZER =
            RECIPE_SERIALIZERS.register("blocktransmutation",
                    () -> new RecipeSerializer<BlockTransmutationRecipe>() {
                        @Override
                        public com.mojang.serialization.MapCodec<BlockTransmutationRecipe> codec() {
                            return BlockTransmutationRecipe.CODEC;
                        }
                        @Override
                        public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, BlockTransmutationRecipe> streamCodec() {
                            return BlockTransmutationRecipe.STREAM_CODEC;
                        }
                    });

    public static final Supplier<RecipeType<StarlightInfusionRecipe>> STARLIGHT_INFUSION_TYPE =
            RECIPE_TYPES.register("starlight_infusion", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return "starlight_infusion";
                }
            });

    public static final Supplier<RecipeSerializer<StarlightInfusionRecipe>> STARLIGHT_INFUSION_SERIALIZER =
            RECIPE_SERIALIZERS.register("starlight_infusion",
                    () -> new RecipeSerializer<StarlightInfusionRecipe>() {
                        @Override
                        public com.mojang.serialization.MapCodec<StarlightInfusionRecipe> codec() {
                            return StarlightInfusionRecipe.CODEC;
                        }
                        @Override
                        public net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, StarlightInfusionRecipe> streamCodec() {
                            return StarlightInfusionRecipe.STREAM_CODEC;
                        }
                    });

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}

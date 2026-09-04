package net.willowins.animewitchery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record StarlightInfusionRecipe(
        Optional<Ingredient> base,
        List<Ingredient> ingredients,
        ItemStack result,
        int fluidCost,
        int duration,
        Optional<FluidIngredient> fluid
) implements Recipe<RecipeInput> {

    public static final MapCodec<StarlightInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Ingredient.CODEC.optionalFieldOf("base").forGetter(StarlightInfusionRecipe::base),
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(StarlightInfusionRecipe::ingredients),
                    ItemStack.CODEC.fieldOf("result").forGetter(StarlightInfusionRecipe::result),
                    Codec.INT.optionalFieldOf("fluidCost", 1000).forGetter(StarlightInfusionRecipe::fluidCost),
                    Codec.INT.optionalFieldOf("duration", 200).forGetter(StarlightInfusionRecipe::duration),
                    FluidIngredient.CODEC.optionalFieldOf("fluid").forGetter(StarlightInfusionRecipe::fluid)
            ).apply(instance, StarlightInfusionRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, StarlightInfusionRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).encode(buf, recipe.base());
                buf.writeVarInt(recipe.ingredients().size());
                for (Ingredient ingredient : recipe.ingredients()) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                }
                ItemStack.STREAM_CODEC.encode(buf, recipe.result());
                buf.writeInt(recipe.fluidCost());
                buf.writeInt(recipe.duration());
                ByteBufCodecs.optional(FluidIngredient.STREAM_CODEC).encode(buf, recipe.fluid());
            },
            buf -> {
                Optional<Ingredient> base = ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).decode(buf);
                int count = buf.readVarInt();
                List<Ingredient> ingredients = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                }
                return new StarlightInfusionRecipe(
                        base,
                        ingredients,
                        ItemStack.STREAM_CODEC.decode(buf),
                        buf.readInt(),
                        buf.readInt(),
                        ByteBufCodecs.optional(FluidIngredient.STREAM_CODEC).decode(buf)
                );
            }
    );

    public boolean matchesIngredients(List<ItemStack> inputs) {
        if (inputs.size() != this.ingredients.size()) {
            return false;
        }
        List<ItemStack> remaining = new ArrayList<>(inputs);
        for (Ingredient ingredient : this.ingredients) {
            ItemStack match = null;
            for (ItemStack stack : remaining) {
                if (ingredient.test(stack)) {
                    match = stack;
                    break;
                }
            }
            if (match == null) {
                return false;
            }
            remaining.remove(match);
        }
        return true;
    }

    public boolean matchesBase(ItemStack stack) {
        return this.base.isEmpty() || this.base.get().test(stack);
    }

    public boolean matchesFluid(FluidStack tankFluid) {
        if (this.fluid.isEmpty()) {
            return true;
        }
        if (tankFluid.isEmpty()) {
            return false;
        }
        return this.fluid.get().test(tankFluid);
    }

    @Override public boolean matches(RecipeInput input, Level level) { return false; }
    @Override public ItemStack assemble(RecipeInput input, net.minecraft.core.HolderLookup.Provider provider) { return this.result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider provider) { return this.result; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.STARLIGHT_INFUSION_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return ModRecipes.STARLIGHT_INFUSION_TYPE.get(); }
}

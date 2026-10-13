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

import java.util.ArrayList;
import java.util.List;

/** Recipe data for the alchemy-table recipes ported from the Fabric edition. */
public record AlchemyRecipe(List<Ingredient> ingredients, ItemStack result, int xpCost, int processingTime)
        implements Recipe<RecipeInput> {

    public static final MapCodec<AlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(AlchemyRecipe::ingredients),
            ItemStack.CODEC.fieldOf("result").forGetter(AlchemyRecipe::result),
            Codec.INT.optionalFieldOf("xp_cost", 0).forGetter(AlchemyRecipe::xpCost),
            Codec.INT.optionalFieldOf("processing_time", 200).forGetter(AlchemyRecipe::processingTime)
    ).apply(instance, AlchemyRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AlchemyRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                buf.writeVarInt(recipe.ingredients.size());
                for (Ingredient ingredient : recipe.ingredients) {
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
                }
                ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                buf.writeVarInt(recipe.xpCost);
                buf.writeVarInt(recipe.processingTime);
            },
            buf -> {
                int count = buf.readVarInt();
                List<Ingredient> ingredients = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    ingredients.add(Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                }
                return new AlchemyRecipe(ingredients, ItemStack.STREAM_CODEC.decode(buf),
                        buf.readVarInt(), buf.readVarInt());
            }
    );

    public boolean matchesIngredients(List<ItemStack> inputStacks) {
        List<ItemStack> remaining = new ArrayList<>(inputStacks);
        for (Ingredient ingredient : ingredients) {
            int matchIndex = -1;
            for (int i = 0; i < remaining.size(); i++) {
                if (ingredient.test(remaining.get(i))) {
                    matchIndex = i;
                    break;
                }
            }
            if (matchIndex < 0) return false;
            remaining.remove(matchIndex);
        }
        return true;
    }

    @Override public boolean matches(RecipeInput input, Level level) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) stacks.add(stack);
        }
        return matchesIngredients(stacks);
    }

    @Override public ItemStack assemble(RecipeInput input, net.minecraft.core.HolderLookup.Provider registries) {
        return result.copy();
    }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) { return result; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.ALCHEMY_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return ModRecipes.ALCHEMY_TYPE.get(); }
}

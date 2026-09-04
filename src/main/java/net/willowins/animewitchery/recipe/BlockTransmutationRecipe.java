package net.willowins.animewitchery.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;  // <-- Ensure this is imported!
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public record BlockTransmutationRecipe(
        Block block,
        Ingredient mainhand,
        Optional<Ingredient> offhand, // Optional so it can be omitted or empty in JSON
        ItemStack result
) implements Recipe<RecipeInput> {

    // Codec for parsing JSON data
    public static final MapCodec<BlockTransmutationRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(BlockTransmutationRecipe::block),
                    Ingredient.CODEC.fieldOf("mainhand").forGetter(BlockTransmutationRecipe::mainhand),
                    Ingredient.CODEC.optionalFieldOf("offhand").forGetter(BlockTransmutationRecipe::offhand),
                    ItemStack.CODEC.fieldOf("result").forGetter(BlockTransmutationRecipe::result)
            ).apply(instance, BlockTransmutationRecipe::new)
    );

    // StreamCodec for syncing recipe data to the client over networks
    // FIX: Replaced StreamCodec.optional(...) with ByteBufCodecs.optional(...)
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockTransmutationRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ByteBufCodecs.registry(Registries.BLOCK).encode(buf, recipe.block());
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.mainhand());
                ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).encode(buf, recipe.offhand());
                ItemStack.STREAM_CODEC.encode(buf, recipe.result());
            },
            buf -> new BlockTransmutationRecipe(
                    ByteBufCodecs.registry(Registries.BLOCK).decode(buf),
                    Ingredient.CONTENTS_STREAM_CODEC.decode(buf),
                    ByteBufCodecs.optional(Ingredient.CONTENTS_STREAM_CODEC).decode(buf),
                    ItemStack.STREAM_CODEC.decode(buf)
            )
    );

    // Helper match method called by your event loop
    public boolean matches(Block targetBlock, ItemStack mainHandStack, ItemStack offHandStack) {
        if (!targetBlock.equals(this.block)) return false;
        if (!this.mainhand.test(mainHandStack)) return false;

        // FIX: If the recipe requires an offhand item, make sure the player's offhand isn't empty first!
        if (this.offhand.isPresent()) {
            if (offHandStack.isEmpty()) return false; // Prevents passing an empty stack to the ingredient tester
            return this.offhand.get().test(offHandStack);
        }

        return true;
    }


    @Override public boolean matches(RecipeInput input, Level level) { return false; }
    @Override public ItemStack assemble(RecipeInput input, net.minecraft.core.HolderLookup.Provider provider) { return this.result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return true; }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider provider) { return this.result; }
    @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.BLOCK_TRANSMUTATION_SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return ModRecipes.BLOCK_TRANSMUTATION_TYPE.get(); }
}

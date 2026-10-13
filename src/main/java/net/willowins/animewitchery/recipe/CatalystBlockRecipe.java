package net.willowins.animewitchery.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.ModDataComponentTypes;

/** Crafting and uncrafting recipes for the two catalyst storage blocks. */
public abstract class CatalystBlockRecipe extends CustomRecipe {
    private CatalystBlockRecipe(CraftingBookCategory category) { super(category); }

    public static class PackAlchemical extends CatalystBlockRecipe {
        public PackAlchemical(CraftingBookCategory category) { super(category); }
        @Override public boolean matches(CraftingInput input, Level level) {
            if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9) return false;
            for (int i = 0; i < input.size(); i++) {
                ItemStack stack = input.getItem(i);
                if (!stack.is(ModItems.ALCHEMICAL_CATALYST.get()) || storedMana(stack) != 0) return false;
            }
            return true;
        }
        @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return new ItemStack(ModBlocks.ALCHEMICAL_CATALYST_BLOCK.get()); }
        @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.PACK_ALCHEMICAL_CATALYSTS.get(); }
    }

    public static class PackResonant extends CatalystBlockRecipe {
        public PackResonant(CraftingBookCategory category) { super(category); }
        @Override public boolean matches(CraftingInput input, Level level) {
            if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9) return false;
            for (int i = 0; i < input.size(); i++) {
                ItemStack stack = input.getItem(i);
                if (!stack.is(ModItems.RESONANT_CATALYST.get()) || storedMana(stack) != 0) return false;
            }
            return true;
        }
        @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return new ItemStack(ModBlocks.RESONANT_CATALYST_BLOCK.get()); }
        @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.PACK_RESONANT_CATALYSTS.get(); }
    }

    public static class UnpackAlchemical extends CatalystBlockRecipe {
        public UnpackAlchemical(CraftingBookCategory category) { super(category); }
        @Override public boolean matches(CraftingInput input, Level level) { return singleBlock(input, ModBlocks.ALCHEMICAL_CATALYST_BLOCK.asItem()); }
        @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return new ItemStack(ModItems.ALCHEMICAL_CATALYST.get(), 9); }
        @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.UNPACK_ALCHEMICAL_CATALYST.get(); }
    }

    public static class UnpackResonant extends CatalystBlockRecipe {
        public UnpackResonant(CraftingBookCategory category) { super(category); }
        @Override public boolean matches(CraftingInput input, Level level) { return singleBlock(input, ModBlocks.RESONANT_CATALYST_BLOCK.asItem()); }
        @Override public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) { return new ItemStack(ModItems.RESONANT_CATALYST.get(), 9); }
        @Override public RecipeSerializer<?> getSerializer() { return ModRecipes.UNPACK_RESONANT_CATALYST.get(); }
    }

    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public boolean isSpecial() { return true; }

    private static int storedMana(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
    }

    private static boolean singleBlock(CraftingInput input, net.minecraft.world.item.Item blockItem) {
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (!stack.is(blockItem) || ++count > 1) return false;
        }
        return count == 1;
    }
}

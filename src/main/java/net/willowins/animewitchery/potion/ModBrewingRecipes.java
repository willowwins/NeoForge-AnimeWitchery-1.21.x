package net.willowins.animewitchery.potion;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.willowins.animewitchery.item.ModItems;

public final class ModBrewingRecipes {
    private ModBrewingRecipes() {
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, ModItems.ROSEWILLOW_BLOSSOM.get(), ModPotions.MANA_REGEN);

        builder.addMix(ModPotions.MANA_REGEN, Items.REDSTONE, ModPotions.LONG_MANA_REGEN);

        builder.addMix(ModPotions.MANA_REGEN, Items.GLOWSTONE_DUST, ModPotions.STRONG_MANA_REGEN);
    }
}
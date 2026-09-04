package net.willowins.animewitchery.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class ModFoodProperties {
    public static final FoodProperties STARLIGHT_BOTTLE = new FoodProperties.Builder().nutrition(5).saturationModifier(1f).alwaysEdible()
            .usingConvertsTo(Items.GLASS_BOTTLE).effect(()-> new MobEffectInstance(MobEffects.NIGHT_VISION,2400),1).build();


}

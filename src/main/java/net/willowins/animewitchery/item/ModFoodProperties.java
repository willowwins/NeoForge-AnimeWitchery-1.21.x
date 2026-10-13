package net.willowins.animewitchery.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import net.willowins.animewitchery.effect.ModEffects;

public class ModFoodProperties {
    public static final FoodProperties STARLIGHT_BOTTLE = new FoodProperties.Builder().nutrition(5).saturationModifier(1f).alwaysEdible()
            .usingConvertsTo(Items.GLASS_BOTTLE).effect(()-> new MobEffectInstance(MobEffects.NIGHT_VISION,2400),1).build();

    public static final FoodProperties LEMON = new FoodProperties.Builder().nutrition(3).saturationModifier(1f).build();
    public static final FoodProperties STRAWBERRY = new FoodProperties.Builder().nutrition(3).saturationModifier(1f).build();
    public static final FoodProperties TART_CRUST = new FoodProperties.Builder().nutrition(3).saturationModifier(1f).build();
    public static final FoodProperties UNBAKED_TART = new FoodProperties.Builder().nutrition(3).saturationModifier(1f).build();
    public static final FoodProperties LEMON_TART = new FoodProperties.Builder().nutrition(8).saturationModifier(1f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1000), 1).build();
    public static final FoodProperties STRAWBERRY_TART = new FoodProperties.Builder().nutrition(8).saturationModifier(1f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1000), 1).build();

    public static final FoodProperties ROSEWILLOW_BLOSSOM = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(0.2f)
            .effect(() -> new MobEffectInstance(ModEffects.MANA_REGEN, 600, 0), 1).build();

}

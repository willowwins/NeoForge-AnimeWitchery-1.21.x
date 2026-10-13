package net.willowins.animewitchery.potion;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.effect.ModEffects;

public final class ModPotions {
    private ModPotions() {
    }

    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, "animewitchery");

    public static final DeferredHolder<Potion, Potion> MANA_REGEN = POTIONS.register("mana_regen",
            () -> new Potion(new MobEffectInstance(ModEffects.MANA_REGEN, 900, 0)));

    public static final DeferredHolder<Potion, Potion> LONG_MANA_REGEN = POTIONS.register("long_mana_regen",
            () -> new Potion(new MobEffectInstance(ModEffects.MANA_REGEN, 1800, 0)));

    public static final DeferredHolder<Potion, Potion> STRONG_MANA_REGEN = POTIONS.register("strong_mana_regen",
            () -> new Potion(new MobEffectInstance(ModEffects.MANA_REGEN, 450, 1)));

    public static void register(IEventBus modEventBus) {
        POTIONS.register(modEventBus);
    }
}
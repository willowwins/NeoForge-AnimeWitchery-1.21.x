package net.willowins.animewitchery.effect;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEffects {
    private ModEffects() {
    }

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, "animewitchery");

    public static final DeferredHolder<MobEffect, MobEffect> REVEALED =
            MOB_EFFECTS.register("revealed", () -> new RevealedEffect());
    public static final DeferredHolder<MobEffect, MobEffect> BLEEDING =
            MOB_EFFECTS.register("bleeding", () -> new BleedingEffect());
    public static final DeferredHolder<MobEffect, MobEffect> BOUND =
            MOB_EFFECTS.register("bound", () -> new BoundEffect());
    public static final DeferredHolder<MobEffect, MobEffect> MARKED =
            MOB_EFFECTS.register("marked", () -> new MarkedEffect());
    public static final DeferredHolder<MobEffect, MobEffect> VOID_BOUND =
            MOB_EFFECTS.register("void_bound", () -> new VoidBoundEffect());
    public static final DeferredHolder<MobEffect, MobEffect> KAMIKAZE_RITUAL =
            MOB_EFFECTS.register("kamikaze_ritual", () -> new KamikazeRitualEffect());
    public static final DeferredHolder<MobEffect, MobEffect> MANA_REGEN =
            MOB_EFFECTS.register("mana_regen", () -> new ManaRegenEffect());
    public static final DeferredHolder<MobEffect, MobEffect> PACIFISM =
            MOB_EFFECTS.register("pacifism", () -> new PacifismEffect());

    public static void register(IEventBus modEventBus) {
        MOB_EFFECTS.register(modEventBus);
    }
}
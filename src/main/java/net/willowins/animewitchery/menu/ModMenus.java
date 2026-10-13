package net.willowins.animewitchery.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.willowins.animewitchery.AnimeWitchery;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, AnimeWitchery.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<AlchemyTableMenu>> ALCHEMY_TABLE =
            MENUS.register("alchemy_table", () -> IMenuTypeExtension.create(AlchemyTableMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<SoulJarMenu>> SOUL_JAR =
            MENUS.register("soul_jar", () -> IMenuTypeExtension.create(SoulJarMenu::new));

    private ModMenus() {}
    public static void register(IEventBus bus) { MENUS.register(bus); }
}

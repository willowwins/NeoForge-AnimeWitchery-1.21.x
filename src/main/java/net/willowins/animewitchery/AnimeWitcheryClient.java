package net.willowins.animewitchery;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.willowins.animewitchery.client.screen.AlchemyTableScreen;
import net.willowins.animewitchery.menu.ModMenus;
import net.willowins.animewitchery.client.screen.SoulJarScreen;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AnimeWitchery.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class AnimeWitcheryClient {
    public AnimeWitcheryClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.ALCHEMY_TABLE.get(), AlchemyTableScreen::new);
        event.register(ModMenus.SOUL_JAR.get(), SoulJarScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        AnimeWitchery.LOGGER.info("HELLO FROM CLIENT SETUP");
        AnimeWitchery.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        event.enqueueWork(() -> {
            ItemProperties.register(net.willowins.animewitchery.item.ModItems.WEATHERITEM.get(),
                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "mode"),
                    (stack, level, entity, seed) -> net.willowins.animewitchery.item.custom.WeatherItem.getMode(stack));
            // Render the HUD baubles on the head (using the item model's "head" display transform)
            // when equipped in a curio slot
            top.theillusivec4.curios.api.client.CuriosRendererRegistry.register(
                    net.willowins.animewitchery.item.ModItems.HUD.get(),
                    net.willowins.animewitchery.client.renderer.HudRenderer::new
            );
            top.theillusivec4.curios.api.client.CuriosRendererRegistry.register(
                    net.willowins.animewitchery.item.ModItems.HUD_ALT_1.get(),
                    net.willowins.animewitchery.client.renderer.HudRenderer::new
            );

        });
    }

    @SubscribeEvent
    public static void onAddLayers(net.neoforged.neoforge.client.event.EntityRenderersEvent.AddLayers event) {
        net.minecraft.client.renderer.entity.player.PlayerRenderer wide =
                event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.WIDE);
        net.minecraft.client.renderer.entity.player.PlayerRenderer slim =
                event.getSkin(net.minecraft.client.resources.PlayerSkin.Model.SLIM);
        if (wide != null) {
            wide.addLayer(new net.willowins.animewitchery.client.render.FlightElytraRenderLayer(wide));
        }
        if (slim != null) {
            slim.addLayer(new net.willowins.animewitchery.client.render.FlightElytraRenderLayer(slim));
        }
    }
}

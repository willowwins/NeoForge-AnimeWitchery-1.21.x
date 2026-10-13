package net.willowins.animewitchery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.item.ModItems;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT)
public class HudOverlayHandler {
    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            handler.findFirstCurio(stack -> stack.is(ModItems.HUD.get()) || stack.is(ModItems.HUD_ALT_1.get()))
                .ifPresent(found -> {
                    renderHudOverlay(event.getGuiGraphics(), mc, found.stack());
                });
        });
    }
    
    private static void renderHudOverlay(GuiGraphics guiGraphics, Minecraft mc, ItemStack stack) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        
        guiGraphics.drawString(mc.font, "HUD Active", width / 2 - 30, height - 30, 0xFFFFFFFF, true);
        
        if (stack.is(ModItems.HUD_ALT_1.get())) {
            guiGraphics.drawString(mc.font, "Alt Mode", width / 2 - 25, height - 20, 0xFF00FF00, true);
        }
    }
}
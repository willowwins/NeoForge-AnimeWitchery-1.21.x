package net.willowins.animewitchery.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.willowins.animewitchery.item.ModItems;
import top.theillusivec4.curios.api.CuriosApi;

public class ManaOverlay {

    private static boolean isHudBauble(ItemStack stack) {
        return stack.is(ModItems.HUD.get()) || stack.is(ModItems.HUD_ALT_1.get());
    }

    private static boolean hasHudEquipped(Player player) {
        if (isHudBauble(player.getItemBySlot(EquipmentSlot.HEAD))) {
            return true;
        }
        return CuriosApi.getCuriosInventory(player)
                .flatMap(handler -> handler.getStacksHandler("head"))
                .map(handler -> {
                    for (int i = 0; i < handler.getSlots(); i++) {
                        if (isHudBauble(handler.getStacks().getStackInSlot(i))) {
                            return true;
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }

        // Only show the mana HUD when the HUD bauble is equipped to the head curio slot
        // or the helmet slot (not while just carried in the inventory)
        if (!hasHudEquipped(player)) {
            return;
        }

        int currentMana = net.willowins.animewitchery.mana.PlayerMana.getMana(player);
        int maxMana = net.willowins.animewitchery.mana.PlayerMana.getMaxMana(player);

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        // Position in the bottom-left corner
        int barWidth = 100;
        int barHeight = 7;
        int x = 12;
        int y = screenHeight - 18;

        // 1. Draw outer dark background frame
        guiGraphics.fill(x - 2, y - 2, x + barWidth + 2, y + barHeight + 2, 0xAA000000);
        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y + barHeight + 1, 0xFF18122B);

        // 2. Draw progress fill bar (magic purple: 0xFF9933FF)
        float progress = Mth.clamp((float) currentMana / Math.max(1, maxMana), 0.0F, 1.0F);
        int fillWidth = Math.round(progress * barWidth);
        if (fillWidth > 0) {
            guiGraphics.fill(x, y, x + fillWidth, y + barHeight, 0xFF9933FF);
            // Highlight shine line at top edge of fill (light purple: 0xFFD8B4FE)
            guiGraphics.fill(x, y, x + fillWidth, y + 2, 0xFFD8B4FE);
        }

        // 3. Draw text label right above the bar in the bottom-left
        Font font = mc.font;
        String text = "Mana: " + currentMana + " / " + maxMana;
        int textX = x;
        int textY = y - 10;
        guiGraphics.drawString(font, text, textX, textY, 0xFFC084FC, true);
    }
}

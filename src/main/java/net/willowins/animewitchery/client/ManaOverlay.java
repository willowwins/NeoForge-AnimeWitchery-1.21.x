package net.willowins.animewitchery.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.item.ModItems;

public class ManaOverlay {

    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.options.hideGui) {
            return;
        }

        // Check if player has Alchemical Catalyst or Resonant Catalyst in inventory or offhand
        boolean hasCatalyst = false;
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && (stack.is(ModItems.ALCHEMICAL_CATALYST.get()) || stack.is(ModItems.RESONANT_CATALYST.get()))) {
                hasCatalyst = true;
                break;
            }
        }
        if (!hasCatalyst) {
            for (ItemStack stack : player.getInventory().offhand) {
                if (!stack.isEmpty() && (stack.is(ModItems.ALCHEMICAL_CATALYST.get()) || stack.is(ModItems.RESONANT_CATALYST.get()))) {
                    hasCatalyst = true;
                    break;
                }
            }
        }

        if (!hasCatalyst) {
            return;
        }

        ManaData manaData = player.getData(ModAttachments.MANA);
        int currentMana = manaData.current();
        int maxMana = manaData.max();

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

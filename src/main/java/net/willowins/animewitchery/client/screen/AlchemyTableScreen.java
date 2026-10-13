package net.willowins.animewitchery.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.menu.AlchemyTableMenu;

public class AlchemyTableScreen extends AbstractContainerScreen<AlchemyTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            AnimeWitchery.MOD_ID, "textures/gui/alchemy_table_gui.png");

    public AlchemyTableScreen(AlchemyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 252;
        inventoryLabelY = imageHeight - 94;
    }

    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        if (menu.isActivated() && menu.getMaxProgress() > 0) {
            int progressWidth = Math.min(24, 24 * menu.getProgress() / menu.getMaxProgress());
            graphics.fill(x + 79, y + 34, x + 79 + progressWidth, y + 51, 0xFF4CAF50);
        }
        if (menu.isActivated()) graphics.fill(x + 79, y + 15, x + 97, y + 33, 0x80FFD700);
    }

    @Override protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        if (menu.getCurrentRecipeXpCost() > 0) {
            graphics.drawString(font, Component.translatable("gui.animewitchery.alchemy_xp_cost",
                    menu.getCurrentRecipeXpCost()), 8, 6, 0xFFFFFF, true);
        }
    }
}

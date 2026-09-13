package net.willowins.animewitchery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.network.PocketPlacePayload;

import java.util.List;

public class PocketJigsawScreen extends Screen {

    private final BlockPos pos;
    private final List<String> options;

    public PocketJigsawScreen(BlockPos pos, List<String> options) {
        super(Component.literal("Pocket Expansion"));
        this.pos = pos;
        this.options = options;
    }

    @Override
    protected void init() {
        int y = this.height / 2 - this.options.size() * 12 - 8;
        for (int i = 0; i < this.options.size(); i++) {
            String name = this.options.get(i);
            int buttonY = y + i * 22;
            final int index = i;
            this.addRenderableWidget(Button.builder(Component.literal(shortName(name)), btn -> {
                        PacketDistributor.sendToServer(new PocketPlacePayload(this.pos, index));
                        Minecraft.getInstance().setScreen(null);
                    })
                    .bounds(this.width / 2 - 100, buttonY, 200, 20)
                    .build());
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
        if (this.options.isEmpty()) {
            graphics.drawCenteredString(this.font, Component.literal("No pocket pieces available yet."), this.width / 2, this.height / 2, 0xAAAAAA);
        }
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static String shortName(String name) {
        int slash = name.indexOf('/');
        return slash >= 0 ? name.substring(slash + 1) : name;
    }
}
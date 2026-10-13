package net.willowins.animewitchery.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class AbstractInventoryScreenMixin {

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void cancelDescriptionsToFixPortingLibCrash(GuiGraphics guiGraphics, int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
    }
}

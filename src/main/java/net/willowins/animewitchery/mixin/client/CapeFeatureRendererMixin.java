package net.willowins.animewitchery.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.willowins.animewitchery.client.render.FlightElytraRenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class CapeFeatureRendererMixin {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/player/AbstractClientPlayer;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void animewitchery$hideCapeForFlightChestplates(PoseStack poseStack, MultiBufferSource multiBufferSource,
            int i, AbstractClientPlayer abstractClientPlayer, float f, float g, float h, float j, float k, float l,
            CallbackInfo ci) {
        if (FlightElytraRenderLayer.hasVisibleFlightChestplate(abstractClientPlayer)) {
            ci.cancel();
        }
    }
}

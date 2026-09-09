package net.willowins.animewitchery.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.willowins.animewitchery.client.AntifluidTextureUsage;
import net.willowins.animewitchery.client.AntifluidVertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Reroutes any draw that uses the antifluid texture through the void shader regardless of which
 * mod produced it. The antifluid still/flowing textures are unique to this mod, and every fluid
 * renderer draws them on the shared translucent sheets coming out of
 * {@link MultiBufferSource.BufferSource#getBuffer}. Quads carrying the antifluid sprite's UVs are
 * captured instead of drawn, then re-rendered by {@link AntifluidTextureUsage#drainInto} on the
 * void shader pass.
 */
@Mixin(MultiBufferSource.BufferSource.class)
public class AntifluidBufferSourceMixin {

    @ModifyReturnValue(method = "getBuffer", at = @At("RETURN"))
    private VertexConsumer animewitchery$captureAntifluidUsage(VertexConsumer original, RenderType renderType) {
        if (AntifluidTextureUsage.isTrackedFluidSheet(renderType)) {
            AntifluidTextureUsage.onWrapperCreated(renderType);
            return new AntifluidVertexConsumer(original, renderType);
        }
        return original;
    }
}
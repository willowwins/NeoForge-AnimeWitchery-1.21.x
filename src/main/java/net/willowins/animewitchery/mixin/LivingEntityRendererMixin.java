package net.willowins.animewitchery.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.willowins.animewitchery.client.InvertedSkinRenderTypes;
import net.willowins.animewitchery.client.InvertedSkinShaders;
import net.willowins.animewitchery.dimension.PocketDimensions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Renders every player skin through the color-inverting shader while the camera is in the
 * darkworld (pocket dimension). Only the base skin pass is swapped; armor, capes, held items and
 * other layers keep rendering normally.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @ModifyReturnValue(
            method = "getRenderType(Lnet/minecraft/world/entity/LivingEntity;ZZZ)Lnet/minecraft/client/renderer/RenderType;",
            at = @At("RETURN"))
    @Nullable
    private RenderType animewitchery$invertPlayerSkin(RenderType original, LivingEntity entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (original == null || InvertedSkinShaders.invertedSkinShader == null
                || !(entity instanceof AbstractClientPlayer player)) {
            return original;
        }
        if (!PocketDimensions.POCKET.equals(player.level().dimension())) {
            return original;
        }
        return InvertedSkinRenderTypes.skin(player.getSkin().texture());
    }
}

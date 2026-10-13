package net.willowins.animewitchery.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.client.InvertedSkinRenderTypes;
import net.willowins.animewitchery.client.InvertedSkinShaders;
import net.willowins.animewitchery.dimension.PocketDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Inverts the first-person arm (the skin texture visible on the local player's own hand) while in
 * the darkworld so it matches the inverted third-person body.
 */
@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {

    @Redirect(
            method = "renderHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entitySolid(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animewitchery$invertHandSolid(ResourceLocation texture) {
        return animewitchery$skinOr(texture, RenderType.entitySolid(texture));
    }

    @Redirect(
            method = "renderHand",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderType;entityTranslucent(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"))
    private static RenderType animewitchery$invertHandTranslucent(ResourceLocation texture) {
        return animewitchery$skinOr(texture, RenderType.entityTranslucent(texture));
    }

    private static RenderType animewitchery$skinOr(ResourceLocation texture, RenderType vanilla) {
        Level level = Minecraft.getInstance().level;
        if (level != null && PocketDimensions.POCKET.equals(level.dimension())
                && InvertedSkinShaders.invertedSkinShader != null) {
            return InvertedSkinRenderTypes.skin(texture);
        }
        return vanilla;
    }
}

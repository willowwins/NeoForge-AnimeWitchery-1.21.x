package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Render type that draws an entity texture through the color-inverting skin shader. Used to render
 * player skins in negative color while they are inside the darkworld/pocket dimension.
 */
public final class InvertedSkinRenderTypes {

    private static final Map<ResourceLocation, RenderType> CACHE = new ConcurrentHashMap<>();

    private InvertedSkinRenderTypes() {}

    public static RenderType skin(ResourceLocation texture) {
        return CACHE.computeIfAbsent(texture, InvertedSkinRenderTypes::create);
    }

    private static RenderType create(ResourceLocation texture) {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(new RenderStateShard.ShaderStateShard(InvertedSkinShaders.INVERTED_SKIN_SUPPLIER))
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(false);
        return RenderType.create(
                "animewitchery_inverted_skin",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                RenderType.TRANSIENT_BUFFER_SIZE,
                true,
                true,
                state);
    }
}
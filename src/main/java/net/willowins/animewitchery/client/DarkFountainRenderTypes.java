package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.willowins.animewitchery.AnimeWitchery;

public class DarkFountainRenderTypes {

    public static final ResourceLocation BEAM_LOCATION_1 =
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "textures/entity/dark_fountain_beam_1.png");

    public static final RenderType BEAM_1 = beam("dark_fountain_beam_1", BEAM_LOCATION_1);

    private static RenderType beam(String name, ResourceLocation texture) {
        return RenderType.create(
                name,
                DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS,
                RenderType.BIG_BUFFER_SIZE,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.RENDERTYPE_CUTOUT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                        .createCompositeState(false));
    }
}
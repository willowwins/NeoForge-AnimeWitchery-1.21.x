package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class AntifluidShaders {

    public static final ResourceLocation END_SKY_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
    public static final ResourceLocation END_PORTAL_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/environment/end_portal.png");

    public static final RenderType ANTIFLUID_PORTAL = RenderType.create(
            "animewitchery_antifluid_portal",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            1536,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_END_PORTAL_SHADER)
                    .setTextureState(
                            RenderStateShard.MultiTextureStateShard.builder()
                                    .add(END_SKY_LOCATION, false, false)
                                    .add(END_PORTAL_LOCATION, false, false)
                                    .build()
                    )
                    .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );
}

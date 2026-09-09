package net.willowins.animewitchery.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL11;

public class AntifluidRenderTypes {

    public static final RenderType VOID = RenderType.create(
            "antifluid_void",
            DefaultVertexFormat.POSITION_TEX_COLOR,
            VertexFormat.Mode.QUADS,
            RenderType.BIG_BUFFER_SIZE,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(AntifluidShaders.ANTIFLUID_SHADER_SUPPLIER))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard("antifluid_void_transparency",
                            () -> {
                                RenderSystem.enableBlend();
                                RenderSystem.defaultBlendFunc();
                            },
                            RenderSystem::disableBlend))
                    .setDepthTestState(new RenderStateShard.DepthTestStateShard("antifluid_void_depth", GL11.GL_LEQUAL))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, true))
                    .setOutputState(new RenderStateShard.OutputStateShard("antifluid_void_output",
                            () -> {
                                RenderSystem.enablePolygonOffset();
                                RenderSystem.polygonOffset(-0.02F, -0.05F);
                            },
                            () -> RenderSystem.disablePolygonOffset()))
                    .createCompositeState(false)
    );
}
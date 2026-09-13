package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.willowins.animewitchery.AnimeWitchery;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ShaderRegistrar {

    @SubscribeEvent
    public static void onRegisterShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            AntifluidShaders.ANTIFLUID_PORTAL_SHADER,
                            DefaultVertexFormat.POSITION_TEX_COLOR
                    ),
                    shader -> AntifluidShaders.antifluidPortalShader = shader
            );
            event.registerShader(
                    new ShaderInstance(
                            event.getResourceProvider(),
                            AntifluidShaders.ANTIFLUID_SPACE_SHADER,
                            DefaultVertexFormat.POSITION_TEX_COLOR
                    ),
                    shader -> AntifluidShaders.antifluidSpaceShader = shader
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to register antifluid portal shader", e);
        }
    }
}
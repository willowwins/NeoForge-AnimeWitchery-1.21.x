package net.willowins.animewitchery.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class AntifluidShaders {

    public static final ResourceLocation ANTIFLUID_PORTAL_SHADER =
            ResourceLocation.fromNamespaceAndPath("animewitchery", "rendertype_antifluid_portal");

    public static final ResourceLocation ANTIFLUID_SPACE_SHADER =
            ResourceLocation.fromNamespaceAndPath("animewitchery", "rendertype_antifluid_space");

    public static ShaderInstance antifluidPortalShader;

    public static ShaderInstance antifluidSpaceShader;

    public static final Supplier<ShaderInstance> ANTIFLUID_SHADER_SUPPLIER = () -> antifluidPortalShader;

    public static final Supplier<ShaderInstance> ANTIFLUID_SPACE_SUPPLIER = () -> antifluidSpaceShader;
}
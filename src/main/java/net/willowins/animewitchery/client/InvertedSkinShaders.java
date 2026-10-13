package net.willowins.animewitchery.client;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.willowins.animewitchery.AnimeWitchery;

import java.util.function.Supplier;

public class InvertedSkinShaders {

    public static final ResourceLocation INVERTED_SKIN_SHADER =
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "rendertype_inverted_skin");

    public static ShaderInstance invertedSkinShader;

    public static final Supplier<ShaderInstance> INVERTED_SKIN_SUPPLIER = () -> invertedSkinShader;
}
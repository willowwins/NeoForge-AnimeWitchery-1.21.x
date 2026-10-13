package net.willowins.animewitchery.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.willowins.animewitchery.enchantments.ModEnchantments;
import net.willowins.animewitchery.item.armor.ResonantArmorItem;

public class FlightElytraRenderLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    private static final ResourceLocation DEFAULT_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    private final ElytraModel<AbstractClientPlayer> elytraModel;

    public FlightElytraRenderLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> context) {
        super(context);
        this.elytraModel = new ElytraModel<>(
                Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.ELYTRA));
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light,
            AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
            float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!hasVisibleFlightChestplate(player)) {
            return;
        }

        matrices.pushPose();
        getParentModel().copyPropertiesTo(this.elytraModel);
        this.elytraModel.setupAnim(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        ResourceLocation texture = getFlightTexture(player);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderType.armorCutoutNoCull(texture));
        this.elytraModel.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
        matrices.popPose();
    }

    public static boolean hasVisibleFlightChestplate(AbstractClientPlayer player) {
        if (player == null) {
            return false;
        }
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (chest.isEmpty()) {
            return false;
        }
        if (!(chest.getItem() instanceof ArmorItem armor)) {
            return false;
        }
        if (armor instanceof ResonantArmorItem) {
            return false;
        }
        if (player.level() == null) {
            return false;
        }
        Holder<Enchantment> holder = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(ModEnchantments.FLIGHT_ENCHANT);
        return EnchantmentHelper.getItemEnchantmentLevel(holder, chest) > 0;
    }

    private ResourceLocation getFlightTexture(AbstractClientPlayer player) {
        if (player.getSkin() != null && player.getSkin().elytraTexture() != null) {
            return player.getSkin().elytraTexture();
        }
        if (player.getSkin() != null && player.getSkin().capeTexture() != null && player.isModelPartShown(PlayerModelPart.CAPE)) {
            return player.getSkin().capeTexture();
        }
        return DEFAULT_TEXTURE;
    }
}

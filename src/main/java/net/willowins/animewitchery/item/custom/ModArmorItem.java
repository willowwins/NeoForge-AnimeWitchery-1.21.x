package net.willowins.animewitchery.item.custom;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.effect.ModEffects;
import net.willowins.animewitchery.item.ModArmorMaterials;

import java.util.List;

/**
 * Grants full-set bonuses and effects depending on the armor material.
 * Automatically re-applies effects while worn, without stacking or duplication.
 */
public class ModArmorItem extends ArmorItem {
    private static final List<MaterialBonus> FULL_SET_BONUSES = List.of(
            // Silver
            new MaterialBonus(ModArmorMaterials.SILVER, List.of(
                    new EffectSpec(MobEffects.SATURATION, 200, 0),
                    new EffectSpec(MobEffects.GLOWING, 300, 0),
                    new EffectSpec(ModEffects.PACIFISM, 300, 0))),
            // Railgunner
            new MaterialBonus(ModArmorMaterials.RAILGUNNER, List.of(
                    new EffectSpec(MobEffects.MOVEMENT_SPEED, 200, 2),
                    new EffectSpec(MobEffects.WEAKNESS, 600, 255))),
            // Obelisk
            new MaterialBonus(ModArmorMaterials.OBELISK, List.of(
                    new EffectSpec(MobEffects.NIGHT_VISION, 300, 0),
                    new EffectSpec(MobEffects.DAMAGE_BOOST, 300, 1),
                    new EffectSpec(MobEffects.DAMAGE_RESISTANCE, 300, 1))),
            // Resonant
            new MaterialBonus(ModArmorMaterials.RESONANT, List.of(
                    new EffectSpec(MobEffects.DAMAGE_RESISTANCE, 300, 0),
                    new EffectSpec(ModEffects.MANA_REGEN, 300, 2))),
            // Haloic
            new MaterialBonus(ModArmorMaterials.HALOIC, List.of(
                    new EffectSpec(MobEffects.REGENERATION, 200, 1),
                    new EffectSpec(MobEffects.FIRE_RESISTANCE, 200, 0))));

    public ModArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide() || !(entity instanceof Player player)) {
            return;
        }
        if (!hasFullSuitOfArmor(player)) {
            return;
        }
        Holder<ArmorMaterial> material = getEquippedMaterial(player);
        if (material == null) {
            return;
        }
        for (MaterialBonus bonus : FULL_SET_BONUSES) {
            if (bonus.material().value() != material.value()) {
                continue;
            }
            for (EffectSpec spec : bonus.effects()) {
                MobEffectInstance current = player.getEffect(spec.effect());
                if (current == null || current.getDuration() <= 40) {
                    player.addEffect(new MobEffectInstance(spec.effect(), spec.duration(), spec.amplifier(),
                            false, false, false));
                }
            }
            break;
        }
    }

    private static boolean hasFullSuitOfArmor(Player player) {
        for (int i = 0; i < 4; i++) {
            ItemStack armor = player.getInventory().getArmor(i);
            if (armor.isEmpty() || !(armor.getItem() instanceof ArmorItem)) {
                return false;
            }
        }
        return true;
    }

    private static Holder<ArmorMaterial> getEquippedMaterial(Player player) {
        ItemStack head = player.getInventory().getArmor(3);
        if (!(head.getItem() instanceof ArmorItem headItem)) {
            return null;
        }
        Holder<ArmorMaterial> type = headItem.getMaterial();
        for (int i = 0; i < 4; i++) {
            ItemStack armor = player.getInventory().getArmor(i);
            if (!(armor.getItem() instanceof ArmorItem armorItem)
                    || armorItem.getMaterial().value() != type.value()) {
                return null;
            }
        }
        return type;
    }

    private record EffectSpec(Holder<MobEffect> effect, int duration, int amplifier) {
    }

    private record MaterialBonus(Holder<ArmorMaterial> material, List<EffectSpec> effects) {
    }
}

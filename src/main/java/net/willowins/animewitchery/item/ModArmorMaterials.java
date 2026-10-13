package net.willowins.animewitchery.item;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, AnimeWitchery.MOD_ID);

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SILVER = register("silver", 3, 6, 8, 3, 10,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.5f, 0.5f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ASSASSIN = register("assassin", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BUTCHER = register("butcher", 3, 6, 8, 3, 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0.5f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> BRUTE = register("brute", 4, 7, 9, 4, 10,
            SoundEvents.ARMOR_EQUIP_IRON, 2.0f, 1.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MONK = register("monk", 2, 4, 5, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HEALER = register("healer", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SANGUINE = register("sanguine", 3, 6, 8, 3, 15,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.5f, 0.5f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DRUID = register("druid", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ALCHEMIST = register("alchemist", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GUNSLINGER = register("gunslinger", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FEMBOY = register("femboy", 1, 3, 4, 1, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> GAMBLER = register("gambler", 2, 5, 6, 2, 20,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> KNIGHT = register("knight", 3, 6, 8, 3, 10,
            SoundEvents.ARMOR_EQUIP_IRON, 1.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PALADIN = register("paladin", 3, 7, 9, 3, 15,
            SoundEvents.ARMOR_EQUIP_IRON, 2.0f, 0.1f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> DEATHBRINGER = register("deathbringer", 2, 5, 6, 2, 15,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> SUMMONER = register("summoner", 2, 4, 5, 2, 20,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> MAGE = register("mage", 2, 4, 5, 2, 30,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> FARMER = register("farmer", 2, 4, 5, 2, 10,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> PROSPECTOR = register("prospector", 2, 5, 6, 2, 10,
            SoundEvents.ARMOR_EQUIP_CHAIN, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HOARDER = register("hoarder", 3, 6, 8, 3, 50,
            SoundEvents.ARMOR_EQUIP_GOLD, 0.0f, 0.0f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RAILGUNNER = register("railgunner", 3, 6, 8, 3, 8,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0f, 0.1f,
            () -> Ingredient.of(Items.NETHERITE_INGOT));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> RESONANT = register("resonant", 3, 6, 8, 3, 25,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
            () -> Ingredient.of(ModItems.RESONANT_CATALYST.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> OBELISK = register("obelisk", 4, 7, 9, 4, 15,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 4.0f, 0.2f,
            () -> Ingredient.of(ModItems.SILVERINGOT.get()));

    public static final DeferredHolder<ArmorMaterial, ArmorMaterial> HALOIC = register("haloic", 4, 7, 9, 4, 25,
            SoundEvents.ARMOR_EQUIP_GOLD, 4.0F, 0.2F,
            () -> Ingredient.of(ModItems.HALOIC_INGOT.get()));

    private static DeferredHolder<ArmorMaterial, ArmorMaterial> register(String id, int boots, int leggings, int chestplate, int helmet,
            int enchantability, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance,
            Supplier<Ingredient> repairIngredient) {
        return ARMOR_MATERIALS.register(id, () -> {
            Map<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
            defense.put(ArmorItem.Type.BOOTS, boots);
            defense.put(ArmorItem.Type.LEGGINGS, leggings);
            defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
            defense.put(ArmorItem.Type.HELMET, helmet);
            List<ArmorMaterial.Layer> layers = List.of(
                    new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, id)));
            return new ArmorMaterial(defense, enchantability, equipSound, repairIngredient, layers, toughness,
                    knockbackResistance);
        });
    }

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
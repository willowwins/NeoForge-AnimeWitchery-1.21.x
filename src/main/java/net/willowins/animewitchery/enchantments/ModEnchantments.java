package net.willowins.animewitchery.enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.willowins.animewitchery.AnimeWitchery;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> BOOT_ENCHANT = registerKey("boot_enchant");
    public static final ResourceKey<Enchantment> EXCAVATE_ENCHANT = registerKey("excavate_enchant");
    public static final ResourceKey<Enchantment> BLAST_ENCHANT = registerKey("blast_enchant");
    public static final ResourceKey<Enchantment> FLIGHT_ENCHANT = registerKey("flight_enchant");
    public static final ResourceKey<Enchantment> SLIVER = registerKey("sliver_enchant");
    public static final ResourceKey<Enchantment> LEAD_COATED_PAINT = registerKey("lead_coated_paint");
    public static final ResourceKey<Enchantment> EXP_BOOST = registerKey("exp_boost");
    public static final ResourceKey<Enchantment> CAPTURING = registerKey("capturing");
    public static final ResourceKey<Enchantment> CHAINSAW_ENCHANT = registerKey("chainsaw_enchant");

    private static ResourceKey<Enchantment> registerKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,
                ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, name));
    }
}

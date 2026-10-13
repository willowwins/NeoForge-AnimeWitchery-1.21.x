package net.willowins.animewitchery.item;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.fluid.ModFluids;
import net.willowins.animewitchery.item.custom.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnimeWitchery.MOD_ID);

    public static final DeferredItem<Item> STARLIGHT_BOTTLE = ITEMS.register("starlight_bottle",
            () ->new StarlightBottleItem(new Item.Properties().food(ModFoodProperties.STARLIGHT_BOTTLE).stacksTo(16)));
    public static final DeferredItem<Item> SILVERINGOT = ITEMS.register("silver_ingot",
            () ->new Item(new Item.Properties()));
    public static final DeferredItem<Item> DIAMOND_SHARD = ITEMS.register("diamond_shard",
            () ->new Item(new Item.Properties()));
    public static final DeferredItem<Item> RAW_SILVER = ITEMS.register("raw_silver",
            () ->new Item(new Item.Properties()));
    public static final DeferredItem<Item> STARLIGHT_DUST = ITEMS.register("starlight_dust",
            () ->new Item(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> WAND = ITEMS.register("wand",
            () ->new WandItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> STARDROP_WAND = ITEMS.register("stardrop_wand",
            () ->new StardropWandItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MANA_READER = ITEMS.register("mana_reader",
            () ->new ManaReaderItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> ALCHEMICAL_CATALYST = ITEMS.register("alchemical_catalyst",
            () ->new AlchemicalCatalystItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> RESONANT_CATALYST = ITEMS.register("resonant_catalyst",
            () ->new ResonantCatalystItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> STAR_DROP = ITEMS.register("stardrop",
            () ->new Item(new Item.Properties()));


    public static final DeferredItem<Item> SILVER_TEMPLATE = ITEMS.register("silver_template",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STAFF_HEAD = ITEMS.register("staff_head",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BLAZE_POWDER_BAG = ITEMS.register("blaze_powder_bag",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> VOID_ESSENCE = ITEMS.register("void_essence",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> RUNE_STONE = ITEMS.register("rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BLOOD_RUNE_STONE = ITEMS.register("blood_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> SILVER_NUGGET = ITEMS.register("silvernugget",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SPOOL = ITEMS.register("spool",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILVER_SPOOL = ITEMS.register("silverspool",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STAR_SHARD = ITEMS.register("star_shard",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> MASTER_KEY = ITEMS.register("master_key",
            () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final DeferredItem<Item> ALCHEMY_RECIPE_BOOK = ITEMS.register("alchemy_recipe_book",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_SPELL_SCROLL = registerSpellScroll("fire_spell_scroll", "Fire Blast");
    public static final DeferredItem<Item> WATER_SPELL_SCROLL = registerSpellScroll("water_spell_scroll", "Water Shield");
    public static final DeferredItem<Item> EARTH_SPELL_SCROLL = registerSpellScroll("earth_spell_scroll", "Earth Spike");
    public static final DeferredItem<Item> AIR_SPELL_SCROLL = registerSpellScroll("air_spell_scroll", "Wind Gust");
    public static final DeferredItem<Item> LIFE_SPELL_SCROLL = registerSpellScroll("life_spell_scroll", "Healing Wave");
    public static final DeferredItem<Item> DEATH_SPELL_SCROLL = registerSpellScroll("death_spell_scroll", "Wither Touch");
    public static final DeferredItem<Item> LIGHT_SPELL_SCROLL = registerSpellScroll("light_spell_scroll", "Light Burst");
    public static final DeferredItem<Item> DARKNESS_SPELL_SCROLL = registerSpellScroll("darkness_spell_scroll", "Shadow Bind");
    public static final DeferredItem<Item> SCHEELITE_CRYSTAL = ITEMS.register("scheelite_crystal",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SCHEELITE_DUST = ITEMS.register("scheelite_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SCHEELITE_SLURRY = ITEMS.register("scheelite_sulpheuric-phosuric_acid_slurry",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> OBELISK_SHARD = ITEMS.register("obelisk_shard",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> ENCHANTED_CRYSTAL = ITEMS.register("enchanted_crystal",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BONE_DUST = ITEMS.register("bone_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AMETHYST_DUST = ITEMS.register("amethyst_dust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STARDUST = ITEMS.register("stardust",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> FIRE_RUNE_STONE = ITEMS.register("fire_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> WATER_RUNE_STONE = ITEMS.register("water_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> EARTH_RUNE_STONE = ITEMS.register("earth_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> AIR_RUNE_STONE = ITEMS.register("air_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LIFE_RUNE_STONE = ITEMS.register("life_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DEATH_RUNE_STONE = ITEMS.register("death_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> LIGHT_RUNE_STONE = ITEMS.register("light_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DARKNESS_RUNE_STONE = ITEMS.register("darkness_rune_stone",
            () -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> BARRIER_CATALYST = ITEMS.register("barrier_catalyst",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> HALOIC_SCRAP = ITEMS.register("haloic_scrap",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> HALOIC_INGOT = ITEMS.register("haloic_ingot",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    // Crafting tools and flask supplies. Their interactions are ported in dedicated item classes.
    public static final DeferredItem<Item> EMPTY_FLASK = ITEMS.register("empty_flask",
            () -> new EmptyFlaskItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> POTION_FLASK = ITEMS.register("potion_flask",
            () -> new PotionFlaskItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> MORTAR_AND_PESTLE = ITEMS.register("mortar_and_pestle",
            () -> new MortarAndPestleItem(new Item.Properties().durability(100).craftRemainder(Items.AIR)));
    public static final DeferredItem<Item> FIRE_RES_CRYSTAL = ITEMS.register("fire_res_crystal",
            () -> new FireResCrystalItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SPEED_CRYSTAL = ITEMS.register("speed_crystal",
            () -> new SpeedCrystalItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MANA_ROCKET = ITEMS.register("mana_rocket",
            () -> new ManaRocketItem(new Item.Properties().stacksTo(1).fireResistant()));
    public static final DeferredItem<Item> CREEPING_PREVAILANCE = ITEMS.register("creeping_prevailance",
            () -> new RepairCharmItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> REPAIR_ESSENCE = ITEMS.register("repair_essence",
            () -> new RepairEssenceItem(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> EMERGENCY_RECALL = ITEMS.register("emergency_recall",
            () -> new EmergencyRecallItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> DIVINING_ROD = ITEMS.register("divining_rod",
            () -> new DiviningRodItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> TORCH_FLOWER_ESSENCE = ITEMS.register("torch_flower_essence",
            () -> new TorchFlowerEssenceItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> WEATHERITEM = ITEMS.register("weatheritem",
            () -> new WeatherItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RESPAWN_BEACON = ITEMS.register("respawn_beacon",
            () -> new RespawnTeleportItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(300)));
    public static final DeferredItem<Item> SOUL_JAR = ITEMS.register("soul_jar",
            () -> new SoulJarItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> SOUL = ITEMS.register("soul",
            () -> new SoulItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SUMMONER_STAFF = ITEMS.register("summoner_staff",
            () -> new SummonerStaffItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Foods
    public static final DeferredItem<Item> LEMON = ITEMS.register("lemon",
            () -> new Item(new Item.Properties().food(ModFoodProperties.LEMON)));
    public static final DeferredItem<Item> STRAWBERRY = ITEMS.register("strawberry",
            () -> new Item(new Item.Properties().food(ModFoodProperties.STRAWBERRY)));
    public static final DeferredItem<Item> TART_CRUST = ITEMS.register("tart_crust",
            () -> new Item(new Item.Properties().food(ModFoodProperties.TART_CRUST)));
    public static final DeferredItem<Item> UNBAKED_LEMON_TART = ITEMS.register("unbaked_lemon_tart",
            () -> new Item(new Item.Properties().food(ModFoodProperties.UNBAKED_TART)));
    public static final DeferredItem<Item> UNBAKED_STRAWBERRY_TART = ITEMS.register("unbaked_strawberry_tart",
            () -> new Item(new Item.Properties().food(ModFoodProperties.UNBAKED_TART)));
    public static final DeferredItem<Item> LEMON_TART = ITEMS.register("lemon_tart",
            () -> new Item(new Item.Properties().food(ModFoodProperties.LEMON_TART)));
    public static final DeferredItem<Item> STRAWBERRY_TART = ITEMS.register("strawberry_tart",
            () -> new Item(new Item.Properties().food(ModFoodProperties.STRAWBERRY_TART)));
    public static final DeferredItem<Item> ROSEWILLOW_BLOSSOM = ITEMS.register("rosewillow_blossom",
            () -> new Item(new Item.Properties().food(ModFoodProperties.ROSEWILLOW_BLOSSOM)));

    // Silver tools
    public static final DeferredItem<Item> SILVER_SWORD = ITEMS.register("silver_sword",
            () -> new SwordItem(ModToolMaterial.SILVER, new Item.Properties().attributes(SwordItem.createAttributes(ModToolMaterial.SILVER, 3, -2.4F))));
    public static final DeferredItem<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe",
            () -> new PickaxeItem(ModToolMaterial.SILVER, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterial.SILVER, 0.0F, -3.0F))));
    public static final DeferredItem<Item> SILVER_AXE = ITEMS.register("silver_axe",
            () -> new AxeItem(ModToolMaterial.SILVER, new Item.Properties().attributes(AxeItem.createAttributes(ModToolMaterial.SILVER, 6.0F, -3.1F))));
    public static final DeferredItem<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel",
            () -> new ShovelItem(ModToolMaterial.SILVER, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolMaterial.SILVER, 1.5F, -3.0F))));
    public static final DeferredItem<Item> SILVER_HOE = ITEMS.register("silver_hoe",
            () -> new HoeItem(ModToolMaterial.SILVER, new Item.Properties().attributes(HoeItem.createAttributes(ModToolMaterial.SILVER, -2.0F, 0.0F))));

    // Silver armor
    public static final DeferredItem<Item> SILVER_HELMET = ITEMS.register("silver_helmet",
            () -> new net.willowins.animewitchery.item.custom.ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
    public static final DeferredItem<Item> SILVER_CHESTPLATE = ITEMS.register("silver_chestplate",
            () -> new net.willowins.animewitchery.item.custom.ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
    public static final DeferredItem<Item> SILVER_LEGGINGS = ITEMS.register("silver_leggings",
            () -> new net.willowins.animewitchery.item.custom.ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
    public static final DeferredItem<Item> SILVER_BOOTS = ITEMS.register("silver_boots",
            () -> new net.willowins.animewitchery.item.custom.ModArmorItem(ModArmorMaterials.SILVER, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));

    // Resonant tools
    public static final DeferredItem<Item> RESONANT_SWORD = ITEMS.register("resonant_sword",
            () -> new SwordItem(ModToolMaterial.RESONANT, new Item.Properties().attributes(SwordItem.createAttributes(ModToolMaterial.RESONANT, 3, -2.4F))));
    public static final DeferredItem<Item> RESONANT_PICKAXE = ITEMS.register("resonant_pickaxe",
            () -> new PickaxeItem(ModToolMaterial.RESONANT, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterial.RESONANT, 1.0F, -2.8F))));
    public static final DeferredItem<Item> RESONANT_AXE = ITEMS.register("resonant_axe",
            () -> new AxeItem(ModToolMaterial.RESONANT, new Item.Properties().attributes(AxeItem.createAttributes(ModToolMaterial.RESONANT, 6.0F, -3.1F))));
    public static final DeferredItem<Item> RESONANT_SHOVEL = ITEMS.register("resonant_shovel",
            () -> new ShovelItem(ModToolMaterial.RESONANT, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolMaterial.RESONANT, 1.5F, -3.0F))));
    public static final DeferredItem<Item> RESONANT_HOE = ITEMS.register("resonant_hoe",
            () -> new HoeItem(ModToolMaterial.RESONANT, new Item.Properties().attributes(HoeItem.createAttributes(ModToolMaterial.RESONANT, -2.0F, 0.0F))));

    public static final DeferredItem<DeepDarkDeepDishItem> DEEP_DARK_DEEP_DISH = ITEMS.register("deep_dark_deep_dish",
            () -> new DeepDarkDeepDishItem(new Item.Properties()));

    // Resonant armor
    public static final DeferredItem<Item> RESONANT_HELMET = ITEMS.register("resonant_helmet",
            () -> new net.willowins.animewitchery.item.armor.ResonantArmorItem(ModArmorMaterials.RESONANT,
                    ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<Item> RESONANT_CHESTPLATE = ITEMS.register("resonant_chestplate",
            () -> new net.willowins.animewitchery.item.armor.ResonantArmorItem(ModArmorMaterials.RESONANT,
                    ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<Item> RESONANT_LEGGINGS = ITEMS.register("resonant_leggings",
            () -> new net.willowins.animewitchery.item.armor.ResonantArmorItem(ModArmorMaterials.RESONANT,
                    ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<Item> RESONANT_BOOTS = ITEMS.register("resonant_boots",
            () -> new net.willowins.animewitchery.item.armor.ResonantArmorItem(ModArmorMaterials.RESONANT,
                    ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    // Obelisk pickaxe
    public static final DeferredItem<Item> OBELISK_PICKAXE = ITEMS.register("obelisk_pickaxe",
            () -> new PickaxeItem(ModToolMaterial.OBELISK, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterial.OBELISK, 0.0F, -2.8F))));

    // Haloic tools
    public static final DeferredItem<Item> HALOIC_PICKAXE = ITEMS.register("haloic_pickaxe",
            () -> new PickaxeItem(ModToolMaterial.HALOIC, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterial.HALOIC, 1.0F, -2.8F))));
    public static final DeferredItem<Item> HALOIC_AXE = ITEMS.register("haloic_axe",
            () -> new AxeItem(ModToolMaterial.HALOIC, new Item.Properties().attributes(AxeItem.createAttributes(ModToolMaterial.HALOIC, 5.0F, -3.0F))));
    public static final DeferredItem<Item> HALOIC_SHOVEL = ITEMS.register("haloic_shovel",
            () -> new ShovelItem(ModToolMaterial.HALOIC, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolMaterial.HALOIC, 1.5F, -3.0F))));
    public static final DeferredItem<Item> HALOIC_HOE = ITEMS.register("haloic_hoe",
            () -> new HoeItem(ModToolMaterial.HALOIC, new Item.Properties().attributes(HoeItem.createAttributes(ModToolMaterial.HALOIC, -4.0F, 0.0F))));
    public static final DeferredItem<Item> HALOIC_SWORD = ITEMS.register("haloic_sword",
            () -> new HaloicSwordItem(new Item.Properties()));
    public static final DeferredItem<Item> HALOIC_HELMET = ITEMS.register("haloic_helmet",
            () -> new ModArmorItem(ModArmorMaterials.HALOIC, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<Item> HALOIC_CHESTPLATE = ITEMS.register("haloic_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.HALOIC, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<Item> HALOIC_LEGGINGS = ITEMS.register("haloic_leggings",
            () -> new ModArmorItem(ModArmorMaterials.HALOIC, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<Item> HALOIC_BOOTS = ITEMS.register("haloic_boots",
            () -> new ModArmorItem(ModArmorMaterials.HALOIC, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    // Curios trinkets
    public static final DeferredItem<Item> SILVER_PENDANT = ITEMS.register("silver_pendant",
            () -> new SilverPendantItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> LATENT_MANA_PENDANT = ITEMS.register("latent_mana_pendant",
            () -> new LatentManaPendantItem(new Item.Properties().stacksTo(1)));

    // Mana items
    public static final DeferredItem<Item> FULFILLING_TIARA = ITEMS.register("fulfilling_tiara",
            () -> new FulfillingTiaraItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> RESONANT_TIARA = ITEMS.register("resonant_tiara",
            () -> new ResonantTiaraItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> FUEL_ROD = ITEMS.register("fuel_rod",
            () -> new FuelRodItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> OVERHEATED_FUEL_ROD = ITEMS.register("overheated_fuel_rod",
            () -> new OverheatedFuelRodItem(new Item.Properties().stacksTo(1)));

    // HUD baubles
    public static final DeferredItem<Item> HUD = ITEMS.register("hud",
            () -> new HudItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> HUD_ALT_1 = ITEMS.register("hud_alt_1",
            () -> new HudItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BULLET = ITEMS.register("bullet",
            () -> new net.willowins.animewitchery.item.custom.BulletItem(new Item.Properties().stacksTo(64)));

    // Ported class equipment; item-specific passive effects are handled in later behavior ports.
    public static final DeferredItem<Item> ASSASSIN_DAGGER = registerClassSword("assassin_dagger", 5, -1.6F);
    public static final DeferredItem<Item> ASSASSIN_HOOD = registerClassArmor("assassin_hood", ModArmorMaterials.ASSASSIN, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> ASSASSIN_CHESTPLATE = registerClassArmor("assassin_chestplate", ModArmorMaterials.ASSASSIN, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> ASSASSIN_LEGGINGS = registerClassArmor("assassin_leggings", ModArmorMaterials.ASSASSIN, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> ASSASSIN_BOOTS = registerClassArmor("assassin_boots", ModArmorMaterials.ASSASSIN, ArmorItem.Type.BOOTS);

    public static final DeferredItem<Item> BUTCHER_KNIFE = registerClassSword("butcher_knife", 6, -2.4F);
    public static final DeferredItem<Item> BUTCHER_HELMET = registerClassArmor("butcher_helmet", ModArmorMaterials.BUTCHER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> BUTCHER_APRON = registerClassArmor("butcher_apron", ModArmorMaterials.BUTCHER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> BUTCHER_LEGGINGS = registerClassArmor("butcher_leggings", ModArmorMaterials.BUTCHER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> BUTCHER_BOOTS = registerClassArmor("butcher_boots", ModArmorMaterials.BUTCHER, ArmorItem.Type.BOOTS);

    public static final DeferredItem<Item> HEAVY_MACE = registerClassSword("heavy_mace", 9, -3.2F);
    public static final DeferredItem<Item> HEAVY_PLATE_HELMET = registerClassArmor("heavy_plate_helmet", ModArmorMaterials.BRUTE, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> HEAVY_PLATE_CHESTPLATE = registerClassArmor("heavy_plate_chestplate", ModArmorMaterials.BRUTE, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> HEAVY_PLATE_LEGGINGS = registerClassArmor("heavy_plate_leggings", ModArmorMaterials.BRUTE, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> HEAVY_PLATE_BOOTS = registerClassArmor("heavy_plate_boots", ModArmorMaterials.BRUTE, ArmorItem.Type.BOOTS);

    public static final DeferredItem<Item> IRON_SHACKLE = registerClassSword("iron_shackle", 4, 0.0F);
    public static final DeferredItem<Item> MONK_HEAD = registerClassArmor("monk_head", ModArmorMaterials.MONK, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> SPARSE_TUNIC = registerClassArmor("sparse_tunic", ModArmorMaterials.MONK, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> MONK_LEGGINGS = registerClassArmor("monk_leggings", ModArmorMaterials.MONK, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> MONK_BOOTS = registerClassArmor("monk_boots", ModArmorMaterials.MONK, ArmorItem.Type.BOOTS);

    // Remaining class weapons use the Fabric stats/materials; custom procs and abilities follow in their behavior ports.
    public static final DeferredItem<Item> BLOODLETTER = registerTieredSword("bloodletter", ModToolMaterial.SILVER, 4, 1.0F, Rarity.RARE);
    public static final DeferredItem<Item> COPPER_WARHAMMER = registerTieredSword("copper_warhammer", ModToolMaterial.COPPER, 9, -3.2F, Rarity.RARE);
    public static final DeferredItem<Item> GREAT_HAMMER = registerTieredSword("great_hammer", ModToolMaterial.SILVER, 9, -3.4F, Rarity.RARE);
    public static final DeferredItem<Item> LONGSWORD = registerTieredSword("longsword", ModToolMaterial.SILVER, 7, -2.8F, Rarity.RARE);
    public static final DeferredItem<Item> OATHBREAKER = registerTieredSword("oathbreaker", ModToolMaterial.RESONANT, 7, -2.4F, Rarity.EPIC);
    public static final DeferredItem<Item> OBELISK_SWORD = registerTieredSword("obelisk_sword", ModToolMaterial.OBELISK, 7, -1.4F, Rarity.RARE);
    public static final DeferredItem<Item> RESONANT_GREATSWORD = registerTieredSword("resonant_greatsword", ModToolMaterial.RESONANT, 7, -2.5F, Rarity.RARE);
    public static final DeferredItem<Item> SWORD_OF_MIDAS = registerTieredSword("sword_of_midas", Tiers.GOLD, 6, -2.4F, Rarity.EPIC);

    // Scythe item registrations and Fabric damage/tier values; area harvest and sweep behavior remain to be ported.
    public static final DeferredItem<Item> WOODEN_SCYTHE = registerTieredHoe("wooden_scythe", Tiers.WOOD, 6, -3.2F, Rarity.COMMON);
    public static final DeferredItem<Item> STONE_SCYTHE = registerTieredHoe("stone_scythe", Tiers.STONE, 7, -3.2F, Rarity.COMMON);
    public static final DeferredItem<Item> COPPER_SCYTHE = registerTieredHoe("copper_scythe", ModToolMaterial.COPPER, 6, -3.1F, Rarity.COMMON);
    public static final DeferredItem<Item> IRON_SCYTHE = registerTieredHoe("iron_scythe", Tiers.IRON, 6, -3.1F, Rarity.COMMON);
    public static final DeferredItem<Item> DIAMOND_SCYTHE = registerTieredHoe("diamond_scythe", Tiers.DIAMOND, 5, -3.0F, Rarity.COMMON);
    public static final DeferredItem<Item> NETHERITE_SCYTHE = registerTieredHoe("netherite_scythe", Tiers.NETHERITE, 5, -3.0F, Rarity.COMMON);
    public static final DeferredItem<Item> DEATHBRINGER_SCYTHE = registerTieredHoe("deathbringer_scythe", ModToolMaterial.SILVER, 6, -2.5F, Rarity.RARE);
    public static final DeferredItem<Item> SOUL_SCYTHE = registerTieredHoe("soul_scythe", ModToolMaterial.HALOIC, 8, -3.0F, Rarity.EPIC);



    // Remaining Fabric class armor sets.
    public static final DeferredItem<Item> RAILGUNNER_HELMET = registerClassArmor("railgunner_helmet", ModArmorMaterials.RAILGUNNER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> RAILGUNNER_CHESTPLATE = registerClassArmor("railgunner_chestplate", ModArmorMaterials.RAILGUNNER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> RAILGUNNER_LEGGINGS = registerClassArmor("railgunner_leggings", ModArmorMaterials.RAILGUNNER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> RAILGUNNER_BOOTS = registerClassArmor("railgunner_boots", ModArmorMaterials.RAILGUNNER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> OBELISK_HELMET = registerClassArmor("obelisk_helmet", ModArmorMaterials.OBELISK, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> OBELISK_CHESTPLATE = registerClassArmor("obelisk_chestplate", ModArmorMaterials.OBELISK, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> OBELISK_LEGGINGS = registerClassArmor("obelisk_leggings", ModArmorMaterials.OBELISK, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> OBELISK_BOOTS = registerClassArmor("obelisk_boots", ModArmorMaterials.OBELISK, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> PALADIN_HELMET = registerClassArmor("paladin_helmet", ModArmorMaterials.PALADIN, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> PALADIN_CHESTPLATE = registerClassArmor("paladin_chestplate", ModArmorMaterials.PALADIN, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> PALADIN_LEGGINGS = registerClassArmor("paladin_leggings", ModArmorMaterials.PALADIN, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> PALADIN_BOOTS = registerClassArmor("paladin_boots", ModArmorMaterials.PALADIN, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> SANGUINE_HOOD = registerClassArmor("sanguine_hood", ModArmorMaterials.SANGUINE, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> HOODED_CAPE = registerClassArmor("hooded_cape", ModArmorMaterials.SANGUINE, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> SANGUINE_LEGGINGS = registerClassArmor("sanguine_leggings", ModArmorMaterials.SANGUINE, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> SANGUINE_BOOTS = registerClassArmor("sanguine_boots", ModArmorMaterials.SANGUINE, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> DEATHBRINGER_HOOD = registerClassArmor("deathbringer_hood", ModArmorMaterials.DEATHBRINGER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> DEATHBRINGER_ROBES = registerClassArmor("deathbringer_robes", ModArmorMaterials.DEATHBRINGER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> DEATHBRINGER_LEGGINGS = registerClassArmor("deathbringer_leggings", ModArmorMaterials.DEATHBRINGER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> DEATHBRINGER_BOOTS = registerClassArmor("deathbringer_boots", ModArmorMaterials.DEATHBRINGER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> SUMMONER_HOOD = registerClassArmor("summoner_hood", ModArmorMaterials.SUMMONER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> SUMMONER_ROBES = registerClassArmor("summoner_robes", ModArmorMaterials.SUMMONER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> SUMMONER_LEGGINGS = registerClassArmor("summoner_leggings", ModArmorMaterials.SUMMONER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> SUMMONER_BOOTS = registerClassArmor("summoner_boots", ModArmorMaterials.SUMMONER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> MAGE_HAT = registerClassArmor("mage_hat", ModArmorMaterials.MAGE, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> MAGE_ROBES = registerClassArmor("mage_robes", ModArmorMaterials.MAGE, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> MAGE_LEGGINGS = registerClassArmor("mage_leggings", ModArmorMaterials.MAGE, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> MAGE_BOOTS = registerClassArmor("mage_boots", ModArmorMaterials.MAGE, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> STRAW_HAT = registerClassArmor("straw_hat", ModArmorMaterials.FARMER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> FARMER_OVERALLS = registerClassArmor("farmer_overalls", ModArmorMaterials.FARMER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> FARMER_LEGGINGS = registerClassArmor("farmer_leggings", ModArmorMaterials.FARMER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> FARMER_BOOTS = registerClassArmor("farmer_boots", ModArmorMaterials.FARMER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> PROSPECTOR_HAT = registerClassArmor("prospector_hat", ModArmorMaterials.PROSPECTOR, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> PROSPECTOR_TUNIC = registerClassArmor("prospector_tunic", ModArmorMaterials.PROSPECTOR, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> PROSPECTOR_LEGGINGS = registerClassArmor("prospector_leggings", ModArmorMaterials.PROSPECTOR, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> PROSPECTOR_BOOTS = registerClassArmor("prospector_boots", ModArmorMaterials.PROSPECTOR, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> HOARDER_HELMET = registerClassArmor("hoarder_helmet", ModArmorMaterials.HOARDER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> HOARDER_CHESTPLATE = registerClassArmor("hoarder_chestplate", ModArmorMaterials.HOARDER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> HOARDER_LEGGINGS = registerClassArmor("hoarder_leggings", ModArmorMaterials.HOARDER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> HOARDER_BOOTS = registerClassArmor("hoarder_boots", ModArmorMaterials.HOARDER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> DRUID_HOOD = registerClassArmor("druid_hood", ModArmorMaterials.DRUID, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> COTTON_GOWN = registerClassArmor("cotton_gown", ModArmorMaterials.DRUID, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> DRUID_LEGGINGS = registerClassArmor("druid_leggings", ModArmorMaterials.DRUID, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> DRUID_BOOTS = registerClassArmor("druid_boots", ModArmorMaterials.DRUID, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> ALCHEMIST_HOOD = registerClassArmor("alchemist_hood", ModArmorMaterials.ALCHEMIST, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> STAINED_ROBES = registerClassArmor("stained_robes", ModArmorMaterials.ALCHEMIST, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> ALCHEMIST_LEGGINGS = registerClassArmor("alchemist_leggings", ModArmorMaterials.ALCHEMIST, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> ALCHEMIST_BOOTS = registerClassArmor("alchemist_boots", ModArmorMaterials.ALCHEMIST, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> COWBOY_HAT = registerClassArmor("cowboy_hat", ModArmorMaterials.GUNSLINGER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> GUNSLINGER_VEST = registerClassArmor("gunslinger_vest", ModArmorMaterials.GUNSLINGER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> CHAPS = registerClassArmor("chaps", ModArmorMaterials.GUNSLINGER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> GUNSLINGER_BOOTS = registerClassArmor("gunslinger_boots", ModArmorMaterials.GUNSLINGER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> GAMBLER_VISOR = registerClassArmor("gambler_visor", ModArmorMaterials.GAMBLER, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> SOILED_JACKET = registerClassArmor("soiled_jacket", ModArmorMaterials.GAMBLER, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> GAMBLER_PANTS = registerClassArmor("gambler_pants", ModArmorMaterials.GAMBLER, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> GAMBLER_SHOES = registerClassArmor("gambler_shoes", ModArmorMaterials.GAMBLER, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> CAT_EARS = registerClassArmor("cat_ears", ModArmorMaterials.FEMBOY, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> FEMBOY_HOODIE = registerClassArmor("femboy_hoodie", ModArmorMaterials.FEMBOY, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> THIGH_HIGHS = registerClassArmor("thigh_highs", ModArmorMaterials.FEMBOY, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> PLATFORM_BOOTS = registerClassArmor("platform_boots", ModArmorMaterials.FEMBOY, ArmorItem.Type.BOOTS);
    public static final DeferredItem<Item> CROWN = registerClassArmor("crown", ModArmorMaterials.KNIGHT, ArmorItem.Type.HELMET);
    public static final DeferredItem<Item> PLATE_CHESTPLATE = registerClassArmor("plate_chestplate", ModArmorMaterials.KNIGHT, ArmorItem.Type.CHESTPLATE);
    public static final DeferredItem<Item> PLATE_LEGGINGS = registerClassArmor("plate_leggings", ModArmorMaterials.KNIGHT, ArmorItem.Type.LEGGINGS);
    public static final DeferredItem<Item> PLATE_BOOTS = registerClassArmor("plate_boots", ModArmorMaterials.KNIGHT, ArmorItem.Type.BOOTS);

    public static final DeferredItem<Item> STARLIGHT_BUCKET = ITEMS.register("starlight_bucket",
            () -> new BucketItem(ModFluids.STARLIGHT_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> LIQUIFIED_SKULK_BUCKET = ITEMS.register("liquified_skulk_bucket",
            () -> new BucketItem(ModFluids.LIQUIFIED_SKULK_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> LIQUID_EXPERIENCE_BUCKET = ITEMS.register("liquid_experience_bucket",
            () -> new BucketItem(ModFluids.LIQUID_EXPERIENCE_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> OBSIDIAN_TEARS_BUCKET = ITEMS.register("obsidian_tears_bucket",
            () -> new BucketItem(ModFluids.OBSIDIAN_TEARS_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> BLOOD_BUCKET = ITEMS.register("blood_bucket",
            () -> new BucketItem(ModFluids.BLOOD_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final DeferredItem<Item> ANTIFLUID_BUCKET = ITEMS.register("antifluid_bucket",
            () -> new BucketItem(ModFluids.ANTIFLUID_SOURCE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static DeferredItem<Item> registerSpellScroll(String id, String spellName) {
        return ITEMS.register(id, () -> new SpellScrollItem(
                new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), spellName));
    }

    private static DeferredItem<Item> registerClassArmor(String id,
            net.minecraft.core.Holder<net.minecraft.world.item.ArmorMaterial> material, ArmorItem.Type type) {
        return ITEMS.register(id, () -> new ArmorItem(material, type,
                new Item.Properties().durability(type.getDurability(15)).stacksTo(1).rarity(Rarity.RARE)));
    }

    private static DeferredItem<Item> registerClassSword(String id, int damage, float speed) {
        return registerTieredSword(id, ModToolMaterial.SILVER, damage, speed, Rarity.RARE);
    }

    private static DeferredItem<Item> registerTieredSword(String id, Tier tier, int damage, float speed, Rarity rarity) {
        return ITEMS.register(id, () -> new SwordItem(tier,
                new Item.Properties().attributes(SwordItem.createAttributes(tier, damage, speed))
                        .stacksTo(1).rarity(rarity)));
    }

    private static DeferredItem<Item> registerTieredHoe(String id, Tier tier, int damage, float speed, Rarity rarity) {
        return ITEMS.register(id, () -> new HoeItem(tier,
                new Item.Properties().attributes(HoeItem.createAttributes(tier, damage, speed))
                        .rarity(rarity)));
    }

}

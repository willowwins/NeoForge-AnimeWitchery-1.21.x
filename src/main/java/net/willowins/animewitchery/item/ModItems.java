package net.willowins.animewitchery.item;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.fluid.ModFluids;
import net.willowins.animewitchery.item.custom.*;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnimeWitchery.MOD_ID);

    public static final DeferredItem<Item> BLAZESACK = ITEMS.register("blaze_powder_sack",
            () ->new Item(new Item.Properties()));
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
    public static final DeferredItem<Item> SILVER_SMITHING_TEMPLATE = ITEMS.register("silver_smithing_template",
            () ->new Item(new Item.Properties()));

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

}

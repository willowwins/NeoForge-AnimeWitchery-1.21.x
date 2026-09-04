package net.willowins.animewitchery;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    public static final ModConfigSpec.IntValue LUNA_RANGE = BUILDER
            .comment("How many blocks above and below the Luna Crystal it will fill containers with starlight.")
            .defineInRange("lunaCrystalRange", 5, 0, 32);

    public static final ModConfigSpec.DoubleValue LUNA_BASE_RATE = BUILDER
            .comment("Base starlight fill rate in millibuckets per second (no glass above).")
            .defineInRange("lunaCrystalBaseRate", 1.0, 0.1, 1000.0);

    public static final ModConfigSpec.DoubleValue LUNA_BONUS_RATE = BUILDER
            .comment("Starlight fill rate in millibuckets per second when a 3x3 square of glass is above the crystal.")
            .defineInRange("lunaCrystalBonusRate", 2.0, 0.1, 1000.0);

    public static final ModConfigSpec.IntValue LUNA_BUFFER = BUILDER
            .comment("Internal starlight buffer capacity (mb) of the Luna Crystal.")
            .defineInRange("lunaCrystalBuffer", 1000, 100, 100000);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}

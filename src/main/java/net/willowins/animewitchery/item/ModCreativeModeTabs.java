package net.willowins.animewitchery.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AnimeWitchery.MOD_ID);

public static  final Supplier<CreativeModeTab> ANIME_WITCHERY_BASIC = CREATIVE_MODE_TAB.register("anime_witchery_basic",
        () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.BLAZE_POWDER_BAG.get()))
                .title(Component.translatable("itemtab.anime_witchery_basic"))
                .displayItems((ItemDisplayParameters, output) -> {
                    // Materials, tools, and alchemy supplies
                    output.accept(new ItemStack(ModItems.BLAZE_POWDER_BAG.get()));
                    output.accept(new ItemStack(ModItems.SILVERINGOT.get()));
                    output.accept(new ItemStack(ModItems.RAW_SILVER.get()));
                    output.accept(new ItemStack(ModItems.WAND.get()));
                    output.accept(new ItemStack(ModItems.STARDROP_WAND.get()));
                    output.accept(new ItemStack(ModItems.MANA_READER.get()));
                    output.accept(new ItemStack(ModItems.ALCHEMICAL_CATALYST.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_CATALYST.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_BUCKET.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_BOTTLE.get()));
                    output.accept(new ItemStack(ModItems.DIAMOND_SHARD.get()));
                    output.accept(new ItemStack(ModItems.STAR_DROP.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_DUST.get()));

                    output.accept(new ItemStack(ModItems.SILVER_TEMPLATE.get()));
                    output.accept(new ItemStack(ModItems.STAFF_HEAD.get()));
                    output.accept(new ItemStack(ModItems.VOID_ESSENCE.get()));
                    output.accept(new ItemStack(ModItems.RUNE_STONE.get()));
                    output.accept(new ItemStack(ModItems.BLOOD_RUNE_STONE.get()));
                    output.accept(new ItemStack(ModItems.SILVER_NUGGET.get()));
                    output.accept(new ItemStack(ModItems.SPOOL.get()));
                    output.accept(new ItemStack(ModItems.SILVER_SPOOL.get()));
                    output.accept(new ItemStack(ModItems.STAR_SHARD.get()));
                    output.accept(new ItemStack(ModItems.MASTER_KEY.get()));
                    output.accept(new ItemStack(ModItems.ALCHEMY_RECIPE_BOOK.get()));
                    output.accept(new ItemStack(ModItems.EMPTY_FLASK.get()));
                    output.accept(new ItemStack(ModItems.POTION_FLASK.get()));
                    output.accept(new ItemStack(ModItems.MORTAR_AND_PESTLE.get()));
                    output.accept(new ItemStack(ModItems.FIRE_RES_CRYSTAL.get()));
                    output.accept(new ItemStack(ModItems.SPEED_CRYSTAL.get()));
                    output.accept(new ItemStack(ModItems.MANA_ROCKET.get()));
                    output.accept(new ItemStack(ModItems.CREEPING_PREVAILANCE.get()));
                    output.accept(new ItemStack(ModItems.REPAIR_ESSENCE.get()));
                    output.accept(new ItemStack(ModItems.EMERGENCY_RECALL.get()));
                    output.accept(new ItemStack(ModItems.DIVINING_ROD.get()));
                    output.accept(new ItemStack(ModItems.TORCH_FLOWER_ESSENCE.get()));
                    output.accept(new ItemStack(ModItems.WEATHERITEM.get()));
                    output.accept(new ItemStack(ModItems.RESPAWN_BEACON.get()));
                    output.accept(new ItemStack(ModItems.METAL_DETECTOR.get()));
                    output.accept(new ItemStack(ModItems.SOUL_JAR.get()));
                    output.accept(new ItemStack(ModItems.SOUL.get()));
                    output.accept(new ItemStack(ModItems.SUMMONER_STAFF.get()));
                    output.accept(new ItemStack(ModItems.SCHEELITE_CRYSTAL.get()));
                    output.accept(new ItemStack(ModItems.SCHEELITE_DUST.get()));
                    output.accept(new ItemStack(ModItems.SCHEELITE_SLURRY.get()));
                    output.accept(new ItemStack(ModItems.OBELISK_SHARD.get()));
                    output.accept(new ItemStack(ModItems.ENCHANTED_CRYSTAL.get()));
                    output.accept(new ItemStack(ModItems.BONE_DUST.get()));
                    output.accept(new ItemStack(ModItems.AMETHYST_DUST.get()));
                    output.accept(new ItemStack(ModItems.STARDUST.get()));
                    output.accept(new ItemStack(ModItems.BARRIER_CATALYST.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_SCRAP.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_INGOT.get()));

                    // Food and brewing
                    output.accept(new ItemStack(ModItems.LEMON.get()));
                    output.accept(new ItemStack(ModItems.STRAWBERRY.get()));
                    output.accept(new ItemStack(ModItems.TART_CRUST.get()));
                    output.accept(new ItemStack(ModItems.UNBAKED_LEMON_TART.get()));
                    output.accept(new ItemStack(ModItems.UNBAKED_STRAWBERRY_TART.get()));
                    output.accept(new ItemStack(ModItems.LEMON_TART.get()));
                    output.accept(new ItemStack(ModItems.STRAWBERRY_TART.get()));
                    output.accept(new ItemStack(ModItems.ROSEWILLOW_BLOSSOM.get()));

                    output.accept(PotionContents.createItemStack(Items.POTION, net.willowins.animewitchery.potion.ModPotions.MANA_REGEN));
                    output.accept(PotionContents.createItemStack(Items.POTION, net.willowins.animewitchery.potion.ModPotions.LONG_MANA_REGEN));
                    output.accept(PotionContents.createItemStack(Items.POTION, net.willowins.animewitchery.potion.ModPotions.STRONG_MANA_REGEN));

                    // Mining and utility equipment
                    output.accept(new ItemStack(ModItems.SILVER_PICKAXE.get()));
                    output.accept(new ItemStack(ModItems.SILVER_AXE.get()));
                    output.accept(new ItemStack(ModItems.SILVER_SHOVEL.get()));
                    output.accept(new ItemStack(ModItems.SILVER_HOE.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_PICKAXE.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_AXE.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_SHOVEL.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_HOE.get()));
                    output.accept(new ItemStack(ModItems.OBELISK_PICKAXE.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_PICKAXE.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_AXE.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_SHOVEL.get()));
                    output.accept(new ItemStack(ModItems.HALOIC_HOE.get()));

                    output.accept(new ItemStack(ModItems.FUEL_ROD.get()));
                    output.accept(new ItemStack(ModItems.OVERHEATED_FUEL_ROD.get()));
                    output.accept(new ItemStack(ModItems.HUD.get()));
                    output.accept(new ItemStack(ModItems.HUD_ALT_1.get()));

                    // Building and functional blocks
                    output.accept(new ItemStack(ModBlocks.SILVER_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.ALCHEMICAL_CATALYST_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.RESONANT_CATALYST_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.DEEPSLATE_SILVER_ORE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_ORE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_BUTTON.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_PRESSURE_PLATE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_FENCE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_FENCE_GATE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_WALL.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_DOOR.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_TRAPDOOR.get()));
                    output.accept(new ItemStack(ModBlocks.CHARCOAL_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.CAUTION_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.CAUTION_BLOCK_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.CAUTION_BLOCK_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.IRON_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.IRON_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.COPPER_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.COPPER_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.GOLD_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.GOLD_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.DIAMOND_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.DIAMOND_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.NETHERITE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.NETHERITE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.WHITE_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.WHITE_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.ORANGE_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.ORANGE_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.MAGENTA_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.MAGENTA_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.LIGHT_BLUE_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.LIGHT_BLUE_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.YELLOW_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.YELLOW_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.LIME_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.LIME_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.PINK_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.PINK_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.GRAY_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.GRAY_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.LIGHT_GRAY_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.LIGHT_GRAY_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.CYAN_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.CYAN_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.PURPLE_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.PURPLE_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.BLUE_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.BLUE_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.BROWN_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.BROWN_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.GREEN_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.GREEN_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.RED_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.RED_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.BLACK_CONCRETE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.BLACK_CONCRETE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_WALL.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_BRICKS.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_BRICK_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_BRICK_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_BRICK_WALL.get()));
                    output.accept(new ItemStack(ModBlocks.MARBLE_PILLAR.get()));
                    output.accept(new ItemStack(ModBlocks.POLISHED_MARBLE.get()));
                    output.accept(new ItemStack(ModBlocks.POLISHED_MARBLE_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.POLISHED_MARBLE_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.POLISHED_MARBLE_WALL.get()));
                    output.accept(new ItemStack(ModBlocks.LUNA_CRYSTAL.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_POOL.get()));
                    output.accept(new ItemStack(ModBlocks.STARDROP_LANTERN.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_INFUSION_PEDESTAL.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_INFUSION_ALTAR.get()));
                    output.accept(new ItemStack(ModBlocks.ANCIENT_VARIABLE_SWITCH.get()));
                    output.accept(new ItemStack(ModBlocks.CRYSTALLIZED_STARLIGHT.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_BRICK.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_BRICK_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_BRICK_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_FLAGSTONE.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_GRASS.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_PLANKS.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_PLANKS_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_PLANKS_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_LOG.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_LEAVES.get()));
                    output.accept(new ItemStack(ModBlocks.DARK_SAPLING.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_LOG.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_LOG_BLOOMING.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_LEAVES.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_PLANKS.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_FENCE.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_FENCE_GATE.get()));
                    output.accept(new ItemStack(ModBlocks.ROSEWILLOW_SAPLING.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_BRICK.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_FLOOR.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_BRICK_STAIRS.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_BRICK_SLAB.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_LEAVES.get()));
                    output.accept(new ItemStack(ModBlocks.RUIN_LEAF_LITTER.get()));
                    output.accept(new ItemStack(ModBlocks.BLUME.get()));
                    output.accept(new ItemStack(ModBlocks.ANTIFOUNTAIN.get()));
                    output.accept(new ItemStack(ModBlocks.POCKET_VEIL.get()));
                    output.accept(new ItemStack(ModBlocks.POCKET_RETURN.get()));
                    output.accept(new ItemStack(ModBlocks.POCKET_EXPANSION_JIGSAW.get()));


                }).build());

public static  final Supplier<CreativeModeTab> ANIME_WITCHERY_COMBAT = CREATIVE_MODE_TAB.register("anime_witchery_combat",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.SILVER_SWORD.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "anime_witchery_basic"))
                    .title(Component.translatable("itemtab.anime_witchery_combat"))
                    .displayItems((ItemDisplayParameters, output) -> {
                        // Remaining class armor sets
                        output.accept(new ItemStack(ModItems.RAILGUNNER_HELMET.get()));
                        output.accept(new ItemStack(ModItems.RAILGUNNER_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.RAILGUNNER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.RAILGUNNER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.OBELISK_HELMET.get()));
                        output.accept(new ItemStack(ModItems.OBELISK_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.OBELISK_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.OBELISK_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.PALADIN_HELMET.get()));
                        output.accept(new ItemStack(ModItems.PALADIN_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.PALADIN_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.PALADIN_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.SANGUINE_HOOD.get()));
                        output.accept(new ItemStack(ModItems.HOODED_CAPE.get()));
                        output.accept(new ItemStack(ModItems.SANGUINE_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.SANGUINE_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.DEATHBRINGER_HOOD.get()));
                        output.accept(new ItemStack(ModItems.DEATHBRINGER_ROBES.get()));
                        output.accept(new ItemStack(ModItems.DEATHBRINGER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.DEATHBRINGER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.SUMMONER_HOOD.get()));
                        output.accept(new ItemStack(ModItems.SUMMONER_ROBES.get()));
                        output.accept(new ItemStack(ModItems.SUMMONER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.SUMMONER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.MAGE_HAT.get()));
                        output.accept(new ItemStack(ModItems.MAGE_ROBES.get()));
                        output.accept(new ItemStack(ModItems.MAGE_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.MAGE_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.STRAW_HAT.get()));
                        output.accept(new ItemStack(ModItems.FARMER_OVERALLS.get()));
                        output.accept(new ItemStack(ModItems.FARMER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.FARMER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.PROSPECTOR_HAT.get()));
                        output.accept(new ItemStack(ModItems.PROSPECTOR_TUNIC.get()));
                        output.accept(new ItemStack(ModItems.PROSPECTOR_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.PROSPECTOR_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.HOARDER_HELMET.get()));
                        output.accept(new ItemStack(ModItems.HOARDER_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.HOARDER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.HOARDER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.DRUID_HOOD.get()));
                        output.accept(new ItemStack(ModItems.COTTON_GOWN.get()));
                        output.accept(new ItemStack(ModItems.DRUID_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.DRUID_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.ALCHEMIST_HOOD.get()));
                        output.accept(new ItemStack(ModItems.STAINED_ROBES.get()));
                        output.accept(new ItemStack(ModItems.ALCHEMIST_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.ALCHEMIST_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.COWBOY_HAT.get()));
                        output.accept(new ItemStack(ModItems.GUNSLINGER_VEST.get()));
                        output.accept(new ItemStack(ModItems.CHAPS.get()));
                        output.accept(new ItemStack(ModItems.GUNSLINGER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.GAMBLER_VISOR.get()));
                        output.accept(new ItemStack(ModItems.SOILED_JACKET.get()));
                        output.accept(new ItemStack(ModItems.GAMBLER_PANTS.get()));
                        output.accept(new ItemStack(ModItems.GAMBLER_SHOES.get()));
                        output.accept(new ItemStack(ModItems.CAT_EARS.get()));
                        output.accept(new ItemStack(ModItems.FEMBOY_HOODIE.get()));
                        output.accept(new ItemStack(ModItems.THIGH_HIGHS.get()));
                        output.accept(new ItemStack(ModItems.PLATFORM_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.CROWN.get()));
                        output.accept(new ItemStack(ModItems.PLATE_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.PLATE_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.PLATE_BOOTS.get()));

                        // Weapons and armor
                        output.accept(new ItemStack(ModItems.ASSASSIN_DAGGER.get()));
                        output.accept(new ItemStack(ModItems.ASSASSIN_HOOD.get()));
                        output.accept(new ItemStack(ModItems.ASSASSIN_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.ASSASSIN_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.ASSASSIN_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.BUTCHER_KNIFE.get()));
                        output.accept(new ItemStack(ModItems.BUTCHER_HELMET.get()));
                        output.accept(new ItemStack(ModItems.BUTCHER_APRON.get()));
                        output.accept(new ItemStack(ModItems.BUTCHER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.BUTCHER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.HEAVY_MACE.get()));
                        output.accept(new ItemStack(ModItems.HEAVY_PLATE_HELMET.get()));
                        output.accept(new ItemStack(ModItems.HEAVY_PLATE_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.HEAVY_PLATE_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.HEAVY_PLATE_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.IRON_SHACKLE.get()));
                        output.accept(new ItemStack(ModItems.MONK_HEAD.get()));
                        output.accept(new ItemStack(ModItems.SPARSE_TUNIC.get()));
                        output.accept(new ItemStack(ModItems.MONK_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.MONK_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.BLOODLETTER.get()));
                        output.accept(new ItemStack(ModItems.COPPER_WARHAMMER.get()));
                        output.accept(new ItemStack(ModItems.GREAT_HAMMER.get()));
                        output.accept(new ItemStack(ModItems.LONGSWORD.get()));
                        output.accept(new ItemStack(ModItems.OATHBREAKER.get()));
                        output.accept(new ItemStack(ModItems.OBELISK_SWORD.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_GREATSWORD.get()));
                        output.accept(new ItemStack(ModItems.SWORD_OF_MIDAS.get()));
                        output.accept(new ItemStack(ModItems.WOODEN_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.STONE_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.COPPER_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.IRON_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.DIAMOND_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.NETHERITE_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.DEATHBRINGER_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.SOUL_SCYTHE.get()));
                        output.accept(new ItemStack(ModItems.SILVER_SWORD.get()));
                        output.accept(new ItemStack(ModItems.SILVER_HELMET.get()));
                        output.accept(new ItemStack(ModItems.SILVER_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.SILVER_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.SILVER_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_SWORD.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_HELMET.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_BOOTS.get()));
                        output.accept(new ItemStack(ModItems.HALOIC_SWORD.get()));
                        output.accept(new ItemStack(ModItems.HALOIC_HELMET.get()));
                        output.accept(new ItemStack(ModItems.HALOIC_CHESTPLATE.get()));
                        output.accept(new ItemStack(ModItems.HALOIC_LEGGINGS.get()));
                        output.accept(new ItemStack(ModItems.HALOIC_BOOTS.get()));
                        // Spellcraft
                        output.accept(new ItemStack(ModItems.FIRE_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.WATER_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.EARTH_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.AIR_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.LIFE_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.DEATH_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.LIGHT_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.DARKNESS_RUNE_STONE.get()));
                        output.accept(new ItemStack(ModItems.FIRE_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.WATER_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.EARTH_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.AIR_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.LIFE_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.DEATH_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.LIGHT_SPELL_SCROLL.get()));
                        output.accept(new ItemStack(ModItems.DARKNESS_SPELL_SCROLL.get()));
                        // Accessories
                        output.accept(new ItemStack(ModItems.SILVER_PENDANT.get()));
                        output.accept(new ItemStack(ModItems.LATENT_MANA_PENDANT.get()));
                        output.accept(new ItemStack(ModItems.FULFILLING_TIARA.get()));
                        output.accept(new ItemStack(ModItems.RESONANT_TIARA.get()));
                        output.accept(new ItemStack(ModItems.DEEP_DARK_DEEP_DISH.get()));

                    }).build());




    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}

package net.willowins.animewitchery.block;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.custom.AncientVariableSwitchBlock;
import net.willowins.animewitchery.block.custom.AlchemyTableBlock;
import net.willowins.animewitchery.block.custom.AntifluidBlock;
import net.willowins.animewitchery.block.custom.AntifountainBlock;
import net.willowins.animewitchery.block.custom.CrystallizedStarlightBlock;
import net.willowins.animewitchery.block.custom.DarkFountainBlock;
import net.willowins.animewitchery.block.custom.DarkPedestalBlock;
import net.willowins.animewitchery.block.custom.DarkSaplingBlock;
import net.willowins.animewitchery.block.custom.PocketExpansionJigsawBlock;
import net.willowins.animewitchery.block.custom.PocketReturnBlock;
import net.willowins.animewitchery.block.custom.PocketVeilBlock;
import net.willowins.animewitchery.block.custom.RuinLeafLitterBlock;
import net.willowins.animewitchery.block.custom.LunaCrystalBlock;
import net.willowins.animewitchery.block.custom.StardropLanternBlock;
import net.willowins.animewitchery.block.custom.StarlightInfusionAltarBlock;
import net.willowins.animewitchery.block.custom.StarlightInfusionPedestalBlock;
import net.willowins.animewitchery.block.custom.StarlightLiquidBlock;
import net.willowins.animewitchery.block.custom.StarlightPoolBlock;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.fluid.ModFluids;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnimeWitchery.MOD_ID);

    public static final DeferredBlock<LiquidBlock> STARLIGHT_BLOCK = BLOCKS.register("starlight",
            () -> new StarlightLiquidBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 15).randomTicks()));
    public static final DeferredBlock<LiquidBlock> STARLIGHT_BLOCK_ALT = BLOCKS.register("starlight_block",
            () -> new StarlightLiquidBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 15).randomTicks()));

    public static final DeferredBlock<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> ALCHEMICAL_CATALYST_BLOCK = registerBlock("alchemical_catalyst_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                    .strength(3.0F, 7.0F).lightLevel(state -> 2)));
    public static final DeferredBlock<Block> RESONANT_CATALYST_BLOCK = registerBlock("resonant_catalyst_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.AMETHYST_BLOCK)
                    .strength(4.0F, 8.0F).lightLevel(state -> 10)));

    public static final DeferredBlock<Block> RUIN_BRICK = registerBlock("ruin_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DARK_BRICK = registerBlock("dark_brick",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> DARK_GRASS = registerBlock("dark_grass",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.WET_GRASS)));

    public static final DeferredBlock<Block> DARK_BRICK_STAIRS = registerBlock("dark_brick_stairs",
            () -> new StairBlock(DARK_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F).sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> DARK_BRICK_SLAB = registerBlock("dark_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> DARK_FLAGSTONE = registerBlock("dark_flagstone",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> DARK_PLANKS = registerBlock("dark_planks",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(2.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    // Rosewillow building set ported from Fabric. Use vanilla wood shapes so the
    // supplied blockstates (slab/stair/fence properties) behave as expected.
    public static final DeferredBlock<Block> ROSEWILLOW_PLANKS = registerBlock("rosewillow_planks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F)));

    public static final DeferredBlock<Block> ROSEWILLOW_STAIRS = registerBlock("rosewillow_stairs",
            () -> new StairBlock(ROSEWILLOW_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));

    public static final DeferredBlock<Block> ROSEWILLOW_SLAB = registerBlock("rosewillow_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final DeferredBlock<Block> ROSEWILLOW_FENCE = registerBlock("rosewillow_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));

    public static final DeferredBlock<Block> ROSEWILLOW_FENCE_GATE = registerBlock("rosewillow_fence_gate",
            () -> new FenceGateBlock(WoodType.OAK,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));

    public static final DeferredBlock<Block> SCHEELITE_BLOCK = registerBlock("scheelite_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> ALCHEMY_TABLE = registerBlock("alchemy_table",
            () -> new AlchemyTableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                    .strength(1.5F, 6.0F).noOcclusion()));

    public static final DeferredBlock<Block> DARK_PLANKS_STAIRS = registerBlock("dark_planks_stairs",
            () -> new StairBlock(DARK_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .strength(2.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DARK_PLANKS_SLAB = registerBlock("dark_planks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DARK_SAPLING = registerBlock("dark_sapling",
            () -> new DarkSaplingBlock(
                    new TreeGrower("dark_tree",
                            java.util.Optional.of(DarkSaplingBlock.DARK_TREE_KEY),
                            java.util.Optional.of(DarkSaplingBlock.DARK_MEGA_TREE_KEY),
                            java.util.Optional.empty()),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    public static final DeferredBlock<Block> RUIN_LEAVES = registerBlock("ruin_leaves",
            () -> new net.minecraft.world.level.block.LeavesBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GRASS).requiresCorrectToolForDrops().randomTicks().noOcclusion()));

    public static final DeferredBlock<Block> RUIN_LEAF_LITTER = registerBlock("ruin_leaf_litter",
            () -> new RuinLeafLitterBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GRASS).noOcclusion()));

    public static final DeferredBlock<Block> BLUME = registerBlock("blume",
            () -> new net.minecraft.world.level.block.TallFlowerBlock(
                    BlockBehaviour.Properties.of()
                            .strength(0.0F).sound(SoundType.GRASS).noOcclusion().noCollission()));

    public static final DeferredBlock<Block> RUIN_FLOOR = registerBlock("ruin_floor",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> RUIN_BRICK_STAIRS = registerBlock("ruin_brick_stairs",
            () -> new StairBlock(RUIN_BRICK.get().defaultBlockState(),
                    BlockBehaviour.Properties.of()
                            .strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> RUIN_BRICK_SLAB = registerBlock("ruin_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of()
                    .strength(-1.0F, 3600000.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> LUNA_CRYSTAL = registerBlock("luna_crystal",
            () -> new LunaCrystalBlock(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.AMETHYST).noOcclusion()));

    public static final DeferredBlock<Block> STARLIGHT_POOL = registerBlock("starlight_pool",
            () -> new StarlightPoolBlock(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));


    public static final DeferredBlock<Block> STARDROP_LANTERN = registerBlock("stardrop_lantern",
            () -> new StardropLanternBlock(BlockBehaviour.Properties.of()
                    .strength(3.5F).requiresCorrectToolForDrops().lightLevel(state -> 15).sound(SoundType.LANTERN)));

    public static final DeferredBlock<Block> STARLIGHT_INFUSION_PEDESTAL = registerBlock("starlight_infusion_pedestal",
            () -> new StarlightInfusionPedestalBlock(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));

    public static final DeferredBlock<Block> STARLIGHT_INFUSION_ALTAR = registerBlock("starlight_infusion_altar",
            () -> new StarlightInfusionAltarBlock(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion().lightLevel(state -> 12)));

    public static final DeferredBlock<Block> SILVER_ORE = registerBlock("silver_ore",
            () -> new DropExperienceBlock(UniformInt.of(8,10),
                    BlockBehaviour.Properties.of()
                    .strength(3F).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_SILVER_ORE = registerBlock("deepslate_silver_ore",
            () -> new DropExperienceBlock(UniformInt.of(10,12),
                    BlockBehaviour.Properties.of()
                    .strength(3F).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ANCIENT_VARIABLE_SWITCH = registerBlock("ancient_variable_switch",
            () -> new AncientVariableSwitchBlock(BlockBehaviour.Properties.of()
                    .strength(2F).sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<Block> CRYSTALLIZED_STARLIGHT = registerBlock("crystallized_starlight",
            () -> new CrystallizedStarlightBlock(BlockBehaviour.Properties.of()
                    .strength(0.5F).sound(SoundType.AMETHYST).noOcclusion().lightLevel(state -> 8)));

    public static final DeferredBlock<Block> ANTIFOUNTAIN = registerBlock("antifountain",
            () -> new AntifountainBlock(BlockBehaviour.Properties.of()
                    .strength(6F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE).lightLevel(state -> 3)));

    public static final DeferredBlock<Block> POCKET_VEIL = registerBlock("pocket_veil",
            () -> new PocketVeilBlock(BlockBehaviour.Properties.of()
                    .strength(0.3F).sound(SoundType.GLASS).noCollission()));

    public static final DeferredBlock<Block> POCKET_RETURN = registerBlock("pocket_return",
            () -> new PocketReturnBlock(BlockBehaviour.Properties.of()
                    .strength(0.3F).sound(SoundType.GLASS)));

    public static final DeferredBlock<Block> POCKET_EXPANSION_JIGSAW = registerBlock("pocket_expansion_jigsaw",
            () -> new PocketExpansionJigsawBlock(BlockBehaviour.Properties.of()
                    .strength(2F).sound(SoundType.DEEPSLATE_TILES).noOcclusion()));

    public static final DeferredBlock<DarkFountainBlock> DARK_FOUNTAIN = registerBlock("dark_fountain",
            () -> new DarkFountainBlock(BlockBehaviour.Properties.of()
                    .strength(6F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<DarkPedestalBlock> DARK_PEDESTAL = registerBlock("dark_pedestal",
            () -> new DarkPedestalBlock(BlockBehaviour.Properties.of()
                    .strength(6F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE_TILES)));

    public static final DeferredBlock<Block> DARK_LOG = registerBlock("dark_log",
            () -> new net.minecraft.world.level.block.RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .strength(2.0F).sound(SoundType.WOOD).requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> DARK_LEAVES = registerBlock("dark_leaves",
            () -> new net.minecraft.world.level.block.LeavesBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GRASS).requiresCorrectToolForDrops().randomTicks().noOcclusion()));

    // Silver set
    public static final DeferredBlock<Block> SILVER_STAIRS = registerBlock("silver_stairs",
            () -> new StairBlock(SILVER_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> SILVER_SLAB = registerBlock("silver_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> SILVER_BUTTON = registerBlock("silver_button",
            () -> new ButtonBlock(BlockSetType.IRON, 30,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noCollission()));

    public static final DeferredBlock<Block> SILVER_PRESSURE_PLATE = registerBlock("silver_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.IRON,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK).noCollission()));

    public static final DeferredBlock<Block> SILVER_FENCE = registerBlock("silver_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> SILVER_FENCE_GATE = registerBlock("silver_fence_gate",
            () -> new FenceGateBlock(WoodType.DARK_OAK,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> SILVER_WALL = registerBlock("silver_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> SILVER_DOOR = registerBlock("silver_door",
            () -> new DoorBlock(BlockSetType.IRON,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_DOOR).noOcclusion()));

    public static final DeferredBlock<Block> SILVER_TRAPDOOR = registerBlock("silver_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.IRON,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_TRAPDOOR).noOcclusion()));

    // Charcoal & caution
    public static final DeferredBlock<Block> CHARCOAL_BLOCK = registerBlock("charcoal_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK)));

    public static final DeferredBlock<Block> CAUTION_BLOCK = registerBlock("caution_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(2.0F).sound(SoundType.STONE)));

    public static final DeferredBlock<Block> CAUTION_BLOCK_SLAB = registerBlock("caution_block_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(CAUTION_BLOCK.get())));

    public static final DeferredBlock<Block> CAUTION_BLOCK_STAIRS = registerBlock("caution_block_stairs",
            () -> new StairBlock(CAUTION_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(CAUTION_BLOCK.get())));

    // Metal stairs & slabs
    public static final DeferredBlock<Block> IRON_STAIRS = registerBlock("iron_stairs",
            () -> new StairBlock(Blocks.IRON_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));
    public static final DeferredBlock<Block> IRON_SLAB = registerBlock("iron_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)));

    public static final DeferredBlock<Block> COPPER_STAIRS = registerBlock("copper_stairs",
            () -> new StairBlock(Blocks.COPPER_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));
    public static final DeferredBlock<Block> COPPER_SLAB = registerBlock("copper_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_BLOCK)));

    public static final DeferredBlock<Block> GOLD_STAIRS = registerBlock("gold_stairs",
            () -> new StairBlock(Blocks.GOLD_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final DeferredBlock<Block> GOLD_SLAB = registerBlock("gold_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK)));

    public static final DeferredBlock<Block> DIAMOND_STAIRS = registerBlock("diamond_stairs",
            () -> new StairBlock(Blocks.DIAMOND_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));
    public static final DeferredBlock<Block> DIAMOND_SLAB = registerBlock("diamond_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_BLOCK)));

    public static final DeferredBlock<Block> NETHERITE_STAIRS = registerBlock("netherite_stairs",
            () -> new StairBlock(Blocks.NETHERITE_BLOCK.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
    public static final DeferredBlock<Block> NETHERITE_SLAB = registerBlock("netherite_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));

    // Concrete stairs & slabs
    public static final DeferredBlock<Block> WHITE_CONCRETE_STAIRS = registerBlock("white_concrete_stairs",
            () -> new StairBlock(Blocks.WHITE_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));
    public static final DeferredBlock<Block> WHITE_CONCRETE_SLAB = registerBlock("white_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CONCRETE)));

    public static final DeferredBlock<Block> ORANGE_CONCRETE_STAIRS = registerBlock("orange_concrete_stairs",
            () -> new StairBlock(Blocks.ORANGE_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE)));
    public static final DeferredBlock<Block> ORANGE_CONCRETE_SLAB = registerBlock("orange_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_CONCRETE)));

    public static final DeferredBlock<Block> MAGENTA_CONCRETE_STAIRS = registerBlock("magenta_concrete_stairs",
            () -> new StairBlock(Blocks.MAGENTA_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE)));
    public static final DeferredBlock<Block> MAGENTA_CONCRETE_SLAB = registerBlock("magenta_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_CONCRETE)));

    public static final DeferredBlock<Block> LIGHT_BLUE_CONCRETE_STAIRS = registerBlock("light_blue_concrete_stairs",
            () -> new StairBlock(Blocks.LIGHT_BLUE_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE)));
    public static final DeferredBlock<Block> LIGHT_BLUE_CONCRETE_SLAB = registerBlock("light_blue_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_CONCRETE)));

    public static final DeferredBlock<Block> YELLOW_CONCRETE_STAIRS = registerBlock("yellow_concrete_stairs",
            () -> new StairBlock(Blocks.YELLOW_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE)));
    public static final DeferredBlock<Block> YELLOW_CONCRETE_SLAB = registerBlock("yellow_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_CONCRETE)));

    public static final DeferredBlock<Block> LIME_CONCRETE_STAIRS = registerBlock("lime_concrete_stairs",
            () -> new StairBlock(Blocks.LIME_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE)));
    public static final DeferredBlock<Block> LIME_CONCRETE_SLAB = registerBlock("lime_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_CONCRETE)));

    public static final DeferredBlock<Block> PINK_CONCRETE_STAIRS = registerBlock("pink_concrete_stairs",
            () -> new StairBlock(Blocks.PINK_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE)));
    public static final DeferredBlock<Block> PINK_CONCRETE_SLAB = registerBlock("pink_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_CONCRETE)));

    public static final DeferredBlock<Block> GRAY_CONCRETE_STAIRS = registerBlock("gray_concrete_stairs",
            () -> new StairBlock(Blocks.GRAY_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE)));
    public static final DeferredBlock<Block> GRAY_CONCRETE_SLAB = registerBlock("gray_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE)));

    public static final DeferredBlock<Block> LIGHT_GRAY_CONCRETE_STAIRS = registerBlock("light_gray_concrete_stairs",
            () -> new StairBlock(Blocks.LIGHT_GRAY_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE)));
    public static final DeferredBlock<Block> LIGHT_GRAY_CONCRETE_SLAB = registerBlock("light_gray_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_CONCRETE)));

    public static final DeferredBlock<Block> CYAN_CONCRETE_STAIRS = registerBlock("cyan_concrete_stairs",
            () -> new StairBlock(Blocks.CYAN_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE)));
    public static final DeferredBlock<Block> CYAN_CONCRETE_SLAB = registerBlock("cyan_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_CONCRETE)));

    public static final DeferredBlock<Block> PURPLE_CONCRETE_STAIRS = registerBlock("purple_concrete_stairs",
            () -> new StairBlock(Blocks.PURPLE_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE)));
    public static final DeferredBlock<Block> PURPLE_CONCRETE_SLAB = registerBlock("purple_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_CONCRETE)));

    public static final DeferredBlock<Block> BLUE_CONCRETE_STAIRS = registerBlock("blue_concrete_stairs",
            () -> new StairBlock(Blocks.BLUE_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE)));
    public static final DeferredBlock<Block> BLUE_CONCRETE_SLAB = registerBlock("blue_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_CONCRETE)));

    public static final DeferredBlock<Block> BROWN_CONCRETE_STAIRS = registerBlock("brown_concrete_stairs",
            () -> new StairBlock(Blocks.BROWN_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE)));
    public static final DeferredBlock<Block> BROWN_CONCRETE_SLAB = registerBlock("brown_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_CONCRETE)));

    public static final DeferredBlock<Block> GREEN_CONCRETE_STAIRS = registerBlock("green_concrete_stairs",
            () -> new StairBlock(Blocks.GREEN_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE)));
    public static final DeferredBlock<Block> GREEN_CONCRETE_SLAB = registerBlock("green_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_CONCRETE)));

    public static final DeferredBlock<Block> RED_CONCRETE_STAIRS = registerBlock("red_concrete_stairs",
            () -> new StairBlock(Blocks.RED_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)));
    public static final DeferredBlock<Block> RED_CONCRETE_SLAB = registerBlock("red_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_CONCRETE)));

    public static final DeferredBlock<Block> BLACK_CONCRETE_STAIRS = registerBlock("black_concrete_stairs",
            () -> new StairBlock(Blocks.BLACK_CONCRETE.defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_CONCRETE)));
    public static final DeferredBlock<Block> BLACK_CONCRETE_SLAB = registerBlock("black_concrete_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_CONCRETE)));

    // Marble Blocks
    public static final DeferredBlock<Block> MARBLE = registerBlock("marble",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_STAIRS = registerBlock("marble_stairs",
            () -> new StairBlock(MARBLE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_SLAB = registerBlock("marble_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_WALL = registerBlock("marble_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_BRICKS = registerBlock("marble_bricks",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_BRICK_STAIRS = registerBlock("marble_brick_stairs",
            () -> new StairBlock(MARBLE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_BRICK_SLAB = registerBlock("marble_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_BRICK_WALL = registerBlock("marble_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MARBLE_PILLAR = registerBlock("marble_pillar",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> POLISHED_MARBLE = registerBlock("polished_marble",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(state -> 10)));
    public static final DeferredBlock<Block> POLISHED_MARBLE_STAIRS = registerBlock("polished_marble_stairs",
            () -> new StairBlock(POLISHED_MARBLE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(state -> 10)));
    public static final DeferredBlock<Block> POLISHED_MARBLE_SLAB = registerBlock("polished_marble_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(state -> 10)));
    public static final DeferredBlock<Block> POLISHED_MARBLE_WALL = registerBlock("polished_marble_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(state -> 10)));
    public static final DeferredBlock<Block> DEEPSLATE_SCHEELITE_ORE = registerBlock("deepslate_scheelite_ore",
            () -> new net.minecraft.world.level.block.DropExperienceBlock(
                    net.minecraft.util.valueproviders.UniformInt.of(5, 8),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));
    public static final DeferredBlock<Block> ROSEWILLOW_LOG = registerBlock("rosewillow_log",
            () -> new net.minecraft.world.level.block.RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> ROSEWILLOW_LOG_BLOOMING = registerBlock("rosewillow_log_blooming",
            () -> new net.minecraft.world.level.block.RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredBlock<Block> ROSEWILLOW_LEAVES = registerBlock("rosewillow_leaves",
            () -> new net.minecraft.world.level.block.LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    public static final DeferredBlock<Block> ROSEWILLOW_SAPLING = registerBlock("rosewillow_sapling",
            () -> new net.minecraft.world.level.block.SaplingBlock(
                    new TreeGrower("rosewillow",
                            java.util.Optional.of(net.minecraft.resources.ResourceKey.create(
                                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "rosewillow_manual"))),
                            java.util.Optional.of(net.minecraft.resources.ResourceKey.create(
                                    net.minecraft.core.registries.Registries.CONFIGURED_FEATURE,
                                    net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "rosewillow_mega_manual"))),
                            java.util.Optional.empty()),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    public static final DeferredBlock<LiquidBlock> LIQUIFIED_SKULK_BLOCK = BLOCKS.register("liquified_skulk",
            () -> new LiquidBlock(ModFluids.LIQUIFIED_SKULK_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 5)));

    public static final DeferredBlock<LiquidBlock> LIQUID_EXPERIENCE_BLOCK = BLOCKS.register("liquid_experience",
            () -> new LiquidBlock(ModFluids.LIQUID_EXPERIENCE_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 10)));

    public static final DeferredBlock<LiquidBlock> OBSIDIAN_TEARS_BLOCK = BLOCKS.register("obsidian_tears",
            () -> new LiquidBlock(ModFluids.OBSIDIAN_TEARS_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 3)));

    public static final DeferredBlock<LiquidBlock> BLOOD_BLOCK = BLOCKS.register("blood",
            () -> new LiquidBlock(ModFluids.BLOOD_SOURCE.get(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER)));

    @SuppressWarnings("unchecked")
    public static final DeferredBlock<LiquidBlock> ANTIFLUID_BLOCK = (DeferredBlock<LiquidBlock>)(DeferredBlock<? extends Block>) BLOCKS.register("antifluid",
            () -> new AntifluidBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 12)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

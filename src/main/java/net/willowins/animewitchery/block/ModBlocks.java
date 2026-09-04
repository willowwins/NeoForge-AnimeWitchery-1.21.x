package net.willowins.animewitchery.block;

import net.minecraft.client.resources.model.Material;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.custom.AncientVariableSwitchBlock;
import net.willowins.animewitchery.block.custom.CrystallizedStarlightBlock;
import net.willowins.animewitchery.block.custom.LunaCrystalBlock;
import net.willowins.animewitchery.block.custom.StardropLanternBlock;
import net.willowins.animewitchery.block.custom.StarlightInfusionAltarBlock;
import net.willowins.animewitchery.block.custom.StarlightInfusionPedestalBlock;
import net.willowins.animewitchery.block.custom.StarlightLiquidBlock;
import net.willowins.animewitchery.block.custom.StarlightPoolBlock;
import net.willowins.animewitchery.item.ModItems;

import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.willowins.animewitchery.fluid.ModFluids;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AnimeWitchery.MOD_ID);

    public static final DeferredBlock<LiquidBlock> STARLIGHT_BLOCK = BLOCKS.register("starlight",
            () -> new StarlightLiquidBlock(
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).lightLevel(state -> 15).randomTicks()));

    public static final DeferredBlock<Block> SILVER_BLOCK = registerBlock("silver_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

    public static final DeferredBlock<Block> RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4F).requiresCorrectToolForDrops().sound(SoundType.METAL)));

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

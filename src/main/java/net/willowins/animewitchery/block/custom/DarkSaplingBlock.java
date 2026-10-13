package net.willowins.animewitchery.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.willowins.animewitchery.block.ModBlocks;

public class DarkSaplingBlock extends SaplingBlock {

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("animewitchery", "dark_cherry_manual"));

    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_MEGA_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath("animewitchery", "dark_oak_mega_manual"));

    public DarkSaplingBlock(TreeGrower treeGrower, BlockBehaviour.Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(ModBlocks.DARK_GRASS.get());
    }
}

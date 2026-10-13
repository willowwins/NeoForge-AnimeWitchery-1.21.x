package net.willowins.animewitchery.worldgen.feature;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.util.RandomSource;
import net.willowins.animewitchery.block.ModBlocks;

/** Generates the branching, broad-canopied Rosewillow tree used by its 2x2 sapling. */
public class RosewillowMegaTreeFeature extends Feature<NoneFeatureConfiguration> {
    public RosewillowMegaTreeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        if (!level.getBlockState(origin.below()).is(BlockTags.DIRT)) {
            return false;
        }

        int height = 27 + random.nextInt(8);
        for (int y = 0; y < height - 2; y++) {
            double radius = 2.5;
            if (y < 10) {
                double progress = (10.0 - y) / 10.0;
                radius += 3.5 * progress * progress;
                radius += (random.nextDouble() - 0.5) * 0.2;
            }
            generateTrunkLayer(level, origin.offset(0, y, 0), radius, origin, random);
        }

        generateRoots(level, origin, random);
        int branchCount = 18 + random.nextInt(6);
        int startY = height / 2;
        for (int i = 0; i < branchCount; i++) {
            float angle = random.nextFloat() * (float) (Math.PI * 2.0);
            int y = startY + random.nextInt(height - startY - 2);
            float relativeHeight = (float) (y - startY) / (height - startY);
            float pitch = -0.1F + relativeHeight * 0.9F;
            int length = (int) ((12 + random.nextInt(8)) * (1.2F - relativeHeight * 0.5F));
            generateBranch(level, origin.offset(0, y, 0), angle, pitch, length, random, origin);
        }

        generateCanopy(level, origin.offset(0, height, 0), 6, random, origin);
        return true;
    }

    private void generateTrunkLayer(WorldGenLevel level, BlockPos center, double radius, BlockPos origin, RandomSource random) {
        int extent = (int) Math.ceil(radius);
        for (int x = -extent; x <= extent; x++) {
            for (int z = -extent; z <= extent; z++) {
                double dx = x + 0.5;
                double dz = z + 0.5;
                if (dx * dx + dz * dz <= radius * radius) {
                    setRosewillowLog(level, center.offset(x, 0, z), ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState(), origin, random);
                }
            }
        }
    }

    private void generateBranch(WorldGenLevel level, BlockPos start, float angle, float pitch, int length,
                                RandomSource random, BlockPos origin) {
        double dx = Math.cos(angle);
        double dz = Math.sin(angle);
        double dy = Math.tan(pitch);
        double magnitude = Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= magnitude;
        dy /= magnitude;
        dz /= magnitude;

        for (int i = 0; i < length; i++) {
            BlockPos branchPos = start.offset((int) (dx * i), (int) (dy * i), (int) (dz * i));
            Direction.Axis axis = Math.abs(dx) >= Math.abs(dz) ? Direction.Axis.X : Direction.Axis.Z;
            BlockState branchLog = ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
            setRosewillowLog(level, branchPos, branchLog, origin, random);

            if (i > length * 0.3) {
                int radius = 4 + (int) (((float) i - length * 0.3F) / (length * 0.7F) * 3.0F);
                if (random.nextInt(3) == 0) {
                    generateCanopy(level, branchPos, radius, random, origin);
                }
            }
            if (i == length - 1) {
                generateCanopy(level, branchPos, 8, random, origin);
            }
        }
    }

    private void generateRoots(WorldGenLevel level, BlockPos origin, RandomSource random) {
        for (int i = 0; i < 8; i++) {
            double angle = random.nextFloat() * Math.PI * 2.0;
            int distance = 3 + random.nextInt(3);
            int dx = (int) (Math.cos(angle) * distance);
            int dz = (int) (Math.sin(angle) * distance);
            Direction.Axis axis = Math.abs(dx) >= Math.abs(dz) ? Direction.Axis.X : Direction.Axis.Z;
            BlockState root = ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, axis);
            setRosewillowLog(level, origin.offset(dx, 0, dz), root, origin, random);
            if (random.nextBoolean()) {
                setRosewillowLog(level, origin.offset(dx, -1, dz), root, origin, random);
            }
        }
    }

    private void generateCanopy(WorldGenLevel level, BlockPos center, int radius, RandomSource random, BlockPos origin) {
        if (radius > 5) {
            int branchOffset = radius / 2;
            BlockState branchX = ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
            BlockState branchZ = ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
            setRosewillowLog(level, center.offset(branchOffset, 0, 0), branchX, origin, random);
            setRosewillowLog(level, center.offset(-branchOffset, 0, 0), branchX, origin, random);
            setRosewillowLog(level, center.offset(0, 0, branchOffset), branchZ, origin, random);
            setRosewillowLog(level, center.offset(0, 0, -branchOffset), branchZ, origin, random);
            setRosewillowLog(level, center.offset(0, branchOffset, 0), ModBlocks.ROSEWILLOW_LOG.get().defaultBlockState(), origin, random);
        }

        for (int y = -radius / 2; y <= radius / 2; y++) {
            int layerRadius = radius - Math.abs(y);
            for (int x = -layerRadius; x <= layerRadius; x++) {
                for (int z = -layerRadius; z <= layerRadius; z++) {
                    if (x * x + z * z <= layerRadius * layerRadius) {
                        BlockPos leafPos = center.offset(x, y, z);
                        if (withinFeatureChunks(leafPos, origin) && level.isEmptyBlock(leafPos)) {
                            setBlock(level, leafPos, ModBlocks.ROSEWILLOW_LEAVES.get().defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private void setRosewillowLog(WorldGenLevel level, BlockPos pos, BlockState state, BlockPos origin, RandomSource random) {
        if (!withinFeatureChunks(pos, origin)) return;
        BlockState existing = level.getBlockState(pos);
        boolean surfaceSoil = pos.getY() == origin.getY() && existing.is(BlockTags.DIRT);
        if (!existing.isAir() && !existing.canBeReplaced() && !existing.is(ModBlocks.ROSEWILLOW_SAPLING.get())
                && !surfaceSoil) return;

        if (random.nextInt(5) == 0) {
            state = ModBlocks.ROSEWILLOW_LOG_BLOOMING.get().defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
        }
        setBlock(level, pos, state);
    }

    private boolean withinFeatureChunks(BlockPos pos, BlockPos origin) {
        return Math.abs((pos.getX() >> 4) - (origin.getX() >> 4)) <= 1
                && Math.abs((pos.getZ() >> 4) - (origin.getZ() >> 4)) <= 1;
    }
}

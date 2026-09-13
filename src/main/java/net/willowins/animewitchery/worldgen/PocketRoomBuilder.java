package net.willowins.animewitchery.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.dimension.PocketRegistry;

public final class PocketRoomBuilder {

    private PocketRoomBuilder() {}

    public static final int INTERIOR_HALF = 4;
    public static final int WALL_XZ = 5;
    public static final int CEILING_Y = 7;
    public static final int MOUNT_Y = 6;

    public static boolean isBuilt(ServerLevel pocketLevel, BlockPos origin) {
        BlockState floor = pocketLevel.getBlockState(origin);
        return floor.is(ModBlocks.RUIN_FLOOR.get());
    }

    public static void build(ServerLevel pocketLevel, PocketRegistry.PocketEntry entry) {
        BlockPos origin = entry.pocketOrigin();
        if (isBuilt(pocketLevel, origin)) {
            return;
        }

        preloadChunks(pocketLevel, origin);

        for (int x = -WALL_XZ; x <= WALL_XZ; x++) {
            for (int z = -WALL_XZ; z <= WALL_XZ; z++) {
                for (int y = 0; y <= CEILING_Y; y++) {
                    BlockPos pos = origin.offset(x, y, z);
                    BlockState state;
                    if (y == 0) {
                        state = ModBlocks.RUIN_FLOOR.get().defaultBlockState();
                    } else if (y == CEILING_Y) {
                        state = ModBlocks.RUIN_BRICK.get().defaultBlockState();
                    } else if (Math.abs(x) == WALL_XZ || Math.abs(z) == WALL_XZ) {
                        state = ModBlocks.RUIN_BRICK.get().defaultBlockState();
                    } else {
                        state = Blocks.AIR.defaultBlockState();
                    }
                    pocketLevel.setBlock(pos, state, 3);
                }
            }
        }

        placeMount(pocketLevel, origin.offset(INTERIOR_HALF, MOUNT_Y, 0), Direction.EAST);
        placeMount(pocketLevel, origin.offset(-INTERIOR_HALF, MOUNT_Y, 0), Direction.WEST);
        placeMount(pocketLevel, origin.offset(0, MOUNT_Y, INTERIOR_HALF), Direction.SOUTH);
        placeMount(pocketLevel, origin.offset(0, MOUNT_Y, -INTERIOR_HALF), Direction.NORTH);

        BlockPos returnRift = origin.offset(0, 1, 0);
        pocketLevel.setBlock(returnRift, ModBlocks.POCKET_RETURN.get().defaultBlockState(), 3);
        if (pocketLevel.getBlockEntity(returnRift) instanceof net.willowins.animewitchery.block.entity.PocketVeilBlockEntity be) {
            be.setPocketIndex(entry.index());
        }
    }

    private static void preloadChunks(ServerLevel pocketLevel, BlockPos origin) {
        int minCX = (origin.getX() - WALL_XZ) >> 4;
        int maxCX = (origin.getX() + WALL_XZ) >> 4;
        int minCZ = (origin.getZ() - WALL_XZ) >> 4;
        int maxCZ = (origin.getZ() + WALL_XZ) >> 4;
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                pocketLevel.getChunk(cx, cz, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
            }
        }
    }

    private static void placeMount(ServerLevel pocketLevel, BlockPos pos, Direction facing) {
        BlockState mountState = ModBlocks.POCKET_EXPANSION_JIGSAW.get()
                .defaultBlockState()
                .setValue(net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING, facing);
        pocketLevel.setBlock(pos, mountState, 3);
    }
}
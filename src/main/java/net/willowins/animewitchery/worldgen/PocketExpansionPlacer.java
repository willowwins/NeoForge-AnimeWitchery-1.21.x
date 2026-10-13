package net.willowins.animewitchery.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.FrontAndTop;
import net.willowins.animewitchery.dimension.PocketRegistry;
import com.mojang.datafixers.util.Either;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class PocketExpansionPlacer {

    private PocketExpansionPlacer() {}

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(PocketExpansionPlacer.class);

    public static final ResourceKey<StructureTemplatePool> POCKET_POOL =
            ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath("animewitchery", "pocket/hub"));

    public static final ResourceKey<StructureTemplatePool> HUB_POOL =
            ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.fromNamespaceAndPath("animewitchery", "darkworld/hub"));

    /** Expected dimensions of the darkworld hub structure. */
    public static final int HUB_SIZE_XZ = 21;
    public static final int HUB_SIZE_Y = 13;

    /** The hub mount jigsaws sit 6 blocks above their floor. */
    public static final int HUB_JIGSAW_HEIGHT_ABOVE_FLOOR = 6;
    public static final int JIGSAW_HEIGHT_ABOVE_FLOOR = 6;

    private static final Field TEMPLATE_FIELD;

    static {
        Field field = null;
        try {
            field = SinglePoolElement.class.getDeclaredField("template");
            field.setAccessible(true);
        } catch (NoSuchFieldException ignored) {
        }
        TEMPLATE_FIELD = field;
    }

    public static List<StructurePoolElement> getElements(ServerLevel level) {
        Holder<StructureTemplatePool> poolHolder = level.registryAccess()
                .registryOrThrow(Registries.TEMPLATE_POOL)
                .getHolderOrThrow(POCKET_POOL);
        return new ArrayList<>(poolHolder.value().getShuffledTemplates(level.getRandom()));
    }

    public static List<StructurePoolElement> getSortedElements(ServerLevel level) {
        return getElements(level).stream()
                .sorted(Comparator.comparing(PocketExpansionPlacer::elementName))
                .toList();
    }

    public static String elementName(StructurePoolElement element) {
        if (TEMPLATE_FIELD != null && element instanceof SinglePoolElement) {
            try {
                @SuppressWarnings("unchecked")
                Either<ResourceLocation, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate> either =
                        (Either<ResourceLocation, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate>) TEMPLATE_FIELD.get(element);
                Optional<ResourceLocation> location = either.left();
                if (location.isPresent()) {
                    return location.get().toString();
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return element.getType().toString();
    }

    public static boolean tryPlaceSelected(ServerLevel level, BlockPos pos, StructurePoolElement element) {
        StructureTemplateManager manager = level.getStructureManager();
        BlockState state = level.getBlockState(pos);
        Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        RandomSource random = level.getRandom();
        for (Rotation rotation : Rotation.getShuffled(random)) {
            if (tryPlaceElement(level, manager, element, pos, facing, rotation, random)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places the darkworld hub structure centered on a pocket origin and links
     * its return pad, veils and pedestals to the pocket index (their BE data is
     * not stored in the template). No-op if the hub is already present.
     */
    public static boolean placeHub(ServerLevel pocketLevel, PocketRegistry.PocketEntry entry) {
        BlockPos origin = entry.pocketOrigin();
        if (entry.hubPlaced(pocketLevel)) {
            return true;
        }
        preloadChunks(pocketLevel, origin);

        var poolRegistry = pocketLevel.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        var poolHolder = poolRegistry.getHolder(HUB_POOL);
        if (poolHolder.isEmpty()) {
            LOGGER.error("Darkworld hub pool {} is missing from the pocket dimension registry", HUB_POOL.location());
            return false;
        }

        List<StructurePoolElement> elements = poolHolder.get().value().getShuffledTemplates(pocketLevel.getRandom());
        if (elements.isEmpty()) {
            LOGGER.error("Darkworld hub pool {} has no templates", HUB_POOL.location());
            return false;
        }

        StructurePoolElement hub = elements.get(0);
        BlockPos corner = origin.offset(-10, 0, -10);
        StructureTemplateManager manager = pocketLevel.getStructureManager();
        StructureManager structureManager = pocketLevel.structureManager();
        ChunkGenerator generator = pocketLevel.getChunkSource().getGenerator();
        RandomSource random = pocketLevel.getRandom();
        boolean placed = hub.place(
                manager,
                pocketLevel,
                structureManager,
                generator,
                corner,
                corner,
                Rotation.NONE,
                BoundingBox.infinite(),
                random,
                LiquidSettings.APPLY_WATERLOGGING,
                false);

        if (placed) {
            relinkPocketBlocks(pocketLevel, origin, entry.index());
            LOGGER.info("Placed darkworld hub template {} at {} for pocket {}", elementName(hub), corner, entry.index());
        } else {
            LOGGER.error("Darkworld hub template {} failed to place at {} for pocket {}", elementName(hub), corner, entry.index());
        }
        return placed;
    }

    private static void preloadChunks(ServerLevel level, BlockPos origin) {
        int minCX = (origin.getX() - 10) >> 4;
        int maxCX = (origin.getX() + 10) >> 4;
        int minCZ = (origin.getZ() - 10) >> 4;
        int maxCZ = (origin.getZ() + 10) >> 4;
        for (int cx = minCX; cx <= maxCX; cx++) {
            for (int cz = minCZ; cz <= maxCZ; cz++) {
                level.getChunk(cx, cz, net.minecraft.world.level.chunk.status.ChunkStatus.FULL, false);
            }
        }
    }

    private static void relinkPocketBlocks(ServerLevel pocketLevel, BlockPos origin, int pocketIndex) {
        BlockPos min = origin.offset(-10, 0, -10);
        BlockPos max = origin.offset(10, HUB_SIZE_Y - 1, 10);
        for (BlockPos cell : BlockPos.betweenClosed(min, max)) {
            if (pocketLevel.getBlockEntity(cell) instanceof net.willowins.animewitchery.block.entity.PocketVeilBlockEntity veil) {
                veil.setPocketIndex(pocketIndex);
            } else if (pocketLevel.getBlockEntity(cell) instanceof net.willowins.animewitchery.block.entity.DarkPedestalBlockEntity pedestal) {
                pedestal.setPocketIndex(pocketIndex);
            }
        }
    }

    private static boolean tryPlaceElement(
            ServerLevel level,
            StructureTemplateManager manager,
            StructurePoolElement element,
            BlockPos pos,
            Direction facing,
            Rotation rotation,
            RandomSource random) {

        StructureBlockInfo parentInfo = makeParentInfo(pos, facing);

        List<StructureBlockInfo> jigsaws =
                element.getShuffledJigsawBlocks(manager, BlockPos.ZERO, rotation, random);

        for (StructureBlockInfo child : jigsaws) {
            if (child.pos().getY() != JIGSAW_HEIGHT_ABOVE_FLOOR) {
                continue;
            }
            if (!JigsawBlock.canAttach(parentInfo, child)) {
                continue;
            }
            BlockPos blockpos1 = pos.relative(facing);
            BlockPos connectedPos = blockpos1.subtract(child.pos());

            StructureManager structureManager = level.structureManager();
            ChunkGenerator generator = level.getChunkSource().getGenerator();
            boolean placed = element.place(
                    manager,
                    level,
                    structureManager,
                    generator,
                    connectedPos,
                    BlockPos.ZERO,
                    rotation,
                    BoundingBox.infinite(),
                    random,
                    LiquidSettings.APPLY_WATERLOGGING,
                    false);
            if (placed) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                return true;
            }
        }
        return false;
    }

    private static StructureBlockInfo makeParentInfo(BlockPos pos, Direction facing) {
        BlockState jigsawState = Blocks.JIGSAW.defaultBlockState()
                .setValue(BlockStateProperties.ORIENTATION, FrontAndTop.fromFrontAndTop(facing, Direction.UP));
        CompoundTag nbt = new CompoundTag();
        nbt.putString("target", POCKET_POOL.location().toString());
        nbt.putString("joint", JigsawBlockEntity.JointType.ROLLABLE.getSerializedName());
        return new StructureBlockInfo(pos, jigsawState, nbt);
    }
}

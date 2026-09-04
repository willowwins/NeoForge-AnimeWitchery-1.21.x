package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.willowins.animewitchery.Config;
import net.willowins.animewitchery.fluid.ModFluids;
import net.willowins.animewitchery.particle.ModParticles;

/**
 * Ticks the {@code luna_crystal} block. While it is night and the crystal can see the sky
 * (even through translucent blocks like glass), it slowly generates starlight into an internal
 * buffer, then pushes that starlight into any Starlight Pools within range, dripping celestial
 * particles into each pool it fills. A horizontal 3x3 square of glass up to
 * {@link Config#LUNA_RANGE} blocks above the crystal doubles the generation rate.
 */
public class LunaCrystalBlockEntity extends BlockEntity {

    private static final float TICKS_PER_SECOND = 20.0F;

    // Internal starlight buffer. It is filled by the crystal and drained into adjacent pools.
    public final FluidTank buffer = new FluidTank(Config.LUNA_BUFFER.get() < 1 ? 1000 : Config.LUNA_BUFFER.get()) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    // Starlight generated but not yet committed to the buffer (kept smooth across ticks).
    private float pendingFill = 0.0F;

    public LunaCrystalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.LUNA_CRYSTAL_BE.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LunaCrystalBlockEntity be) {
        be.tickServer(level, pos);
    }

    private void tickServer(Level level, BlockPos pos) {
        // Only in a sky-accessible dimension, at night, with the sky visible from above.
        if (!level.dimensionType().hasSkyLight() || !level.isNight()) {
            return;
        }
        if (!canSeeSky(level, pos)) {
            return;
        }

        double base = Config.LUNA_BASE_RATE.get();
        double bonus = Config.LUNA_BONUS_RATE.get();
        float rate = (float) (hasGlassRoof(level, pos) ? bonus : base);
        pendingFill += rate / TICKS_PER_SECOND;

        // Commit whole millibuckets of generated starlight into the internal buffer.
        while (pendingFill >= 1.0F) {
            pendingFill -= 1.0F;
            int filled = buffer.fill(new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), 1), IFluidHandler.FluidAction.EXECUTE);
            if (filled <= 0) {
                // Buffer is full: bounce the pending fluid so it isn't lost.
                pendingFill += 1.0F;
                break;
            }
        }

        // Push the buffer into Starlight Pools below us.
        outputToPools(level, pos);
    }

    /**
     * Drains the internal buffer into nearby {@link StarlighPoolBlockEntity} tanks (pools only),
     * spawning drip particles at each pool that accepts starlight.
     */
    private void outputToPools(Level level, BlockPos pos) {
        if (buffer.getFluidAmount() <= 0) {
            return;
        }
        int range = Config.LUNA_RANGE.get();
        for (int dy = -range; dy <= 0; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos candidate = pos.offset(dx, dy, dz);
                    if (!(level.getBlockEntity(candidate) instanceof StarlighPoolBlockEntity pool)) {
                        continue;
                    }
                    // Only target the pool itself, and only while it can accept starlight.
                    FluidStack toFill = new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), buffer.getFluidAmount());
                    if (toFill.isEmpty()) {
                        continue;
                    }
                    int filled = pool.fluidTank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                    if (filled <= 0) {
                        continue;
                    }
                    buffer.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    spawnDripParticles((ServerLevel) level, pool.getBlockPos());
                    pool.setChanged();
                    if (buffer.getFluidAmount() <= 0) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Spawns a handful of falling stardrop particles above the pool to show it being filled.
     */
    private static void spawnDripParticles(ServerLevel level, BlockPos poolPos) {
        for (int i = 0; i < 3; i++) {
            double x = poolPos.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
            double z = poolPos.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.5;
            level.sendParticles(
                    ModParticles.STARDROP_PARTICLE.get(),
                    x, poolPos.getY() + 1.1, z,
                    1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    /**
     * True if the sky is visible directly above the given crystal position, considering
     * translucent blocks (such as glass) as transparent to sunlight.
     */
    private boolean canSeeSky(Level level, BlockPos pos) {
        for (int y = pos.getY() + 1; y < level.getMaxBuildHeight(); y++) {
            BlockPos above = new BlockPos(pos.getX(), y, pos.getZ());
            BlockState state = level.getBlockState(above);
            if (state.isAir()) {
                continue;
            }
            // A block that fully blocks light (opacity 15) hides the sky; translucent blocks don't.
            if (state.getLightBlock(level, above) >= 15) {
                return false;
            }
        }
        return true;
    }

    /**
     * True if there is a horizontal 3x3 square of glass somewhere above the crystal (up to
     * {@link Config#LUNA_RANGE} blocks up, matching the crystal's effect range), centred on its x/z.
     */
    private boolean hasGlassRoof(Level level, BlockPos pos) {
        int range = Config.LUNA_RANGE.get();
        for (int roofYOffset = 1; roofYOffset <= range; roofYOffset++) {
            int roofY = pos.getY() + roofYOffset;
            boolean fullSquare = true;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (!level.getBlockState(new BlockPos(pos.getX() + dx, roofY, pos.getZ() + dz)).is(net.minecraft.world.level.block.Blocks.GLASS)) {
                        fullSquare = false;
                        break;
                    }
                }
                if (!fullSquare) {
                    break;
                }
            }
            if (fullSquare) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Buffer", buffer.writeToNBT(registries, new CompoundTag()));
        tag.putFloat("PendingFill", pendingFill);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Buffer")) {
            buffer.readFromNBT(registries, tag.getCompound("Buffer"));
        }
        pendingFill = tag.getFloat("PendingFill");
    }
}

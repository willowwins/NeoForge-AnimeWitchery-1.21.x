package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.willowins.animewitchery.particle.ModParticles;
import net.willowins.animewitchery.recipe.ModRecipes;
import net.willowins.animewitchery.recipe.StarlightInfusionRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class StarlightInfusionAltarBlockEntity extends BlockEntity {

    public static final int SEARCH_RANGE = 12;

    public final FluidTank fluidTank = new FluidTank(20000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private ItemStack baseItem = ItemStack.EMPTY;
    private ItemStack pendingResult = ItemStack.EMPTY;
    private ItemStack craftResult = ItemStack.EMPTY;

    private int progress = 0;
    private int duration = 0;
    private int fluidCost = 0;
    private int ingredientCount = 0;
    private int pedestalsConsumed = 0;

    private boolean wasPowered = false;

    public StarlightInfusionAltarBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.STARLIGHT_INFUSION_ALTAR_BE.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, StarlightInfusionAltarBlockEntity be) {
        be.tickServer(level, pos);
    }

    private void tickServer(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }
        boolean powered = level.hasNeighborSignal(pos);

        if (progress > 0) {
            if (fluidCost > 0 && duration > 0) {
                int drainPerTick = Math.max(1, fluidCost / duration);
                FluidStack drained = fluidTank.drain(drainPerTick, IFluidHandler.FluidAction.EXECUTE);
            }

            if (ingredientCount > 0 && pedestalsConsumed < ingredientCount) {
                int shouldHaveConsumed = progress * ingredientCount / duration;
                if (shouldHaveConsumed > pedestalsConsumed) {
                    consumeOnePedestal(level, pos);
                    pedestalsConsumed++;
                }
            }

            progress++;
            if (progress >= duration) {
                while (pedestalsConsumed < ingredientCount) {
                    consumeOnePedestal(level, pos);
                    pedestalsConsumed++;
                }
                this.baseItem = craftResult.copy();
                this.craftResult = ItemStack.EMPTY;
                this.pendingResult = ItemStack.EMPTY;
                this.progress = 0;
                this.duration = 0;
                this.fluidCost = 0;
                this.ingredientCount = 0;
                this.pedestalsConsumed = 0;
            }
            spawnCraftParticles(level, pos);
            setChanged();
            setChangedAndSync();
            wasPowered = powered;
            return;
        }

        spawnCraftParticles(level, pos);

        if (powered && !wasPowered) {
            StarlightInfusionRecipe recipe = findRecipeForCurrentSetup(level);
            if (recipe != null) {
                if (!recipe.matchesFluid(fluidTank.getFluid())) {
                    wasPowered = powered;
                    return;
                }
                int initialDrain = Math.max(1, recipe.fluidCost() / recipe.duration());
                if (fluidTank.getFluidAmount() >= initialDrain) {
                    fluidTank.drain(initialDrain, IFluidHandler.FluidAction.EXECUTE);
                    this.craftResult = recipe.result().copy();
                    this.baseItem = ItemStack.EMPTY;
                    this.pendingResult = ItemStack.EMPTY;
                    this.progress = 1;
                    this.duration = recipe.duration() * 20;
                    this.fluidCost = recipe.fluidCost();
                    this.ingredientCount = recipe.ingredients().size();
                    this.pedestalsConsumed = 0;
                    setChanged();
                    setChangedAndSync();
                }
            }
        }

        wasPowered = powered;
    }

    @Nullable
    private StarlightInfusionRecipe findRecipeForCurrentSetup(Level level) {
        List<ItemStack> pedestalItems = new ArrayList<>();
        for (StarlightInfusionPedestalBlockEntity pedestal : getBoundPedestals(level)) {
            if (!pedestal.getHeldItem().isEmpty()) {
                pedestalItems.add(pedestal.getHeldItem());
            }
        }
        for (net.minecraft.world.item.crafting.RecipeHolder<StarlightInfusionRecipe> holder
                : level.getRecipeManager().getAllRecipesFor(ModRecipes.STARLIGHT_INFUSION_TYPE.get())) {
            StarlightInfusionRecipe recipe = holder.value();
            if (recipe.matchesBase(this.baseItem) && recipe.matchesIngredients(pedestalItems)) {
                return recipe;
            }
        }
        return null;
    }

    private void consumeOnePedestal(Level level, BlockPos pos) {
        for (StarlightInfusionPedestalBlockEntity pedestal : getBoundPedestals(level)) {
            if (!pedestal.getHeldItem().isEmpty()) {
                BlockPos pedestalPos = pedestal.getBlockPos();
                pedestal.setHeldItem(ItemStack.EMPTY);

                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ModParticles.STARDROP_PARTICLE.get(),
                            pedestalPos.getX() + 0.5,
                            pedestalPos.getY() + 1.1,
                            pedestalPos.getZ() + 0.5,
                            6,
                            0.1, 0.1, 0.1,
                            0.02);
                    serverLevel.playSound(
                            null,
                            pedestalPos,
                            net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                            net.minecraft.sounds.SoundSource.BLOCKS,
                            0.8F,
                            1.2F);
                }
                return;
            }
        }
    }

    private void spawnCraftParticles(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        double x = pos.getX() + 0.5;
        double z = pos.getZ() + 0.5;

        if (!baseItem.isEmpty() || !pendingResult.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                serverLevel.sendParticles(
                        ModParticles.STARDROP_PARTICLE.get(),
                        x + (level.random.nextDouble() - 0.5) * 0.8,
                        pos.getY() + 1.1 + level.random.nextDouble() * 0.3,
                        z + (level.random.nextDouble() - 0.5) * 0.8,
                        1,
                        0.0, 0.05, 0.0,
                        0.01);
            }
        }

        if (progress > 0) {
            for (int i = 0; i < 5; i++) {
                double px = x + (level.random.nextDouble() - 0.5) * 0.6;
                double pz = z + (level.random.nextDouble() - 0.5) * 0.6;
                serverLevel.sendParticles(
                        ModParticles.STARDROP_PARTICLE.get(),
                        px, pos.getY() + 1.1, pz,
                        1,
                        0.0, 0.12 + level.random.nextDouble() * 0.08, 0.0,
                        0.02);
            }
        }
    }

    private List<StarlightInfusionPedestalBlockEntity> getBoundPedestals(Level level) {
        List<StarlightInfusionPedestalBlockEntity> pedestals = new ArrayList<>();
        BlockPos pos = getBlockPos();
        for (BlockPos candidate : BlockPos.betweenClosed(
                pos.offset(-SEARCH_RANGE, -SEARCH_RANGE, -SEARCH_RANGE),
                pos.offset(SEARCH_RANGE, SEARCH_RANGE, SEARCH_RANGE))) {
            BlockEntity be = level.getBlockEntity(candidate);
            if (be instanceof StarlightInfusionPedestalBlockEntity pedestal && pedestal.isBoundTo(pos)) {
                pedestals.add(pedestal);
            }
        }
        return pedestals;
    }

    public ItemStack getBaseItem() {
        return baseItem;
    }

    public void setBaseItem(ItemStack stack) {
        this.baseItem = stack;
        setChangedAndSync();
    }

    private void setChangedAndSync() {
        setChanged();
        if (hasLevel() && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public int getProgress() {
        return progress;
    }

    public int getDuration() {
        return duration;
    }

    public ItemStack getPendingResult() {
        return pendingResult;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!baseItem.isEmpty()) {
            tag.put("BaseItem", baseItem.save(registries));
        }
        if (!pendingResult.isEmpty()) {
            tag.put("PendingResult", pendingResult.save(registries));
        }
        if (!craftResult.isEmpty()) {
            tag.put("CraftResult", craftResult.save(registries));
        }
        tag.put("Fluid", fluidTank.writeToNBT(registries, new CompoundTag()));
        tag.putInt("Progress", progress);
        tag.putInt("Duration", duration);
        tag.putInt("FluidCost", fluidCost);
        tag.putInt("IngredientCount", ingredientCount);
        tag.putInt("PedestalsConsumed", pedestalsConsumed);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        baseItem = ItemStack.parse(registries, tag.getCompound("BaseItem")).orElse(ItemStack.EMPTY);
        pendingResult = ItemStack.parse(registries, tag.getCompound("PendingResult")).orElse(ItemStack.EMPTY);
        craftResult = ItemStack.parse(registries, tag.getCompound("CraftResult")).orElse(ItemStack.EMPTY);
        fluidTank.readFromNBT(registries, tag.getCompound("Fluid"));
        progress = tag.getInt("Progress");
        duration = tag.getInt("Duration");
        fluidCost = tag.getInt("FluidCost");
        ingredientCount = tag.getInt("IngredientCount");
        pedestalsConsumed = tag.getInt("PedestalsConsumed");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(net.minecraft.network.Connection connection, ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
        loadAdditional(packet.getTag(), registries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }
}

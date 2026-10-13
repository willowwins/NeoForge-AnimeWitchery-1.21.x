package net.willowins.animewitchery.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ContainerHelper;
import net.willowins.animewitchery.ModDataComponentTypes;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.recipe.AlchemyRecipe;
import net.willowins.animewitchery.recipe.ModRecipes;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

/** Inventory and alchemy processing for the Fabric alchemy-table port. */
public class AlchemyTableBlockEntity extends BlockEntity implements Container, GeoBlockEntity {
    public static final int SLOT_COUNT = 11;
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private int progress;
    private int maxProgress = 200;
    private boolean activated;

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE_BE.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Idle", 0,
                animationState -> animationState.setAndContinue(IDLE_ANIMATION)));
    }

    @Override public AnimatableInstanceCache getAnimatableInstanceCache() { return animationCache; }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyTableBlockEntity table) {
        if (!table.activated) return;
        AlchemyRecipe recipe = table.findRecipe();
        if (recipe == null || !table.canFitResult(recipe.result())) {
            table.cancelProcessing();
            return;
        }
        table.maxProgress = Math.max(1, recipe.processingTime());
        table.progress++;
        if (table.progress >= table.maxProgress) {
            table.craft(recipe);
            table.activated = false;
            table.progress = 0;
        }
        table.setChangedAndSync();
    }

    public boolean activateWithCatalyst(Player player, ItemStack catalyst) {
        if (level == null || level.isClientSide || activated || findRecipe() == null) return false;
        int mana = catalyst.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
        int maxMana = catalyst.is(ModItems.RESONANT_CATALYST.get()) ? 5_000_000 : 10_000;
        if (mana < maxMana) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    "The catalyst must be fully charged to activate alchemy."), true);
            return false;
        }
        int xpCost = getCurrentRecipeXpCost();
        if (player.experienceLevel < xpCost) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal(
                    "You lack the experience required for this recipe."), true);
            return false;
        }
        player.giveExperienceLevels(-xpCost);
        activated = true;
        progress = 0;
        setChangedAndSync();
        return true;
    }

    public int getProgress() { return progress; }
    public int getMaxProgress() { return maxProgress; }
    public boolean isActivated() { return activated; }
    public int getCurrentRecipeXpCost() {
        AlchemyRecipe recipe = findRecipe();
        return recipe == null ? 0 : recipe.xpCost();
    }

    private AlchemyRecipe findRecipe() {
        if (level == null) return null;
        List<ItemStack> inputs = new ArrayList<>();
        for (int i = 1; i < SLOT_COUNT; i++) inputs.add(items.get(i));
        for (var holder : level.getRecipeManager().getAllRecipesFor(ModRecipes.ALCHEMY_TYPE.get())) {
            if (holder.value().matchesIngredients(inputs)) return holder.value();
        }
        return null;
    }

    private boolean canFitResult(ItemStack result) {
        ItemStack output = items.get(0);
        return output.isEmpty() || (ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= output.getMaxStackSize());
    }

    private void craft(AlchemyRecipe recipe) {
        for (var ingredient : recipe.ingredients()) {
            for (int i = 1; i < SLOT_COUNT; i++) {
                ItemStack stack = items.get(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    break;
                }
            }
        }
        ItemStack output = items.get(0);
        if (output.isEmpty()) items.set(0, recipe.result().copy());
        else output.grow(recipe.result().getCount());
    }

    private void cancelProcessing() {
        activated = false;
        progress = 0;
        setChangedAndSync();
    }

    private void setChangedAndSync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override public int getContainerSize() { return SLOT_COUNT; }
    @Override public boolean isEmpty() { return items.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getItem(int slot) { return slot >= 0 && slot < SLOT_COUNT ? items.get(slot) : ItemStack.EMPTY; }
    @Override public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChangedAndSync();
        return result;
    }
    @Override public ItemStack removeItemNoUpdate(int slot) { return ContainerHelper.takeItem(items, slot); }
    @Override public void setItem(int slot, ItemStack stack) {
        if (slot < 0 || slot >= SLOT_COUNT) return;
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) stack.setCount(getMaxStackSize());
        setChangedAndSync();
    }
    @Override public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5,
                worldPosition.getZ() + 0.5) <= 20.25;
    }
    @Override public void clearContent() { items.clear(); setChangedAndSync(); }

    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putBoolean("Activated", activated);
    }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ContainerHelper.loadAllItems(tag, items, registries);
        progress = tag.getInt("Progress");
        maxProgress = Math.max(1, tag.getInt("MaxProgress"));
        activated = tag.getBoolean("Activated");
    }
    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}


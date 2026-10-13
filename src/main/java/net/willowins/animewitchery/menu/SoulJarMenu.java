package net.willowins.animewitchery.menu;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.willowins.animewitchery.item.ModItems;

public class SoulJarMenu extends AbstractContainerMenu {
    private static final int JAR_SIZE = 27;
    private final SimpleContainer jarInventory = new SimpleContainer(JAR_SIZE);
    private final ItemStack jarStack;
    private final InteractionHand hand;

    public SoulJarMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(id, playerInventory, data.readEnum(InteractionHand.class));
    }

    public SoulJarMenu(int id, Inventory playerInventory, InteractionHand hand) {
        this(id, playerInventory, hand, playerInventory.player.getItemInHand(hand));
    }

    private SoulJarMenu(int id, Inventory playerInventory, InteractionHand hand, ItemStack jarStack) {
        super(ModMenus.SOUL_JAR.get(), id);
        this.hand = hand;
        this.jarStack = jarStack;
        loadInventory(playerInventory.player.registryAccess());
        jarInventory.addListener(container -> {
            if (!playerInventory.player.level().isClientSide) {
                saveInventory(playerInventory.player.registryAccess());
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(jarInventory, column + row * 9, 8 + column * 18, 18 + row * 18));
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 86 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 144));
        }
    }

    private void loadInventory(HolderLookup.Provider registries) {
        CompoundTag jarData = jarStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag items = jarData.getList("Items", CompoundTag.TAG_COMPOUND);
        for (int index = 0; index < items.size(); index++) {
            CompoundTag itemTag = items.getCompound(index);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < jarInventory.getContainerSize()) {
                jarInventory.setItem(slot, ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY));
            }
        }
    }

    private void saveInventory(HolderLookup.Provider registries) {
        ListTag items = new ListTag();
        for (int slot = 0; slot < jarInventory.getContainerSize(); slot++) {
            ItemStack item = jarInventory.getItem(slot);
            if (!item.isEmpty()) {
                CompoundTag itemTag = (CompoundTag) item.save(registries);
                itemTag.putInt("Slot", slot);
                items.add(itemTag);
            }
        }
        CompoundTag jarData = jarStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        jarData.put("Items", items);
        jarStack.set(DataComponents.CUSTOM_DATA, CustomData.of(jarData));
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(hand) == jarStack && jarStack.is(ModItems.SOUL_JAR.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) return empty;
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        if (index < JAR_SIZE) {
            if (!moveItemStackTo(source, JAR_SIZE, slots.size(), true)) return empty;
        } else if (!moveItemStackTo(source, 0, JAR_SIZE, false)) {
            return empty;
        }
        if (source.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        if (source.getCount() == copy.getCount()) return empty;
        slot.onTake(player, source);
        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!player.level().isClientSide) {
            saveInventory(player.registryAccess());
        }
    }
}

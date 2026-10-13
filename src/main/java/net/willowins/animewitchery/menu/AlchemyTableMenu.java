package net.willowins.animewitchery.menu;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.willowins.animewitchery.block.entity.AlchemyTableBlockEntity;

public class AlchemyTableMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerLevelAccess access;
    private final AlchemyTableBlockEntity table;

    public AlchemyTableMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(id, playerInventory, getTable(playerInventory, data));
    }

    public AlchemyTableMenu(int id, Inventory playerInventory, AlchemyTableBlockEntity table) {
        super(ModMenus.ALCHEMY_TABLE.get(), id);
        this.table = table;
        this.inventory = table;
        this.access = ContainerLevelAccess.create(table.getLevel(), table.getBlockPos());
        checkContainerSize(inventory, AlchemyTableBlockEntity.SLOT_COUNT);
        inventory.startOpen(playerInventory.player);

        addSlot(new Slot(inventory, 0, 79, 71) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        int[][] positions = {{56,10},{29,36},{21,72},{29,107},{56,133},{103,133},{130,108},{138,72},{130,36},{103,10}};
        for (int i = 0; i < positions.length; i++) {
            addSlot(new Slot(inventory, i + 1, positions[i][0], positions[i][1]));
        }
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++)
            addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 170 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(playerInventory, col, 8 + col * 18, 228));
    }

    private static AlchemyTableBlockEntity getTable(Inventory inventory, RegistryFriendlyByteBuf data) {
        BlockEntity entity = inventory.player.level().getBlockEntity(data.readBlockPos());
        if (entity instanceof AlchemyTableBlockEntity table) return table;
        throw new IllegalStateException("Alchemy table menu opened without its block entity");
    }

    @Override public boolean stillValid(Player player) {
        return inventory.stillValid(player) && stillValid(access, player, table.getBlockState().getBlock());
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        ItemStack empty = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot == null || !slot.hasItem()) return empty;
        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();
        if (index == 0) {
            if (!moveItemStackTo(source, 11, slots.size(), true)) return empty;
            slot.onQuickCraft(source, copy);
        } else if (index < 11) {
            if (!moveItemStackTo(source, 11, slots.size(), true)) return empty;
        } else if (!moveItemStackTo(source, 1, 11, false)) return empty;
        if (source.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        if (source.getCount() == copy.getCount()) return empty;
        slot.onTake(player, source);
        return copy;
    }

    @Override public void removed(Player player) {
        super.removed(player);
        inventory.stopOpen(player);
    }

    public int getProgress() { return table.getProgress(); }
    public int getMaxProgress() { return table.getMaxProgress(); }
    public boolean isActivated() { return table.isActivated(); }
    public int getCurrentRecipeXpCost() { return table.getCurrentRecipeXpCost(); }
}

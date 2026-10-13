package net.willowins.animewitchery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;
import java.util.List;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.menu.SoulJarMenu;

public class SoulJarItem extends Item {
    public SoulJarItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        int count = data.getList("Items", CompoundTag.TAG_COMPOUND).size();
        if (count > 0) tooltip.add(Component.literal("Contains " + count + " items").withStyle(ChatFormatting.GRAY));
    }

    public static boolean captureEntity(Player player, Entity entity, ItemStack jar) {
        SimpleContainer inventory = new SimpleContainer(27);
        loadInventory(player.registryAccess(), jar, inventory);
        int freeSlot = -1;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (inventory.getItem(i).isEmpty()) { freeSlot = i; break; }
        }
        if (freeSlot < 0) return false;

        CompoundTag entityData = new CompoundTag();
        entity.saveWithoutId(entityData);
        entityData.putString("id", net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
        CompoundTag soulData = new CompoundTag();
        soulData.put("EntityData", entityData);
        soulData.putString("EntityName", entity.getName().getString());
        ItemStack soul = new ItemStack(ModItems.SOUL.get());
        soul.set(DataComponents.CUSTOM_DATA, CustomData.of(soulData));
        inventory.setItem(freeSlot, soul);
        saveInventory(player.registryAccess(), jar, inventory);
        return true;
    }

    public static void loadInventory(HolderLookup.Provider registries, ItemStack jar, SimpleContainer inventory) {
        CompoundTag data = jar.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        ListTag items = data.getList("Items", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag itemTag = items.getCompound(i);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < inventory.getContainerSize()) {
                inventory.setItem(slot, ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY));
            }
        }
    }

    public static void saveInventory(HolderLookup.Provider registries, ItemStack jar, SimpleContainer inventory) {
        ListTag items = new ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = (CompoundTag) stack.save(registries);
                itemTag.putInt("Slot", i);
                items.add(itemTag);
            }
        }
        CompoundTag data = jar.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        data.put("Items", items);
        jar.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getMainHandItem().getItem() instanceof SummonerStaffItem) {
            return InteractionResultHolder.pass(stack);
        }
        if (!level.isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inventory, user) -> new SoulJarMenu(id, inventory, hand),
                    Component.translatable("container.animewitchery.soul_jar")),
                    buffer -> buffer.writeEnum(hand));
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }
}

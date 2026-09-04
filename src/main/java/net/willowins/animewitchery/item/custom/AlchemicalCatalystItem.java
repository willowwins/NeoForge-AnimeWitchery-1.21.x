package net.willowins.animewitchery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.ModDataComponentTypes;

public class AlchemicalCatalystItem extends Item {
    private static final int MAX_STORAGE = 10000; // Total capacity of the item

    public AlchemicalCatalystItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            ManaData playerMana = player.getData(ModAttachments.MANA);
            int itemMana = stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);

            if (player.isCrouching()) {
                // SQUEEZE MANA OUT: Item -> Player
                if (itemMana > 0 && playerMana.current() < playerMana.max()) {
                    int transferAmount = Math.min(itemMana, playerMana.max() - playerMana.current());

                    ManaData newMana = playerMana.addMana(transferAmount);
                    player.setData(ModAttachments.MANA, newMana);
                    stack.set(ModDataComponentTypes.STORED_MANA.get(), itemMana - transferAmount);

                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new net.willowins.animewitchery.network.ManaSyncPayload(newMana.current(), newMana.max()));
                    }

                    player.displayClientMessage(Component.literal("Withdrew " + transferAmount + " mana!"), true);
                }
            } else {
                // INJECT MANA IN: Player -> Item
                if (playerMana.current() > 0 && itemMana < MAX_STORAGE) {
                    int transferAmount = Math.min(playerMana.current(), MAX_STORAGE - itemMana);

                    ManaData newMana = playerMana.addMana(-transferAmount);
                    player.setData(ModAttachments.MANA, newMana);
                    stack.set(ModDataComponentTypes.STORED_MANA.get(), itemMana + transferAmount);

                    if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new net.willowins.animewitchery.network.ManaSyncPayload(newMana.current(), newMana.max()));
                    }

                    player.displayClientMessage(Component.literal("Stored " + transferAmount + " mana!"), true);
                }
            }

            int updatedItemMana = stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
            player.displayClientMessage(Component.literal("Catalyst Storage: " + updatedItemMana + " / " + MAX_STORAGE), true);
        }

        return InteractionResultHolder.success(stack);
    }

    // --- CUSTOM DURABILITY BAR OVERRIDES ---

    @Override
    public boolean isBarVisible(ItemStack stack) {
        // Show the bar if there is any mana inside it
        return stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int itemMana = stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);

        // Map the current mana to a pixel value between 0 and 13
        // Math.min protects against any potential overflow issues
        float ratio = (float) Math.min(itemMana, MAX_STORAGE) / MAX_STORAGE;
        return Math.round(ratio * 13.0F);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        // Returns a custom color for the bar instead of vanilla green/yellow/red.
        // This is a Hex color code. 0x3399FF is a nice vibrant light-magic blue.
        return 0x3399FF;
    }
}

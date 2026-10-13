package net.willowins.animewitchery.item.custom;

// Make sure to import your ModAttachments and ManaData classes!
// import net.willowins.animewitchery.attachment.ModAttachments;
// import net.willowins.animewitchery.attachment.ManaData;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ManaReaderItem extends Item {

    public ManaReaderItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Always check !level.isClientSide for data safety, though action bar messages can be sent from the server
        if (!level.isClientSide()) {
            // 1. Fetch mana via the PlayerMana helper (includes pendant bonuses and locked-player caps)
            int current = net.willowins.animewitchery.mana.PlayerMana.getMana(player);
            int max = net.willowins.animewitchery.mana.PlayerMana.getMaxMana(player);

            // 2. Format the message string
            String message = "Mana: " + current + " / " + max;

            // 3. Send the message. Setting the second parameter to 'true' forces it above the hotbar (action bar)
            player.displayClientMessage(Component.literal(message), true);
        }

        // Return success so the item animation triggers correctly
        return InteractionResultHolder.success(itemStack);
    }
}

package net.willowins.animewitchery.item.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.network.ManaSyncPayload;

public class StarlightBottleItem extends Item {

    private static final int MANA_RESTORED = 10;

    public StarlightBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        // Let the food handler convert to a glass bottle / apply effects first.
        ItemStack result = super.finishUsingItem(stack, level, livingEntity);

        // Restore mana on the server only, so the client doesn't desync.
        if (!level.isClientSide && livingEntity instanceof ServerPlayer serverPlayer) {
            ManaData data = serverPlayer.getData(ModAttachments.MANA);
            ManaData newMana = data.addMana(MANA_RESTORED);
            serverPlayer.setData(ModAttachments.MANA, newMana);
            PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(newMana.current(), newMana.max()));
        }

        return result;
    }
}

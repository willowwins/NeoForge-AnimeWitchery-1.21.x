package net.willowins.animewitchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EmergencyRecallItem extends Item {
    private static final float HEALTH_THRESHOLD = 4.0F;

    public EmergencyRecallItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide || level.getGameTime() % 5 != 0 || !(entity instanceof ServerPlayer player)
                || player.getHealth() > HEALTH_THRESHOLD || level.getServer() == null) {
            return;
        }

        BlockPos spawnPos = player.getRespawnPosition();
        ServerLevel spawnLevel = level.getServer().getLevel(player.getRespawnDimension());
        if (spawnPos == null || spawnLevel == null) {
            return;
        }

        player.teleportTo(spawnLevel, spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5,
                player.getYRot(), player.getXRot());
        spawnLevel.playSound(null, spawnPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        player.sendSystemMessage(Component.literal("Emergency Recall activated!"));
        stack.shrink(1);
    }
}

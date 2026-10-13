package net.willowins.animewitchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

public class RespawnTeleportItem extends Item {
    private static final int CHARGE_TICKS = 30;

    public RespawnTeleportItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int timeLeft) {
        if (level.isClientSide || !(user instanceof ServerPlayer player)
                || getUseDuration(stack, user) - timeLeft < CHARGE_TICKS || player.experienceLevel < 1
                || level.getServer() == null) {
            return;
        }

        BlockPos respawnPos = player.getRespawnPosition();
        ServerLevel destination = level.getServer().getLevel(player.getRespawnDimension());
        Vec3 destinationPos;
        if (respawnPos != null && destination != null) {
            destinationPos = Vec3.atCenterOf(respawnPos);
        } else {
            destination = level.getServer().overworld();
            destinationPos = Vec3.atCenterOf(destination.getSharedSpawnPos());
        }

        List<Entity> companions = List.of();
        if (player.isShiftKeyDown()) {
            Vec3 start = player.position();
            companions = level.getEntities(player, new AABB(start.subtract(2, 2, 2), start.add(2, 2, 2)), entity -> true);
        }

        player.giveExperienceLevels(-1);
        player.teleportTo(destination, destinationPos.x, destinationPos.y, destinationPos.z, player.getYRot(), player.getXRot());
        for (Entity companion : companions) {
            Vec3 oldPos = companion.position();
            companion.teleportTo(destination, oldPos.x, oldPos.y, oldPos.z, Set.<RelativeMovement>of(),
                    companion.getYRot(), companion.getXRot());
        }
        if (!companions.isEmpty()) {
            player.sendSystemMessage(Component.literal("Teleported " + companions.size() + " nearby entities with you!"));
        }
    }
}

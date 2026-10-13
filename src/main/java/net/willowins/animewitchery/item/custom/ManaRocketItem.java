package net.willowins.animewitchery.item.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.network.ManaSyncPayload;

public class ManaRocketItem extends Item {
    private static final int MANA_COST = 500;
    private static final double BOOST_POWER = 5.0;

    public ManaRocketItem(Properties properties) {
        super(properties);
    }

    public static void doRocketEffect(Level level, Player player) {
        Vec3 look = player.getLookAngle().normalize();
        player.push(look.x * BOOST_POWER, look.y * BOOST_POWER * 0.6, look.z * BOOST_POWER);
        player.hasImpulse = true;
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM,
                SoundSource.PLAYERS, 2.0F, 0.9F);

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY(), player.getZ(),
                    40, 0.25, 0.25, 0.25, 0.02);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.pass(stack);
        }
        if (!player.isFallFlying()) {
            player.displayClientMessage(Component.literal("You must be gliding to use this rocket!"), true);
            return InteractionResultHolder.fail(stack);
        }

        ManaData mana = player.getData(ModAttachments.MANA);
        if (mana.current() < MANA_COST) {
            player.displayClientMessage(Component.literal("Not enough mana!"), true);
            return InteractionResultHolder.fail(stack);
        }

        ManaData updated = mana.addMana(-MANA_COST);
        player.setData(ModAttachments.MANA, updated);
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(updated.current(), updated.max()));
        }
        doRocketEffect(level, player);
        player.getCooldowns().addCooldown(this, 30);
        return InteractionResultHolder.success(stack);
    }
}

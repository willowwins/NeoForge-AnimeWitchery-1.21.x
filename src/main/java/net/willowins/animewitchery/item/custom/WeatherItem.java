package net.willowins.animewitchery.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.ModDataComponentTypes;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;
import net.neoforged.neoforge.network.PacketDistributor;
import net.minecraft.server.level.ServerPlayer;
import net.willowins.animewitchery.network.ManaSyncPayload;

public class WeatherItem extends Item {
    private static final int CLEAR_COST = 500;
    private static final int RAIN_COST = 1000;
    private static final int THUNDER_COST = 2000;

    public WeatherItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (!data.contains("Mode")) {
            data.putInt("Mode", 1);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
        }

        if (player.isShiftKeyDown()) {
            int nextMode = (data.getInt("Mode") + 1) % 3;
            data.putInt("Mode", nextMode);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal("Weather Mode: " + modeName(nextMode)), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK,
                        SoundSource.PLAYERS, 0.5F, 1.2F);
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) {
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        int mode = data.getInt("Mode");
        int cost = switch (mode) {
            case 0 -> CLEAR_COST;
            case 1 -> RAIN_COST;
            default -> THUNDER_COST;
        };
        if (!consumeAvailableMana(player, cost)) {
            player.displayClientMessage(Component.literal("Not enough mana (" + cost + ")"), true);
            return InteractionResultHolder.fail(stack);
        }

        switch (mode) {
            case 0 -> {
                serverLevel.setWeatherParameters(6000, 0, false, false);
                player.displayClientMessage(Component.literal("Weather cleared!"), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEACON_ACTIVATE,
                        SoundSource.PLAYERS, 1.0F, 1.5F);
            }
            case 1 -> {
                serverLevel.setWeatherParameters(0, 6000, true, false);
                player.displayClientMessage(Component.literal("Rain summoned!"), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_SPLASH,
                        SoundSource.PLAYERS, 1.0F, 0.8F);
            }
            default -> {
                serverLevel.setWeatherParameters(0, 6000, true, true);
                player.displayClientMessage(Component.literal("Thunderstorm summoned!"), true);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER,
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        player.getCooldowns().addCooldown(this, 60);
        return InteractionResultHolder.success(stack);
    }

    public static float getMode(ItemStack stack) {
        CompoundTag data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        return data.contains("Mode") ? Math.clamp(data.getInt("Mode"), 0, 2) / 2.0F : 0.5F;
    }

    private static String modeName(int mode) {
        return switch (mode) {
            case 0 -> "Sunny";
            case 1 -> "Rainy";
            default -> "Thunder";
        };
    }

    private static boolean consumeAvailableMana(Player player, int cost) {
        ManaData mana = player.getData(ModAttachments.MANA);
        int stored = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            stored += player.getInventory().getItem(slot).getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
        }
        if (mana.current() + stored < cost) {
            return false;
        }

        int fromPlayer = Math.min(mana.current(), cost);
        ManaData updated = mana.addMana(-fromPlayer);
        player.setData(ModAttachments.MANA, updated);
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ManaSyncPayload(updated.current(), updated.max()));
        }

        int remaining = cost - fromPlayer;
        for (int slot = 0; slot < player.getInventory().getContainerSize() && remaining > 0; slot++) {
            ItemStack catalyst = player.getInventory().getItem(slot);
            int available = catalyst.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
            if (available <= 0) {
                continue;
            }
            int drained = Math.min(available, remaining);
            int left = available - drained;
            if (left == 0) {
                catalyst.remove(ModDataComponentTypes.STORED_MANA.get());
            } else {
                catalyst.set(ModDataComponentTypes.STORED_MANA.get(), left);
            }
            remaining -= drained;
        }
        return remaining == 0;
    }
}

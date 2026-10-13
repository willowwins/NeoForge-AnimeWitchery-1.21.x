package net.willowins.animewitchery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.ModDataComponentTypes;
import net.willowins.animewitchery.mana.PlayerMana;

import java.util.List;

public class FuelRodItem extends Item {
    public static final int MAX_MANA = 50000;

    public FuelRodItem(Properties properties) {
        super(properties);
    }

    public static int getStoredMana(ItemStack stack) {
        return stack.getOrDefault(ModDataComponentTypes.STORED_MANA.get(), 0);
    }

    public static void setStoredMana(ItemStack stack, int amount) {
        int clamped = Math.max(0, Math.min(amount, MAX_MANA));
        if (clamped == 0) {
            stack.remove(ModDataComponentTypes.STORED_MANA.get());
        } else {
            stack.set(ModDataComponentTypes.STORED_MANA.get(), clamped);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hand == InteractionHand.OFF_HAND) {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }

        if (!level.isClientSide) {
            int stored = getStoredMana(stack);
            int playerCurrent = PlayerMana.getMana(player);
            int playerMax = PlayerMana.getMaxMana(player);

            if (player.isShiftKeyDown()) {
                if (stored > 0) {
                    int space = playerMax - playerCurrent;
                    int amountToReturn = Math.min(stored, space);
                    if (amountToReturn > 0) {
                        PlayerMana.setMana(player, playerCurrent + amountToReturn);
                        setStoredMana(stack, stored - amountToReturn);
                        player.displayClientMessage(Component.literal("Discharged " + amountToReturn + " mana from rod").withStyle(ChatFormatting.GREEN), true);
                    } else {
                        player.displayClientMessage(Component.literal("You are already at full mana").withStyle(ChatFormatting.YELLOW), true);
                    }
                } else {
                    player.displayClientMessage(Component.literal("Fuel Rod is empty").withStyle(ChatFormatting.GRAY), true);
                }
            } else {
                if (stored < MAX_MANA) {
                    int spaceInRod = MAX_MANA - stored;
                    int amountToTake = Math.min(playerCurrent, spaceInRod);
                    if (amountToTake > 0) {
                        if (PlayerMana.consume(player, amountToTake)) {
                            setStoredMana(stack, stored + amountToTake);
                            player.displayClientMessage(Component.literal("Charged Fuel Rod by " + amountToTake + " mana").withStyle(ChatFormatting.AQUA), true);
                        } else {
                            player.displayClientMessage(Component.literal("Failed to consume mana").withStyle(ChatFormatting.RED), true);
                        }
                    } else {
                        player.displayClientMessage(Component.literal("You have no mana to charge with").withStyle(ChatFormatting.GRAY), true);
                    }
                } else {
                    player.displayClientMessage(Component.literal("Fuel Rod already full").withStyle(ChatFormatting.YELLOW), true);
                }
            }
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int stored = getStoredMana(stack);
        tooltip.add(Component.literal("Stored Mana: " + stored + " / " + MAX_MANA).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int stored = getStoredMana(stack);
        return Math.round((float) stored / MAX_MANA * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x3B9EFF;
    }
}
package net.willowins.animewitchery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.willowins.animewitchery.mana.PlayerMana;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class FulfillingTiaraItem extends Item implements ICurioItem {
    private static final int MANA_COST = 100;
    private static final float HUNGER_THRESHOLD = 10.0f;

    public FulfillingTiaraItem(Properties properties) {
        super(properties);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.level().isClientSide || !(entity instanceof Player player)) {
            return;
        }

        if (entity.level().getGameTime() % 20 != 0) {
            return;
        }

        if (player.getFoodData().getFoodLevel() <= HUNGER_THRESHOLD) {
            if (PlayerMana.getMana(player) >= MANA_COST) {
                PlayerMana.consume(player, MANA_COST);
                player.getFoodData().setFoodLevel(20);
                player.getFoodData().setSaturation(20.0f);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Automatically refills hunger at 50%").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal("Costs: 100 Mana").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("Restores full hunger and saturation").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
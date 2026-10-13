package net.willowins.animewitchery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class LatentManaPendantItem extends Item implements ICurioItem {
    private static final int BONUS_MANA = 20000;

    public LatentManaPendantItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Expands latent mana pool").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.literal("+20,000 Maximum Mana").withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("Must be worn in necklace slot").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    public static int getBonusMana(LivingEntity entity) {
        return BONUS_MANA;
    }
}
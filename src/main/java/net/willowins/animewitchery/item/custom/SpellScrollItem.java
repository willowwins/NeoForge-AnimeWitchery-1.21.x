package net.willowins.animewitchery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/** Spell scroll item ported from Fabric; scrolls identify and encode a specific spell. */
public class SpellScrollItem extends Item {
    private final String spellName;

    public SpellScrollItem(Properties properties, String spellName) {
        super(properties);
        this.spellName = spellName;
    }

    public String getSpellName() {
        return spellName;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public Component getName(ItemStack stack) {
        return super.getName(stack).copy().withStyle(colorForSpell(spellName));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip,
                                TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.animewitchery.spell_scroll", spellName));
        tooltip.add(Component.translatable("tooltip.animewitchery.spell_scroll_use"));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    private static ChatFormatting colorForSpell(String spellName) {
        return switch (spellName) {
            case "Fire Blast" -> ChatFormatting.RED;
            case "Water Shield" -> ChatFormatting.AQUA;
            case "Earth Spike" -> ChatFormatting.DARK_GREEN;
            case "Wind Gust" -> ChatFormatting.WHITE;
            case "Healing Wave" -> ChatFormatting.GREEN;
            case "Wither Touch" -> ChatFormatting.DARK_PURPLE;
            case "Light Burst" -> ChatFormatting.YELLOW;
            case "Shadow Bind" -> ChatFormatting.DARK_GRAY;
            default -> ChatFormatting.RESET;
        };
    }
}

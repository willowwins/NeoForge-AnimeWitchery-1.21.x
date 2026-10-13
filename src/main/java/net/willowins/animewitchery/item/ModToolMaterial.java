package net.willowins.animewitchery.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum ModToolMaterial implements Tier {
    SILVER(BlockTags.INCORRECT_FOR_IRON_TOOL, 1561, 13.0F, 3.0F, 22,
            () -> Ingredient.of(ModItems.SILVERINGOT.get())),
    OBELISK(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 12.0F, 5.0F, 25,
            () -> Ingredient.of(ModItems.ENCHANTED_CRYSTAL.get())),
    RESONANT(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 5.0F, 25,
            () -> Ingredient.of(ModItems.RESONANT_CATALYST.get())),
    COPPER(BlockTags.INCORRECT_FOR_STONE_TOOL, 250, 6.0F, 2.0F, 18,
            () -> Ingredient.of(Items.COPPER_INGOT)),
    HALOIC(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2500, 10.0F, 6.0F, 25,
            () -> Ingredient.of(ModItems.HALOIC_INGOT.get()));

    private final TagKey<Block> incorrectBlocksForDrops;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(TagKey<Block> incorrectBlocksForDrops, int itemDurability, float miningSpeed, float attackDamage,
            int enchantability, Supplier<Ingredient> repairIngredient) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getUses() {
        return this.itemDurability;
    }

    @Override
    public float getSpeed() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocksForDrops;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
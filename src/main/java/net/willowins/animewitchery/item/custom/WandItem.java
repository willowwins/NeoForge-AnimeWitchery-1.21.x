package net.willowins.animewitchery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.willowins.animewitchery.ModAttachments;
import net.willowins.animewitchery.ModDataComponentTypes;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.StarlightInfusionPedestalBlockEntity;

import java.util.Map;

/**
 * The mod's "wrench" tool. Right-click works on several things:
 * - Block transmutation (turns certain blocks into others, costing mana).
 * - Starlight Infusion Altar selection (stores the bound altar on the wand).
 * - Starlight Infusion Pedestal binding / unbinding to the selected altar.
 */
public class WandItem extends Item {

    private static final Map<Block, Block> TRANSMUTE_MAP = Map.of(
            Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN,
            Blocks.OAK_LOG, Blocks.DARK_OAK_LOG,
            Blocks.DARK_OAK_LOG, Blocks.SPRUCE_LOG
    );

    // How much mana a single transmutation costs.
    private static final int MANA_COST = 10;

    public WandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Block clickedBlock = level.getBlockState(clickedPos).getBlock();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) {
            return InteractionResult.PASS;
        }

        // Altar selection: remember the altar this wand is bound to.
        if (clickedBlock == ModBlocks.STARLIGHT_INFUSION_ALTAR.get()) {
            if (!level.isClientSide) {
                stack.set(ModDataComponentTypes.BOUND_ALTAR.get(), clickedPos);
                player.displayClientMessage(Component.literal("Altar selected at " + clickedPos.toShortString()), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        // Pedestal binding: bind/unbind this pedestal to the wand's selected altar.
        if (clickedBlock == ModBlocks.STARLIGHT_INFUSION_PEDESTAL.get()) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            if (level.getBlockEntity(clickedPos) instanceof StarlightInfusionPedestalBlockEntity pedestal) {
                BlockPos altarPos = stack.getOrDefault(ModDataComponentTypes.BOUND_ALTAR.get(), null);
                if (altarPos == null) {
                    player.displayClientMessage(Component.literal("Select an altar first (right-click an altar with the wand)."), true);
                } else if (pedestal.isBoundTo(altarPos)) {
                    pedestal.setBoundAltar(null);
                    player.displayClientMessage(Component.literal("Pedestal unbound."), true);
                } else {
                    pedestal.setBoundAltar(altarPos);
                    player.displayClientMessage(Component.literal("Pedestal bound to altar."), true);
                }
            }
            return InteractionResult.SUCCESS;
        }

        // Fall back to basic block transmutation.
        if (!TRANSMUTE_MAP.containsKey(clickedBlock)) {
            return InteractionResult.PASS;
        }

        // These operations must run on the logical server.
        if (!level.isClientSide) {
            net.willowins.animewitchery.ManaData manaData = player.getData(ModAttachments.MANA);

            if (manaData.current() >= MANA_COST) {
                player.setData(ModAttachments.MANA, manaData.addMana(-MANA_COST));
                level.setBlockAndUpdate(clickedPos, TRANSMUTE_MAP.get(clickedBlock).defaultBlockState());
                level.playSound(null, clickedPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS);
            } else {
                player.displayClientMessage(Component.literal("Not enough mana! Needs " + MANA_COST), true);
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.SUCCESS;
    }
}

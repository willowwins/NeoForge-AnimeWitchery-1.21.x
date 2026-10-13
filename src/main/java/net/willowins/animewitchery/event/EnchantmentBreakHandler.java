package net.willowins.animewitchery.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.willowins.animewitchery.enchantments.EnchantmentUtils;
import net.willowins.animewitchery.enchantments.ModEnchantments;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Port of the Fabric block-break enchantment handlers (excavation / blasting / chainsaw) onto
 * NeoForge's single {@link BlockEvent.BreakEvent}, which fires before the block is removed.
 *
 * <ul>
 *     <li><b>Blasting</b> cancels the vanilla break and smelts the drops through the blast furnace
 *     recipe table, matching the Fabric BEFORE handler.</li>
 *     <li><b>Excavation</b> and <b>Chainsaw</b> break their extra blocks here; the origin block is
 *     still removed normally by vanilla afterwards.</li>
 * </ul>
 */
public final class EnchantmentBreakHandler {
    private static final Set<BlockPos> ACTIVE = ConcurrentHashMap.newKeySet();

    private EnchantmentBreakHandler() {
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        BlockPos pos = event.getPos().immutable();
        if (!ACTIVE.add(pos)) {
            return;
        }
        try {
            ItemStack tool = player.getMainHandItem();
            BlockState state = event.getState();

            if (handleBlasting(level, player, pos, state, tool)) {
                event.setCanceled(true);
                return;
            }
            handleChainsaw(level, player, pos, state, tool);
            handleExcavation(level, player, pos, state, tool);
        } finally {
            ACTIVE.remove(pos);
        }
    }

    private static boolean handleBlasting(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStack tool) {
        if (EnchantmentUtils.getLevel(level, ModEnchantments.BLAST_ENCHANT, tool) <= 0) {
            return false;
        }
        if (!tool.isCorrectToolForDrops(state)) {
            return false;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        List<ItemStack> originalDrops = Block.getDrops(state, level, pos, blockEntity, player, tool);

        level.levelEvent(2001, pos, Block.getId(state));
        level.destroyBlock(pos, false, player);

        List<ItemStack> finalDrops = new ArrayList<>();
        float totalXp = 0.0F;

        for (ItemStack drop : originalDrops) {
            if (drop.isEmpty()) {
                continue;
            }
            ItemStack one = drop.copy();
            one.setCount(1);

            Optional<RecipeHolder<BlastingRecipe>> match = level.getRecipeManager()
                    .getRecipeFor(RecipeType.BLASTING, new SingleRecipeInput(one), level);

            if (match.isPresent()) {
                BlastingRecipe recipe = match.get().value();
                ItemStack resultPerOne = recipe.getResultItem(level.registryAccess()).copy();
                if (!resultPerOne.isEmpty()) {
                    resultPerOne.setCount(resultPerOne.getCount() * drop.getCount());
                    finalDrops.add(resultPerOne);
                    totalXp += recipe.getExperience() * drop.getCount();
                    continue;
                }
            }
            finalDrops.add(drop.copy());
        }

        for (ItemStack out : finalDrops) {
            if (!out.isEmpty()) {
                Block.popResource(level, pos, out);
            }
        }
        if (totalXp > 0.0F) {
            ExperienceOrb.award(level, pos.getCenter(), Math.round(totalXp));
        }
        return true;
    }

    private static void handleExcavation(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStack tool) {
        if (EnchantmentUtils.getLevel(level, ModEnchantments.EXCAVATE_ENCHANT, tool) <= 0) {
            return;
        }
        if (!tool.isCorrectToolForDrops(state)) {
            return;
        }

        HitResult hit = player.pick(5.0D, 0.0F, false);
        if (!(hit instanceof BlockHitResult blockHit) || blockHit.getDirection() == null) {
            return;
        }
        Direction.Axis axis = blockHit.getDirection().getAxis();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                BlockPos target = switch (axis) {
                    case X -> pos.offset(0, dx, dy);
                    case Y -> pos.offset(dx, 0, dy);
                    case Z -> pos.offset(dx, dy, 0);
                };
                BlockState targetState = level.getBlockState(target);
                if (!tool.isCorrectToolForDrops(targetState)) {
                    continue;
                }
                breakGuarded(level, target, player);
            }
        }
    }

    private static void handleChainsaw(ServerLevel level, ServerPlayer player, BlockPos pos, BlockState state, ItemStack tool) {
        if (EnchantmentUtils.getLevel(level, ModEnchantments.CHAINSAW_ENCHANT, tool) <= 0) {
            return;
        }
        if (!state.is(BlockTags.LOGS)) {
            return;
        }

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> toBreak = new ArrayList<>();
        visited.add(pos);

        for (BlockPos neighbor : getNeighbors(pos)) {
            if (isLog(level, neighbor) && visited.add(neighbor)) {
                queue.add(neighbor);
            }
        }

        int maxBlocks = 64;
        while (!queue.isEmpty() && toBreak.size() < maxBlocks) {
            BlockPos current = queue.poll();
            toBreak.add(current);
            for (BlockPos neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor) && isLog(level, neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        BlockPos playerPos = player.blockPosition();
        for (BlockPos target : toBreak) {
            if (ACTIVE.contains(target)) {
                continue;
            }
            ACTIVE.add(target);
            try {
                BlockState targetState = level.getBlockState(target);
                BlockEntity targetEntity = level.getBlockEntity(target);
                List<ItemStack> drops = Block.getDrops(targetState, level, target, targetEntity, player, tool);
                for (ItemStack drop : drops) {
                    Block.popResource(level, playerPos, drop);
                }
                if (tool.isDamageableItem()) {
                    tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    if (tool.isEmpty()) {
                        break;
                    }
                }
                level.setBlock(target, Blocks.AIR.defaultBlockState(), 3);
            } finally {
                ACTIVE.remove(target);
            }
        }
    }

    private static void breakGuarded(ServerLevel level, BlockPos target, ServerPlayer player) {
        if (!ACTIVE.add(target)) {
            return;
        }
        try {
            level.destroyBlock(target, true, player);
        } finally {
            ACTIVE.remove(target);
        }
    }

    private static boolean isLog(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(BlockTags.LOGS);
    }

    private static List<BlockPos> getNeighbors(BlockPos pos) {
        List<BlockPos> neighbors = new ArrayList<>(26);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    neighbors.add(pos.offset(x, y, z));
                }
            }
        }
        return neighbors;
    }
}

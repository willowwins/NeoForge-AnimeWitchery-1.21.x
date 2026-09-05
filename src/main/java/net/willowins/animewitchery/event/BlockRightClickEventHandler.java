package net.willowins.animewitchery.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.fluid.ModFluids;
import net.willowins.animewitchery.recipe.BlockTransmutationRecipe;
import net.willowins.animewitchery.recipe.ModRecipes;

import java.util.List;

@EventBusSubscriber(modid = "animewitchery")
public class BlockRightClickEventHandler {

    // 25% of a bucket (1000 mb) = 250 mb per bottle.
    private static final int BOTTLE_CAPACITY = 250;

    @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();

        // 1. Safety check for hand and null players
        if (player == null || event.getHand() != InteractionHand.MAIN_HAND) return;

        ItemStack mainHand = player.getMainHandItem();

        // 2. Clear out early if the hand is empty
        if (mainHand.isEmpty()) return;

        // Fill / empty starlight bottles (300 mb) against any fluid container.
        if (isStarlightBottle(mainHand) || mainHand.is(Items.GLASS_BOTTLE)) {
            IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, event.getFace());
            if (handler != null && handleBottleInteraction(level, pos, player, mainHand, handler)) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                return;
            }
        }

        Block clickedBlock = level.getBlockState(pos).getBlock();
        ItemStack offHand = player.getOffhandItem();

        // 3. Query the recipe manager for your custom recipe type
        List<RecipeHolder<BlockTransmutationRecipe>> recipes = level.getRecipeManager()
                .getAllRecipesFor(ModRecipes.BLOCK_TRANSMUTATION_TYPE.get());

        // 4. Loop through recipes to find a matching rule configuration
        for (RecipeHolder<BlockTransmutationRecipe> holder : recipes) {
            BlockTransmutationRecipe recipe = holder.value();

            if (recipe.matches(clickedBlock, mainHand, offHand)) {

                // Perform the block transformation and drop rewards on the logical server
                if (!level.isClientSide()) {
                    // Consume 1 item from the main hand
                    mainHand.shrink(1);

                    // Only shrink offhand item if required by the JSON recipe configuration
                    if (recipe.offhand().isPresent()) {
                        offHand.shrink(1);
                    }

                    // FIX 1: Manually trigger the vanilla block-breaking particles and sound effects
                    level.levelEvent(2001, pos, Block.getId(clickedBlock.defaultBlockState()));

                    // FIX 2: Turn the targeted block into air safely without breaking the engine sequence
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                    // Spawn the resultant item stack
                    ItemStack resultDrop = recipe.result().copy();
                    ItemEntity dropEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, resultDrop);
                    level.addFreshEntity(dropEntity);
                }

                // 5. Cancel vanilla placement systems to finish execution cleanly
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                break; // Stop checking other recipes once a match succeeds
            }
        }
    }

    private static boolean isStarlightBottle(ItemStack stack) {
        return stack.is(ModItems.STARLIGHT_BOTTLE.get());
    }

    /**
     * Handles filling an empty glass bottle from, or emptying a starlight bottle into,
     * a fluid container. Returns true if the interaction was consumed (a bottle was used).
     */
    private static boolean handleBottleInteraction(Level level, BlockPos pos, Player player, ItemStack mainHand, IFluidHandler handler) {
        // Only run data changes on the server.
        if (level.isClientSide) {
            return false;
        }

        if (isStarlightBottle(mainHand)) {
            // Empty: pour 300 mb of starlight into the container.
            FluidStack toPour = new FluidStack(ModFluids.STARLIGHT_SOURCE.get(), BOTTLE_CAPACITY);
            int poured = handler.fill(toPour, IFluidHandler.FluidAction.EXECUTE);
            if (poured >= BOTTLE_CAPACITY) {
                mainHand.shrink(1);
                giveOrDrop(player, level, pos, new ItemStack(Items.GLASS_BOTTLE));
                return true;
            }
        } else if (mainHand.is(Items.GLASS_BOTTLE)) {
            // Fill: take 300 mb of starlight from the container, if available.
            if (handler.drain(BOTTLE_CAPACITY, IFluidHandler.FluidAction.SIMULATE).getAmount() >= BOTTLE_CAPACITY) {
                handler.drain(BOTTLE_CAPACITY, IFluidHandler.FluidAction.EXECUTE);
                mainHand.shrink(1);
                giveOrDrop(player, level, pos, new ItemStack(ModItems.STARLIGHT_BOTTLE.get()));
                return true;
            }
        }
        return false;
    }

    private static void giveOrDrop(Player player, Level level, BlockPos pos, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
        }
    }
}




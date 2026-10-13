package net.willowins.animewitchery.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.willowins.animewitchery.item.ModItems;

@EventBusSubscriber(modid = "animewitchery")
public final class TorchFlowerEssenceEntityHandler {
    private TorchFlowerEssenceEntityHandler() {}

    @SubscribeEvent
    public static void onItemFrameInteract(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof ItemFrame frame)) return;
        var player = event.getEntity();
        var essence = player.getItemInHand(event.getHand());
        if (!essence.is(ModItems.TORCH_FLOWER_ESSENCE.get())) return;

        if (!event.getLevel().isClientSide && !frame.isInvisible()) {
            frame.setInvisible(true);
            if (!player.getAbilities().instabuild) essence.shrink(1);
        }
        // Prevent the vanilla item-frame interaction from inserting the essence as its display item.
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}

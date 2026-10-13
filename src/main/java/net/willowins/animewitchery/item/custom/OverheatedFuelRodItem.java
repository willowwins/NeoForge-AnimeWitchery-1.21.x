package net.willowins.animewitchery.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.willowins.animewitchery.item.ModItems;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class OverheatedFuelRodItem extends Item implements ICurioItem {
    public OverheatedFuelRodItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack mainHand = player.getMainHandItem();
        if (mainHand.is(ModItems.OVERHEATED_FUEL_ROD.get())) {
            level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0f, 1.0f);
            mainHand.shrink(1);
            player.addItem(new ItemStack(ModItems.FUEL_ROD.get()));
        }
        ItemStack offHand = player.getOffhandItem();
        if (offHand.is(ModItems.OVERHEATED_FUEL_ROD.get())) {
            level.playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0f, 1.0f);
            offHand.shrink(1);
            player.addItem(new ItemStack(ModItems.FUEL_ROD.get()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
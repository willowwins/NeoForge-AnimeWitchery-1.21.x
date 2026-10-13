package net.willowins.animewitchery.block.custom;

import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2473;
import net.minecraft.class_2647;
import net.minecraft.class_2680;
import net.minecraft.class_4970.class_2251;
import net.willowins.animewitchery.block.ModBlocks;

public class RosewillowSaplingBlock extends class_2473 {
   public RosewillowSaplingBlock(class_2647 generator, class_2251 settings) {
      super(generator, settings);
   }

   public boolean method_9695(class_2680 floor, class_1922 world, class_2338 pos) {
      return floor.method_27852(ModBlocks.ROSEWILLOW_ROOTS) || super.method_9695(floor, world, pos);
   }
}

package net.willowins.animewitchery.world.gen.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.class_2338;
import net.minecraft.class_3746;
import net.minecraft.class_4643;
import net.minecraft.class_4647;
import net.minecraft.class_4648;
import net.minecraft.class_5819;
import net.minecraft.class_6017;
import net.minecraft.class_4647.class_5208;
import net.minecraft.class_4647.class_8179;
import net.willowins.animewitchery.world.gen.ModFoliagePlacerTypes;

public class RosewillowFoliagePlacer extends class_4647 {
   public static final Codec<RosewillowFoliagePlacer> CODEC = RecordCodecBuilder.create(
      instance -> method_30411(instance)
            .and(Codec.intRange(0, 16).fieldOf("height").forGetter(placer -> placer.height))
            .apply(instance, RosewillowFoliagePlacer::new)
   );
   private final int height;

   public RosewillowFoliagePlacer(class_6017 radius, class_6017 offset, int height) {
      super(radius, offset);
      this.height = height;
   }

   protected class_4648<?> method_28843() {
      return ModFoliagePlacerTypes.ROSEWILLOW_FOLIAGE_PLACER;
   }

   protected void method_23448(
      class_3746 world,
      class_8179 placer,
      class_5819 random,
      class_4643 config,
      int trunkHeight,
      class_5208 treeNode,
      int foliageHeight,
      int radius,
      int offset
   ) {
      class_2338 center = treeNode.method_27388();
      int currentRadius = radius + random.method_43048(2);

      for (int y = offset; y >= -foliageHeight - 2; y--) {
         int yDist = Math.abs(y);
         int layerRadius = currentRadius;
         if (yDist >= currentRadius) {
            layerRadius = currentRadius - 2;
         } else if (yDist >= currentRadius - 1) {
            layerRadius = currentRadius - 1;
         }

         if (layerRadius >= 0) {
            this.method_23449(world, placer, random, config, center, layerRadius, y, treeNode.method_27390());
         }
      }
   }

   public int method_26989(class_5819 random, int trunkHeight, class_4643 config) {
      return this.height;
   }

   protected boolean method_23451(class_5819 random, int dx, int dy, int dz, int radius, boolean giantTrunk) {
      return dx * dx + dz * dz > radius * radius;
   }
}

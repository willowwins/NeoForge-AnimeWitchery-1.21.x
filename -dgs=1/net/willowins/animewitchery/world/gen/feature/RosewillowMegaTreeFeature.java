package net.willowins.animewitchery.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.class_2338;
import net.minecraft.class_2397;
import net.minecraft.class_2465;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_3031;
import net.minecraft.class_3111;
import net.minecraft.class_3481;
import net.minecraft.class_5281;
import net.minecraft.class_5819;
import net.minecraft.class_5821;
import net.minecraft.class_2350.class_2351;
import net.willowins.animewitchery.block.ModBlocks;

public class RosewillowMegaTreeFeature extends class_3031<class_3111> {
   public RosewillowMegaTreeFeature(Codec<class_3111> configCodec) {
      super(configCodec);
   }

   public boolean method_13151(class_5821<class_3111> context) {
      class_5281 world = context.method_33652();
      class_2338 origin = context.method_33655();
      class_5819 random = context.method_33654();
      class_2680 down = world.method_8320(origin.method_10074());
      if (!down.method_26164(class_3481.field_29822)) {
         return false;
      } else {
         if (random.method_43048(10) == 0) {
            this.safeSetBlock(world, origin.method_10074(), ModBlocks.ROSEWILLOW_BULB.method_9564(), origin);
         }

         int height = 27 + random.method_43048(8);

         for (int y = 0; y < height - 2; y++) {
            double radius = 2.5;
            if (y < 10) {
               double progress = (double)(10 - y) / 10.0;
               radius += 3.5 * progress * progress;
               radius += ((double)random.method_43057() - 0.5) * 0.2;
            }

            this.generateCircle(world, origin.method_10069(0, y, 0), radius, ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         }

         this.generateRoots(world, origin, random, origin);
         int branchCount = 18 + random.method_43048(6);
         int startY = height / 2;

         for (int i = 0; i < branchCount; i++) {
            float angle = random.method_43057() * 6.28F;
            int y = startY + random.method_43048(height - startY - 2);
            float relativeHeight = (float)(y - startY) / (float)(height - startY);
            float pitch = -0.1F + relativeHeight * 0.9F;
            int length = 12 + random.method_43048(8);
            length = (int)((float)length * (1.2F - relativeHeight * 0.5F));
            this.generateBranchWithLeaves(world, origin.method_10086(y), angle, pitch, length, random, origin);
         }

         this.generateCanopyCluster(world, origin.method_10086(height), 6, random, origin);
         return true;
      }
   }

   private void generateCircle(class_5281 world, class_2338 center, double radius, class_2680 state, class_2338 origin) {
      int r = (int)Math.ceil(radius);

      for (int x = -r; x <= r; x++) {
         for (int z = -r; z <= r; z++) {
            if (((double)x + 0.5) * ((double)x + 0.5) + ((double)z + 0.5) * ((double)z + 0.5) <= radius * radius) {
               class_2338 logPos = center.method_10069(x, 0, z);
               this.safeSetBlock(world, logPos, state, origin);
               class_2338 groundPos = logPos.method_10074();
               if (world.method_8320(groundPos).method_26164(class_3481.field_29822)) {
                  this.safeSetBlock(world, groundPos, ModBlocks.ROSEWILLOW_ROOTS.method_9564(), origin);
               }
            }
         }
      }
   }

   private void generateBranchWithLeaves(class_5281 world, class_2338 start, float angle, float pitch, int length, class_5819 random, class_2338 origin) {
      double dx = Math.cos((double)angle);
      double dz = Math.sin((double)angle);
      double dy = Math.tan((double)pitch);
      double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
      dx /= dist;
      dy /= dist;
      dz /= dist;

      for (int i = 0; i < length; i++) {
         class_2338 next = start.method_10069((int)(dx * (double)i), (int)(dy * (double)i), (int)(dz * (double)i));
         this.safeSetBlock(world, next, (class_2680)ModBlocks.ROSEWILLOW_LOG.method_9564().method_11657(class_2465.field_11459, class_2351.field_11048), origin);
         if ((double)i > (double)length * 0.3) {
            int radius = 4 + (int)((double)((float)((double)i - (double)length * 0.3)) / ((double)length * 0.7) * 3.0);
            if (random.method_43048(3) == 0) {
               this.generateCanopyCluster(world, next, radius, random, origin);
            }
         }

         if (i == length - 1) {
            this.generateCanopyCluster(world, next, 8, random, origin);
         }
      }
   }

   private void safeSetBlock(class_5281 world, class_2338 pos, class_2680 state, class_2338 origin) {
      int originChunkX = origin.method_10263() >> 4;
      int originChunkZ = origin.method_10260() >> 4;
      int targetChunkX = pos.method_10263() >> 4;
      int targetChunkZ = pos.method_10260() >> 4;
      if (Math.abs(originChunkX - targetChunkX) <= 1 && Math.abs(originChunkZ - targetChunkZ) <= 1) {
         if (world.method_22347(pos)
            || world.method_8320(pos).method_26164(class_3481.field_15503)
            || world.method_8320(pos).method_26164(class_3481.field_15462)
            || world.method_8320(pos).method_27852(ModBlocks.ROSEWILLOW_VINES)
            || world.method_8320(pos).method_27852(ModBlocks.ROSEWILLOW_VINES_TIP)) {
            if (state.method_27852(ModBlocks.ROSEWILLOW_LOG) && world.method_8409().method_43048(5) == 0) {
               state = (class_2680)ModBlocks.ROSEWILLOW_LOG_BLOOMING
                  .method_9564()
                  .method_11657(class_2465.field_11459, (class_2351)state.method_11654(class_2465.field_11459));
            }

            world.method_8652(pos, state, 3);
         }
      }
   }

   private void generateCanopyCluster(class_5281 world, class_2338 center, int radius, class_5819 random, class_2338 origin) {
      if (radius > 5) {
         int offset = radius / 2;
         this.safeSetBlock(world, center.method_10069(offset, 0, 0), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         this.safeSetBlock(world, center.method_10069(-offset, 0, 0), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         this.safeSetBlock(world, center.method_10069(0, 0, offset), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         this.safeSetBlock(world, center.method_10069(0, 0, -offset), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         this.safeSetBlock(world, center.method_10069(0, offset, 0), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
      }

      for (int y = -radius / 2; y <= radius / 2; y++) {
         int currentRadius = radius - Math.abs(y);

         for (int x = -currentRadius; x <= currentRadius; x++) {
            for (int z = -currentRadius; z <= currentRadius; z++) {
               if (x * x + z * z <= currentRadius * currentRadius) {
                  class_2338 leafPos = center.method_10069(x, y, z);
                  if (world.method_22347(leafPos)) {
                     this.safeSetBlock(
                        world,
                        leafPos,
                        (class_2680)((class_2680)ModBlocks.ROSEWILLOW_LEAVES.method_9564().method_11657(class_2397.field_11200, false))
                           .method_11657(class_2397.field_11199, 1),
                        origin
                     );
                     if (y < -radius / 3 && random.method_43048(6) == 0) {
                        this.generateVine(world, leafPos.method_10074(), random, origin);
                     }
                  }
               }
            }
         }
      }
   }

   private void generateRoots(class_5281 world, class_2338 pos, class_5819 random, class_2338 origin) {
      for (int i = 0; i < 8; i++) {
         float angle = random.method_43057() * 6.28F;
         int dist = 3 + random.method_43048(3);
         int dx = (int)(Math.cos((double)angle) * (double)dist);
         int dz = (int)(Math.sin((double)angle) * (double)dist);
         class_2338 rootPos = pos.method_10069(dx, 0, dz);
         if (world.method_22347(rootPos)) {
            this.safeSetBlock(world, rootPos, ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         }

         if (random.method_43056() && world.method_22347(rootPos.method_10074())) {
            this.safeSetBlock(world, rootPos.method_10074(), ModBlocks.ROSEWILLOW_LOG.method_9564(), origin);
         }
      }
   }

   private void generateVine(class_5281 world, class_2338 pos, class_5819 random, class_2338 origin) {
      if (world.method_22347(pos)) {
         int length = 3 + random.method_43048(10);

         for (int i = 0; i < length; i++) {
            class_2338 vinePos = pos.method_10087(i);
            if (!world.method_22347(vinePos)) {
               break;
            }

            if (i == length - 1) {
               this.safeSetBlock(world, vinePos, ModBlocks.LARGE_ROSEWILLOW_BLOSSOM.method_9564(), origin);
            } else if (i == length - 2) {
               this.safeSetBlock(world, vinePos, ModBlocks.ROSEWILLOW_VINES_TIP.method_9564(), origin);
            } else {
               class_2680 vineState = ModBlocks.ROSEWILLOW_VINES.method_9564();
               if (random.method_43048(3) == 0) {
                  vineState = (class_2680)vineState.method_11657(class_2741.field_28716, true);
               }

               this.safeSetBlock(world, vinePos, vineState, origin);
            }
         }
      }
   }
}

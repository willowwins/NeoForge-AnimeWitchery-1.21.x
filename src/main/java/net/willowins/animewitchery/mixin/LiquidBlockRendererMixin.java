package net.willowins.animewitchery.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.willowins.animewitchery.fluid.ModFluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public class LiquidBlockRendererMixin {

    @Inject(method = "tesselate", at = @At("HEAD"), cancellable = true)
    private void animewitchery$skipAntifluid(BlockAndTintGetter level, BlockPos pos, VertexConsumer buffer, BlockState blockState, FluidState fluidState, CallbackInfo ci) {
        if (fluidState.getType() == ModFluids.ANTIFLUID_SOURCE.get()
                || fluidState.getType() == ModFluids.ANTIFLUID_FLOWING.get()) {
            ci.cancel();
        }
    }
}

package net.willowins.animewitchery.mixin;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class TorchflowerLightMixin {
    @Shadow public abstract net.minecraft.world.level.block.Block getBlock();

    @Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
    private void animewitchery$torchflowerLight(CallbackInfoReturnable<Integer> cir) {
        if (getBlock() == Blocks.TORCHFLOWER || getBlock() == Blocks.POTTED_TORCHFLOWER) {
            cir.setReturnValue(14);
        }
    }
}

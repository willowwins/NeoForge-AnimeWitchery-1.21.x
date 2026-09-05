package net.willowins.animewitchery.block.custom;

import net.minecraft.world.level.block.LiquidBlock;
import net.willowins.animewitchery.fluid.ModFluids;

public class AntifluidBlock extends LiquidBlock {

    public AntifluidBlock(Properties properties) {
        super(ModFluids.ANTIFLUID_SOURCE.get(), properties);
    }
}

package net.willowins.animewitchery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.block.entity.AntifountainBlockEntity;

@EventBusSubscriber(modid = AnimeWitchery.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class AntifountainTestCommands {

    private static final int HALF = 4;
    private static final int FLOOR_Y = 96;
    private static final int WALL_TOP_Y = 105;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("awtest")
                        .then(Commands.literal("room")
                                .executes(AntifountainTestCommands::buildRoomAndToggle))
                        .then(Commands.literal("report")
                                .executes(AntifountainTestCommands::report)));
    }

    private static int buildRoomAndToggle(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        BlockPos origin = new BlockPos(0, FLOOR_Y + 1, 0);

        for (int x = -HALF - 2; x <= HALF + 2; x++) {
            for (int z = -HALF - 2; z <= HALF + 2; z++) {
                for (int y = FLOOR_Y - 1; y <= WALL_TOP_Y + 1; y++) {
                    level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }

        for (int x = -HALF; x <= HALF; x++) {
            for (int z = -HALF; z <= HALF; z++) {
                level.setBlock(new BlockPos(x, FLOOR_Y, z), Blocks.STONE.defaultBlockState(), 3);
                level.setBlock(new BlockPos(x, WALL_TOP_Y, z), Blocks.STONE.defaultBlockState(), 3);
            }
        }
        for (int x = -HALF; x <= HALF; x++) {
            for (int z = -HALF; z <= HALF; z++) {
                if (Math.abs(x) == HALF || Math.abs(z) == HALF) {
                    for (int y = FLOOR_Y + 1; y <= WALL_TOP_Y - 1; y++) {
                        level.setBlock(new BlockPos(x, y, z), Blocks.STONE.defaultBlockState(), 3);
                    }
                }
            }
        }

        level.setBlock(origin, ModBlocks.ANTIFOUNTAIN.get().defaultBlockState(), 3);
        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof AntifountainBlockEntity fountain) {
            fountain.toggle();
            ctx.getSource().sendSuccess(() -> Component.literal("awtest: room built, fountain toggled at " + origin.toShortString()), true);
        } else {
            ctx.getSource().sendFailure(Component.literal("awtest: antifountain BE missing at " + origin.toShortString()));
            return 1;
        }
        return 0;
    }

    private static int report(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        BlockPos origin = new BlockPos(0, FLOOR_Y + 1, 0);
        int veils = 0;
        for (int x = origin.getX() - 11; x <= origin.getX() + 11; x++) {
            for (int z = origin.getZ() - 11; z <= origin.getZ() + 11; z++) {
                for (int y = origin.getY() - 1; y <= origin.getY() + 3; y++) {
                    if (level.getBlockState(new BlockPos(x, y, z)).is(ModBlocks.POCKET_VEIL.get())) {
                        veils++;
                    }
                }
            }
        }
        boolean active = false;
        if (level.getBlockEntity(origin) instanceof AntifountainBlockEntity af) {
            active = af.isActive();
        }
        final int count = veils;
        final boolean isActive = active;
        ctx.getSource().sendSuccess(() -> Component.literal("awtest: veils=" + count + " fountainActive=" + isActive + " (expect ~2027 in 9x9 room)"), true);
        return 0;
    }
}
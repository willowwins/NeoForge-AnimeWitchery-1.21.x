package net.willowins.animewitchery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.worldgen.PocketExpansionPlacer;

import java.util.List;

public record PocketPlacePayload(BlockPos pos, int index) implements CustomPacketPayload {

    public static final Type<PocketPlacePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "pocket_place"));

    public static final StreamCodec<ByteBuf, PocketPlacePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PocketPlacePayload::pos,
            ByteBufCodecs.VAR_INT, PocketPlacePayload::index,
            PocketPlacePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PocketPlacePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ServerLevel level = (ServerLevel) player.level();
            BlockState state = level.getBlockState(payload.pos());
            if (!state.is(ModBlocks.POCKET_EXPANSION_JIGSAW.get())) {
                player.displayClientMessage(Component.literal("The pocket jigsaw is gone."), true);
                return;
            }
            List<net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement> elements =
                    PocketExpansionPlacer.getSortedElements(level);
            if (payload.index() < 0 || payload.index() >= elements.size()) {
                player.displayClientMessage(Component.literal("That pocket piece is no longer available."), true);
                return;
            }
            if (!PocketExpansionPlacer.tryPlaceSelected(level, payload.pos(), elements.get(payload.index()))) {
                player.displayClientMessage(Component.literal("No room to place that piece here."), true);
                return;
            }
            Direction facing = state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING);
            level.setBlock(payload.pos(), level.getBlockState(payload.pos().relative(facing)), 3);
            player.displayClientMessage(Component.literal("Pocket piece placed."), true);
        });
    }
}
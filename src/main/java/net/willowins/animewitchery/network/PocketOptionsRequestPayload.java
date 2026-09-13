package net.willowins.animewitchery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;
import net.willowins.animewitchery.worldgen.PocketExpansionPlacer;

import java.util.List;
import java.util.Objects;

public record PocketOptionsRequestPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<PocketOptionsRequestPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "pocket_options_request"));

    public static final StreamCodec<ByteBuf, PocketOptionsRequestPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PocketOptionsRequestPayload::pos,
            PocketOptionsRequestPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PocketOptionsRequestPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) {
                return;
            }
            ServerLevel level = (ServerLevel) player.level();
            BlockState state = level.getBlockState(payload.pos());
            if (!state.is(ModBlocks.POCKET_EXPANSION_JIGSAW.get())) {
                return;
            }
            List<String> names = PocketExpansionPlacer.getSortedElements(level).stream()
                    .map(PocketExpansionPlacer::elementName)
                    .filter(Objects::nonNull)
                    .toList();
            PacketDistributor.sendToPlayer(player, new PocketOptionsPayload(payload.pos(), names));
        });
    }
}
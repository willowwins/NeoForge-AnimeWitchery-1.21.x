package net.willowins.animewitchery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.willowins.animewitchery.AnimeWitchery;

import java.util.List;

public record PocketOptionsPayload(BlockPos pos, List<String> names) implements CustomPacketPayload {

    public static final Type<PocketOptionsPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "pocket_options"));

    public static final StreamCodec<ByteBuf, PocketOptionsPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, PocketOptionsPayload::pos,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()), PocketOptionsPayload::names,
            PocketOptionsPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PocketOptionsPayload payload, IPayloadContext context) {
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            NeoForge.EVENT_BUS.post(new PocketOptionsEvent(payload.pos(), payload.names()));
        }
    }
}
package net.willowins.animewitchery.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.ManaData;
import net.willowins.animewitchery.ModAttachments;

public record ManaSyncPayload(int current, int max) implements CustomPacketPayload {
    public static final Type<ManaSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "mana_sync"));

    public static final StreamCodec<ByteBuf, ManaSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ManaSyncPayload::current,
            ByteBufCodecs.VAR_INT, ManaSyncPayload::max,
            ManaSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ManaSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.setData(ModAttachments.MANA, new ManaData(payload.current(), payload.max()));
            }
        });
    }
}

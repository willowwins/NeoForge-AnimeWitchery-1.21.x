package net.willowins.animewitchery;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, AnimeWitchery.MOD_ID);

    // Create the Codec for saving/loading ManaData
    private static final Codec<ManaData> MANA_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("current").forGetter(ManaData::current),
                    Codec.INT.fieldOf("max").forGetter(ManaData::max)
            ).apply(instance, ManaData::new)
    );

    // Register the attachment and configure it to persist through player death/dimension changes
    public static final Supplier<AttachmentType<ManaData>> MANA = ATTACHMENT_TYPES.register(
            "mana",
            () -> AttachmentType.builder(() -> new ManaData(100000, 100000)) // Default values: 100000/100000
                    .serialize(MANA_CODEC)
                    .copyOnDeath() // Keeps mana when the player dies and respawns
                    .build()
    );

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}

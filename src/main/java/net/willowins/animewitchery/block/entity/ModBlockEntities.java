package net.willowins.animewitchery.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.willowins.animewitchery.block.ModBlocks; // Adjust to your actual block registry path

public class ModBlockEntities {
    // 1. Create the Deferred Register for Block Entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, "animewitchery");

    // 2. Register your Starlight Pool Block Entity
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StarlighPoolBlockEntity>> STARLIGHT_POOL_BE =
            BLOCK_ENTITIES.register("starlight_pool", () ->
                    BlockEntityType.Builder.of(
                            StarlighPoolBlockEntity::new,
                            ModBlocks.STARLIGHT_POOL.get() // Links this BE to your block
                    ).build(null)
            );

    // Luna Crystal - a standing block entity so it can tick and harvest starlight
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<LunaCrystalBlockEntity>> LUNA_CRYSTAL_BE =
            BLOCK_ENTITIES.register("luna_crystal", () ->
                    BlockEntityType.Builder.of(
                            LunaCrystalBlockEntity::new,
                            ModBlocks.LUNA_CRYSTAL.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StarlightInfusionPedestalBlockEntity>> STARLIGHT_INFUSION_PEDESTAL_BE =
            BLOCK_ENTITIES.register("starlight_infusion_pedestal", () ->
                    BlockEntityType.Builder.of(
                            StarlightInfusionPedestalBlockEntity::new,
                            ModBlocks.STARLIGHT_INFUSION_PEDESTAL.get()
                    ).build(null)
            );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StarlightInfusionAltarBlockEntity>> STARLIGHT_INFUSION_ALTAR_BE =
            BLOCK_ENTITIES.register("starlight_infusion_altar", () ->
                    BlockEntityType.Builder.of(
                            StarlightInfusionAltarBlockEntity::new,
                            ModBlocks.STARLIGHT_INFUSION_ALTAR.get()
                    ).build(null)
            );


    // 3. Call this method in your main mod constructor
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

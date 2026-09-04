package net.willowins.animewitchery.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AnimeWitchery.MOD_ID);

public static  final Supplier<CreativeModeTab> ANIME_WITCHERY_BASIC = CREATIVE_MODE_TAB.register("anime_witchery_basic",
        () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.BLAZESACK.get()))
                .title(Component.translatable("itemtab.anime_witchery_basic"))
                .displayItems((ItemDisplayParameters, output) -> {
                    output.accept(new ItemStack(ModItems.BLAZESACK.get()));
                    output.accept(new ItemStack(ModItems.SILVERINGOT.get()));
                    output.accept(new ItemStack(ModItems.RAW_SILVER.get()));
                    output.accept(new ItemStack(ModItems.WAND.get()));
                    output.accept(new ItemStack(ModItems.STARDROP_WAND.get()));
                    output.accept(new ItemStack(ModItems.MANA_READER.get()));
                    output.accept(new ItemStack(ModItems.ALCHEMICAL_CATALYST.get()));
                    output.accept(new ItemStack(ModItems.RESONANT_CATALYST.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_BUCKET.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_BOTTLE.get()));
                    output.accept(new ItemStack(ModItems.DIAMOND_SHARD.get()));
                    output.accept(new ItemStack(ModItems.STAR_DROP.get()));
                    output.accept(new ItemStack(ModItems.STARLIGHT_DUST.get()));
                    output.accept(new ItemStack(ModItems.SILVER_SMITHING_TEMPLATE.get()));

                    output.accept(new ItemStack(ModBlocks.SILVER_BLOCK.get()));
                    output.accept(new ItemStack(ModBlocks.DEEPSLATE_SILVER_ORE.get()));
                    output.accept(new ItemStack(ModBlocks.SILVER_ORE.get()));
                    output.accept(new ItemStack(ModBlocks.LUNA_CRYSTAL.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_POOL.get()));
                    output.accept(new ItemStack(ModBlocks.STARDROP_LANTERN.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_INFUSION_PEDESTAL.get()));
                    output.accept(new ItemStack(ModBlocks.STARLIGHT_INFUSION_ALTAR.get()));
                    output.accept(new ItemStack(ModBlocks.ANCIENT_VARIABLE_SWITCH.get()));
                    output.accept(new ItemStack(ModBlocks.CRYSTALLIZED_STARLIGHT.get()));


                }).build());

public static  final Supplier<CreativeModeTab> ANIME_WITCHERY_COMBAT = CREATIVE_MODE_TAB.register("anime_witchery_combat",
            () -> CreativeModeTab.builder().icon(()-> new ItemStack(ModItems.SILVERINGOT.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "anime_witchery_basic"))
                    .title(Component.translatable("itemtab.anime_witchery_combat"))
                    .displayItems((ItemDisplayParameters, output) -> {

                        output.accept(new ItemStack(ModItems.SILVERINGOT.get()));

                    }).build());




    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}

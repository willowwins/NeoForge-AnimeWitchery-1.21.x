package net.willowins.animewitchery.client.model;

import net.minecraft.resources.ResourceLocation;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.block.entity.AlchemyTableBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class AlchemyTableModel extends DefaultedBlockGeoModel<AlchemyTableBlockEntity> {
    public AlchemyTableModel() {
        super(ResourceLocation.fromNamespaceAndPath(AnimeWitchery.MOD_ID, "alchemy_table"));
    }
}

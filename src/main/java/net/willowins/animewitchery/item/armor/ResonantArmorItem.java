package net.willowins.animewitchery.item.armor;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.willowins.animewitchery.item.ModItems;
import net.willowins.animewitchery.item.armor.client.ResonantArmorRenderer;
import net.willowins.animewitchery.item.custom.ModArmorItem;
import net.willowins.animewitchery.mana.PlayerMana;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class ResonantArmorItem extends ModArmorItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public ResonantArmorItem(Holder<ArmorMaterial> armorMaterial, Type type, Properties properties) {
        super(armorMaterial, type, properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private @Nullable ResonantArmorRenderer renderer;

            @Override
            public <T extends net.minecraft.world.entity.LivingEntity> net.minecraft.client.model.HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T entity,
                    ItemStack stack,
                    @Nullable EquipmentSlot slot,
                    @Nullable net.minecraft.client.model.HumanoidModel<T> original) {
                if (renderer == null) {
                    renderer = new ResonantArmorRenderer();
                }
                if (slot != null && original != null && entity != null) {
                    renderer.prepForRender(entity, stack, slot, original);
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 20, state -> {
            Entity entity = state.getData(DataTickets.ENTITY);
            if (entity == null) {
                return PlayState.STOP;
            }
            if (entity instanceof ArmorStand) {
                state.getController().setAnimation(DefaultAnimations.IDLE);
                return PlayState.CONTINUE;
            }
            if (entity instanceof Player player && hasFullSet(player)) {
                state.getController().setAnimation(DefaultAnimations.IDLE);
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }));
    }

    private boolean hasFullSet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.RESONANT_HELMET.get())
                && player.getItemBySlot(EquipmentSlot.CHEST).is(ModItems.RESONANT_CHESTPLATE.get())
                && player.getItemBySlot(EquipmentSlot.LEGS).is(ModItems.RESONANT_LEGGINGS.get())
                && player.getItemBySlot(EquipmentSlot.FEET).is(ModItems.RESONANT_BOOTS.get());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide() && entity instanceof Player player) {
            if (hasFullSet(player) && shouldSpawnAura(player)) {
                spawnResonantAura(level, player);
            }
        }
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    @OnlyIn(Dist.CLIENT)
    private boolean shouldSpawnAura(Player player) {
        return PlayerMana.getMana(player) >= PlayerMana.getMaxMana(player);
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnResonantAura(Level level, Player player) {
        double x = player.getX() + (level.random.nextDouble() - 0.5) * 1.5;
        double y = player.getY() + level.random.nextDouble() * 2.0;
        double z = player.getZ() + (level.random.nextDouble() - 0.5) * 1.5;
        level.addParticle(ParticleTypes.WITCH, x, y, z, 0, 0.08 + level.random.nextDouble() * 0.06, 0);
        if (level.random.nextInt(15) == 0) {
            level.addParticle(ParticleTypes.END_ROD, x, y, z, 0, 0.05, 0);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

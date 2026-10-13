package net.willowins.animewitchery.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.willowins.animewitchery.item.ModItems;

public class SummonerStaffItem extends Item {
    private static final int COST = 20;
    public SummonerStaffItem(Properties properties) { super(properties); }

    @Override
    public InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level level, Player player, InteractionHand hand) {
        ItemStack staff = player.getItemInHand(hand);
        if (level.isClientSide) return InteractionResultHolder.sidedSuccess(staff, true);
        ItemStack jar = player.getOffhandItem();
        if (!(jar.getItem() instanceof SoulJarItem)) {
            player.displayClientMessage(Component.literal("Hold a Soul Jar in your offhand to summon."), true);
            return InteractionResultHolder.fail(staff);
        }
        SimpleContainer inventory = new SimpleContainer(27);
        SoulJarItem.loadInventory(player.registryAccess(), jar, inventory);
        if (player.isShiftKeyDown()) {
            int recalled = 0;
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(20), e -> e != player)) {
                if (isOwnedBy(entity, player) && SoulJarItem.captureEntity(player, entity, jar)) {
                    entity.discard(); recalled++;
                    player.giveExperiencePoints(10);
                }
            }
            player.displayClientMessage(Component.literal("Recalled " + recalled + " entities."), true);
            return InteractionResultHolder.success(staff);
        }
        if (!player.getAbilities().instabuild && player.totalExperience < COST) {
            player.displayClientMessage(Component.literal("Not enough XP to summon. Cost: " + COST + " points."), true);
            return InteractionResultHolder.fail(staff);
        }
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack soul = inventory.getItem(i);
            if (soul.is(ModItems.SOUL.get()) && soul.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains("EntityData")) {
                if (spawnSoul((ServerLevel) level, player, soul)) {
                    inventory.setItem(i, ItemStack.EMPTY);
                    SoulJarItem.saveInventory(player.registryAccess(), jar, inventory);
                    if (!player.getAbilities().instabuild) player.giveExperiencePoints(-COST);
                }
                return InteractionResultHolder.success(staff);
            }
        }
        player.displayClientMessage(Component.literal("The Soul Jar has no souls."), true);
        return InteractionResultHolder.success(staff);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide) return InteractionResult.SUCCESS;
        ItemStack jar = player.getOffhandItem();
        if (!(jar.getItem() instanceof SoulJarItem)) {
            player.displayClientMessage(Component.literal("Hold a Soul Jar in your offhand to recall."), true);
            return InteractionResult.PASS;
        }
        if (!isOwnedBy(target, player)) return InteractionResult.PASS;
        if (!SoulJarItem.captureEntity(player, target, jar)) {
            player.displayClientMessage(Component.literal("Soul Jar is full!"), true);
            return InteractionResult.FAIL;
        }
        target.discard();
        player.giveExperiencePoints(10);
        player.displayClientMessage(Component.literal("Recalled " + target.getName().getString()), true);
        return InteractionResult.SUCCESS;
    }

    private static boolean isOwnedBy(LivingEntity entity, Player player) {
        if (entity instanceof TamableAnimal tameable && player.getUUID().equals(tameable.getOwnerUUID())) return true;
        CompoundTag tag = new CompoundTag();
        entity.saveWithoutId(tag);
        return tag.hasUUID("SummonerOwner") && player.getUUID().equals(tag.getUUID("SummonerOwner"));
    }

    private static boolean spawnSoul(ServerLevel level, Player owner, ItemStack soul) {
        CompoundTag soulTag = soul.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag entityTag = soulTag.getCompound("EntityData");
        var type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getOptional(
                net.minecraft.resources.ResourceLocation.tryParse(entityTag.getString("id"))).orElse(null);
        if (type == null) return false;
        Entity entity = type.create(level);
        if (!(entity instanceof LivingEntity living)) return false;
        living.load(entityTag);
        Vec3 start = owner.getEyePosition();
        Vec3 direction = owner.getLookAngle();
        Vec3 end = start.add(direction.scale(2.5));
        var hit = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner));
        Vec3 pos = hit.getType() == HitResult.Type.BLOCK ? hit.getLocation().subtract(direction.scale(0.5)) : end;
        living.moveTo(pos.x, pos.y, pos.z, owner.getYRot(), 0);
        if (living instanceof TamableAnimal tameable) tameable.tame(owner);
        if (living instanceof AbstractHorse horse) { horse.setOwnerUUID(owner.getUUID()); horse.setTamed(true); }
        if (living instanceof Mob mob) mob.setTarget(null);
        return level.addFreshEntity(living);
    }
}

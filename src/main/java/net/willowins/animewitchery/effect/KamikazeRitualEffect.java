package net.willowins.animewitchery.effect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.willowins.animewitchery.AnimeWitchery;
import net.willowins.animewitchery.util.ModExplosionManager;

public class KamikazeRitualEffect extends MobEffect {

    private static final Map<UUID, DeathSequence> activeDeathSequences = new HashMap<>();

    public static boolean isRitualActive = false;

    public KamikazeRitualEffect() {
        super(MobEffectCategory.NEUTRAL, 0x8B0000);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity instanceof Player player) {
            if (!entity.level().isClientSide) {
                Level world = entity.level();
                BlockPos pos = entity.blockPosition();

                if (activeDeathSequences.containsKey(player.getUUID())) {
                    DeathSequence sequence = activeDeathSequences.get(player.getUUID());
                    sequence.update(world, player);
                    return true;
                }

                for (int i = 0; i < 3; i++) {
                    double angle = (world.getGameTime() * 0.1) + (i * Math.PI * 2.0 / 3.0);
                    double radius = 2.0 + (amplifier * 0.5);
                    double x = pos.getX() + 0.5 + Math.cos(angle) * radius;
                    double z = pos.getZ() + 0.5 + Math.sin(angle) * radius;

                    world.addParticle(ParticleTypes.END_ROD, x, pos.getY() + 0.1, z, 0, 0.1, 0);
                }

                if (world.getGameTime() % 20 == 0) {
                    world.addParticle(ParticleTypes.SMOKE,
                            pos.getX() + 0.5 + (world.random.nextDouble() - 0.5) * 3,
                            pos.getY() + 0.5,
                            pos.getZ() + 0.5 + (world.random.nextDouble() - 0.5) * 3,
                            0, 0.1, 0);
                }
            } else {
                if (activeDeathSequences.containsKey(player.getUUID())) {
                    spawnRitualParticles(player);
                } else {
                    spawnAmbientParticles(player, amplifier);
                }
            }
        }
        return true;
    }

    public static boolean onPlayerDeath(Player player, DamageSource source) {
        if (player instanceof ServerPlayer serverPlayer && player.hasEffect(ModEffects.KAMIKAZE_RITUAL)) {
            if (!activeDeathSequences.containsKey(player.getUUID())) {
                DeathSequence sequence = new DeathSequence(player);
                activeDeathSequences.put(player.getUUID(), sequence);

                isRitualActive = true;

                player.setHealth(player.getMaxHealth());
                player.setInvulnerable(true);

                player.setDeltaMovement(0, 0, 0);
                player.fallDistance = 0;
                player.setShiftKeyDown(false);
                player.setSprinting(false);
                player.setSwimming(false);

                serverPlayer.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal("§4§l[FORBIDDEN MAGIC] §c" + player.getName().getString() + " has triggered the Kamikaze Ritual!"),
                        false);

                player.sendSystemMessage(Component.literal("§4§l[FORBIDDEN MAGIC] §cYour soul is forfeit!"));

                player.level().playSound(null, player.blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
                        player.getSoundSource(), 4.0f, 1f);

                return true;
            }
        }
        return false;
    }

    private void spawnAmbientParticles(Player player, int amplifier) {
        BlockPos pos = player.blockPosition();
        double time = player.level().getGameTime() * 0.1;

        for (int i = 0; i < 6; i++) {
            double angle = time + (i * Math.PI * 2.0 / 6.0);
            double radius = 2.5 + (amplifier * 0.3);
            double x = pos.getX() + 0.5 + Math.cos(angle) * radius;
            double z = pos.getZ() + 0.5 + Math.sin(angle) * radius;

            player.level().addParticle(ParticleTypes.END_ROD, x, pos.getY() + 0.2, z, 0, 0.05, 0);
        }
    }

    private void spawnRitualParticles(Player player) {
        BlockPos pos = player.blockPosition();
        double time = player.level().getGameTime() * 0.2;

        for (int i = 0; i < 12; i++) {
            double angle = time + (i * Math.PI * 2.0 / 12.0);
            double radius = 4.0 + Math.sin(time * 2) * 2.0;
            double x = pos.getX() + 0.5 + Math.cos(angle) * radius;
            double z = pos.getZ() + 0.5 + Math.sin(angle) * radius;

            player.level().addParticle(ParticleTypes.FIREWORK, x, pos.getY() + 0.1, z, 0, 0.1, 0);
        }

        if (player.level().getGameTime() % 15 == 0) {
            for (int i = 0; i < 8; i++) {
                double angle = (i * Math.PI * 2.0 / 8.0);
                double x = pos.getX() + 0.5 + Math.cos(angle) * 15.0;
                double z = pos.getZ() + 0.5 + Math.sin(angle) * 15.0;

                player.level().addParticle(ParticleTypes.END_ROD, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        (x - pos.getX() - 0.5) * 0.1, 0, (z - pos.getZ() - 0.5) * 0.1);
            }
        }
    }

    private static class DeathSequence {
        private final Player player;
        private final BlockPos deathPos;
        private int ticks = 0;
        private final int TOTAL_TICKS = 200;
        private float lockedYaw;
        private boolean yawPitchCaptured;
        private float lockedPitch;

        public DeathSequence(Player player) {
            this.player = player;
            this.deathPos = player.blockPosition();
        }

        public void update(Level world, Player player) {
            ticks++;

            if (!yawPitchCaptured) {
                lockedYaw = player.getYRot();
                lockedPitch = player.getXRot();
                yawPitchCaptured = true;
                player.stopRiding();

                player.setPos(deathPos.getX() + 0.5, deathPos.getY() + 0.5, deathPos.getZ() + 0.5);
                player.setDeltaMovement(0, 0, 0);
                player.setSpeed(0.0f);
                player.fallDistance = 0;
            }

            if (player instanceof ServerPlayer sp) {
                sp.connection.teleport(
                        deathPos.getX() + 0.5,
                        deathPos.getY() + 0.5,
                        deathPos.getZ() + 0.5,
                        lockedYaw, lockedPitch);
            }

            player.setHealth(player.getMaxHealth());
            player.setInvulnerable(true);

            player.setShiftKeyDown(false);
            player.setSprinting(false);
            player.setSwimming(false);

            double progress = (double) ticks / TOTAL_TICKS;

            for (int i = 0; i < 8; i++) {
                double angle = (world.getGameTime() * 0.2) + (i * Math.PI * 2.0 / 8.0);
                double radius = 3.0 + (progress * 5.0);
                double x = deathPos.getX() + 0.5 + Math.cos(angle) * radius;
                double z = deathPos.getZ() + 0.5 + Math.sin(angle) * radius;

                world.addParticle(ParticleTypes.END_ROD, x, deathPos.getY() + 0.1, z, 0, 0.1, 0);
            }

            if (ticks % 10 == 0) {
                for (int i = 0; i < 4; i++) {
                    double angle = (i * Math.PI * 2.0 / 4.0);
                    double x = deathPos.getX() + 0.5 + Math.cos(angle) * 10.0;
                    double z = deathPos.getZ() + 0.5 + Math.sin(angle) * 10.0;

                    world.addParticle(ParticleTypes.FIREWORK, x, deathPos.getY() + 0.5, z, 0, 0.5, 0);
                }
            }

            if (ticks == 200) {
                player.sendSystemMessage(Component.literal("§4§l[FORBIDDEN MAGIC] §cRITUAL COMPLETE!"));
                player.level().playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(),
                        player.getSoundSource(), 4.0f, 1f);
            }

            if (ticks >= TOTAL_TICKS) {
                executeRitual(world, player);
                flingDebris(world, deathPos, 80, 90);
                activeDeathSequences.remove(player.getUUID());
                isRitualActive = false;
            }
        }

        private void executeRitual(Level world, Player player) {
            HomemadeExplosion explosion = new HomemadeExplosion(world, deathPos, 200, player);
            explosion.start();

            world.playSound(null, deathPos, SoundEvents.GENERIC_EXPLODE.value(),
                    player.getSoundSource(), 4.0f, 0.8f);

            try {
                String banCommand = "ban " + player.getName().getString() + " [Kamikaze Ritual: Forbidden Magic]";
                world.getServer().getCommands().performPrefixedCommand(
                        world.getServer().createCommandSourceStack(), banCommand);

                world.getServer().getPlayerList().broadcastSystemMessage(
                        Component.literal("§4§l[FORBIDDEN MAGIC] §c" + player.getName().getString() + " has been erased from this world!"),
                        false);
            } catch (Exception e) {
                AnimeWitchery.LOGGER.error("Failed to ban player after Kamikaze Ritual: " + e.getMessage());
            }
        }

        private void flingDebris(Level world, BlockPos c, int samples, double r) {
            var rnd = world.random;
            for (int i = 0; i < samples; i++) {
                double a = rnd.nextDouble() * Math.PI * 2;
                int x = c.getX() + (int) Math.round(Math.cos(a) * r);
                int z = c.getZ() + (int) Math.round(Math.sin(a) * r);
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
                BlockPos pos = new BlockPos(x, y, z);
                var st = world.getBlockState(pos);
                if (st.isAir() || st.getDestroySpeed(world, pos) < 0) continue;

                world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                var fbe = FallingBlockEntity.fall(world, pos, st);
                if (fbe != null) {
                    double spd = 0.6 + rnd.nextDouble() * 0.6;
                    fbe.setDeltaMovement(
                            Math.cos(a) * spd,
                            0.55 + rnd.nextDouble() * 0.55,
                            Math.sin(a) * spd);
                }
            }
        }

        private static class HomemadeExplosion implements ModExplosionManager.TickableExplosion {
            private final Level world;
            private final BlockPos center;
            private final double maxRadius;
            private final Player player;

            private int tick = 0;
            private final int EXPANSION_TICKS = 200;

            private double currentRadius = 0.0;
            private double lastRadius = 0.0;
            private double currentRadiusSq = 0.0;
            private double lastRadiusSq = 0.0;

            private static final int BASE_BLOCKS_PER_TICK = 10000;
            private static final int MAX_BLOCKS_PER_TICK = 40000;
            private static final int MAX_PARTICLES_PER_TICK = 400;
            private static final int DILATION_BUDGET_PER_TICK = 10000;
            private int dilationBudget = DILATION_BUDGET_PER_TICK;
            private int dynamicStride = 1;
            private double shellMargin = 1.0;
            private static final int ENTITY_BUDGET_PER_TICK = 512;

            private int scanRadius = 0;
            private int sx = Integer.MIN_VALUE, sy = Integer.MIN_VALUE, sz = Integer.MIN_VALUE;
            private int minY, maxY;

            public HomemadeExplosion(Level world, BlockPos center, double maxRadius, Player player) {
                this.world = world;
                this.center = center.immutable();
                this.maxRadius = maxRadius;
                this.player = player;
                initYBounds();
            }

            private int strideFor(double r) {
                return 1;
            }

            private double marginFor(int stride) {
                double baseMargin = Math.max(2.0, 1.9 * stride);
                double radiusScale = Math.max(1.0, maxRadius / 100.0);
                return baseMargin * radiusScale;
            }

            private static double hash01(int x, int y, int z, int salt) {
                long h = 1469598103934665603L;
                h ^= (long) x * 0x8da6b343L;
                h *= 0xff51afd7ed558ccdL;
                h ^= (long) y * 0xd8163841L;
                h *= 0xc4ceb9fe1a85ec53L;
                h ^= (long) z * 0xcb1ab31fL;
                h ^= (long) salt * 0x9e3779b97f4a7c15L;
                h ^= (h >>> 33);
                h *= 0xff51afd7ed558ccdL;
                h ^= (h >>> 33);
                h *= 0xc4ceb9fe1a85ec53L;
                h ^= (h >>> 33);
                return (h >>> 11) * (1.0 / (1L << 53));
            }

            private static double easeOutCubic(double t) {
                double u = 1.0 - Math.max(0.0, Math.min(1.0, t));
                return 1.0 - u * u * u;
            }

            private void maybeDilateAt(BlockPos pos, double edgeRadiusSq) {
                if (dilationBudget <= 0) return;
                final BlockPos[] nbs = new BlockPos[] {
                        pos.above(), pos.below(), pos.north(), pos.south(), pos.east(), pos.west()
                };
                for (BlockPos n : nbs) {
                    if (dilationBudget <= 0) break;
                    if (!world.isLoaded(n)) continue;
                    var st = world.getBlockState(n);
                    if (st.isAir() || st.getDestroySpeed(world, n) < 0) continue;
                    int dx = n.getX() - center.getX();
                    int dy = n.getY() - center.getY();
                    int dz = n.getZ() - center.getZ();
                    double d2 = (double) dx * dx + (double) dy * dy + (double) dz * dz;
                    if (d2 <= edgeRadiusSq) {
                        world.setBlock(n, Blocks.AIR.defaultBlockState(), 3);
                        dilationBudget--;
                    }
                }
            }

            private void initYBounds() {
                minY = Math.max(world.getMinBuildHeight(), center.getY() - (int) Math.ceil(maxRadius));
                maxY = Math.min(world.getMaxBuildHeight() - 1, center.getY() + (int) Math.ceil(maxRadius));
            }

            public void start() {
                ModExplosionManager.add(this);
            }

            @Override
            public boolean tickOnce(MinecraftServer server) {
                if (world.isClientSide) return true;

                tick++;
                double progress = (double) tick / EXPANSION_TICKS;
                if (progress > 1.0) progress = 1.0;

                lastRadius = currentRadius;
                lastRadiusSq = lastRadius * lastRadius;

                currentRadius = progress * maxRadius;
                currentRadiusSq = currentRadius * currentRadius;

                this.dynamicStride = strideFor(currentRadius);
                this.shellMargin = marginFor(this.dynamicStride);

                this.dilationBudget = DILATION_BUDGET_PER_TICK;

                scanRadius = (int) Math.ceil(currentRadius + shellMargin);

                if (tick % 20 == 0) {
                    AnimeWitchery.LOGGER.info("Explosion tick {}/{} radius {}/{} ({:.1f}%)",
                            tick, EXPANSION_TICKS, currentRadius, maxRadius, progress * 100.0);
                }

                int changed = destroyIncrementalShell();
                killEntitiesInShell();
                spawnThrottledParticles();

                if (changed > 0) {
                    world.playSound(null, center, SoundEvents.GLASS_BREAK,
                            player.getSoundSource(), 0.25f, 1.0f + (float) (progress * 0.5f));
                }

                boolean finishedRadius = tick >= EXPANSION_TICKS;
                boolean finishedScan = (sx == Integer.MIN_VALUE);
                return finishedRadius && finishedScan;
            }

            private void killEntitiesInShell() {
                if (world.isClientSide) return;

                final double shellInnerR = Math.max(0.0, lastRadius - 0.75);
                final double shellOuterR = currentRadius + 1.25;
                final double innerSq = shellInnerR * shellInnerR;
                final double outerSq = shellOuterR * shellOuterR;

                final int r = (int) Math.ceil(shellOuterR + 1.0);
                final AABB aabb = new AABB(
                        center.getX() - r, center.getY() - r, center.getZ() - r,
                        center.getX() + r + 1, center.getY() + r + 1, center.getZ() + r + 1);

                final Vec3 centerVec = new Vec3(
                        center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);

                var hits = world.getEntities((Entity) null,
                        aabb,
                        e -> {
                            if (!e.isAlive()) return false;
                            if (e.getUUID().equals(player.getUUID())) return false;
                            if (e instanceof ServerPlayer sp &&
                                    (sp.isSpectator() || sp.isCreative())) return false;

                            double d2 = e.position().distanceToSqr(centerVec);
                            return d2 > innerSq && d2 <= outerSq;
                        });

                int budget = ENTITY_BUDGET_PER_TICK;

                for (var e : hits) {
                    if (budget-- <= 0) break;

                    double d2 = e.position().distanceToSqr(centerVec);
                    double d = Math.sqrt(d2);

                    double rim = (d - shellInnerR) / Math.max(1e-6, (shellOuterR - shellInnerR));
                    rim = Math.max(0.0, Math.min(1.0, rim));

                    double centerFrac = 1.0 - Math.min(1.0, d / (maxRadius + 1e-6));

                    var dir = e.position().subtract(centerVec).normalize();
                    double kb = 0.7 + 1.0 * rim;
                    double up = 0.12 + 0.28 * rim;

                    if (!(e instanceof LivingEntity le)) {
                        if (e.isVehicle()) e.ejectPassengers();
                        e.discard();
                        continue;
                    }

                    final float MIN_DMG = 40f;
                    final float MAX_DMG = 500f;
                    final double GAMMA = 1.7;
                    float dmg = (float) (MIN_DMG + (MAX_DMG - MIN_DMG) * Math.pow(centerFrac, GAMMA));

                    var src = world.damageSources().explosion(player, player);
                    le.hurt(src, dmg);

                    if (le.isAlive()) {
                        le.hurt(world.damageSources().fellOutOfWorld(), Float.MAX_VALUE);
                    }

                    if (le.isAlive()) le.setRemainingFireTicks(80);

                    e.setDeltaMovement(dir.x * kb, up, dir.z * kb);

                    if (e.isVehicle()) e.ejectPassengers();
                }
            }

            private int destroyIncrementalShell() {
                int budget = (int) Math.min(MAX_BLOCKS_PER_TICK,
                        BASE_BLOCKS_PER_TICK * Math.max(1.0, maxRadius / 100.0));
                int destroyed = 0;

                double lastSqWithMargin = Math.max(0.0, (lastRadius - shellMargin) * (lastRadius - shellMargin));
                double currentSqWithMargin = (currentRadius + shellMargin) * (currentRadius + shellMargin);

                if (sx == Integer.MIN_VALUE) {
                    sx = -scanRadius;
                    sy = minY - center.getY();
                    sz = -scanRadius;
                }

                for (int x = sx; x <= scanRadius; x += dynamicStride) {
                    for (int yOff = sy; yOff <= (maxY - center.getY()); yOff += dynamicStride) {
                        int y = center.getY() + yOff;
                        for (int z = sz; z <= scanRadius; z += dynamicStride) {
                            if (budget <= 0) {
                                sx = x;
                                sy = yOff;
                                sz = z;
                                return destroyed;
                            }

                            int dx = x, dz = z;
                            double distSq = (double) dx * dx + (double) yOff * yOff + (double) dz * dz;

                            if (distSq <= lastSqWithMargin || distSq > currentSqWithMargin) continue;

                            BlockPos pos = center.offset(dx, yOff, dz);
                            var state = world.getBlockState(pos);
                            if (state.isAir() || state.getDestroySpeed(world, pos) < 0) continue;

                            double jitterAmp = Math.max(0.75, 0.35 * dynamicStride);
                            double jitter = (hash01(center.getX() + dx, y, center.getZ() + dz, 1337) * 2.0 - 1.0) * jitterAmp;
                            double rEdge = currentRadius + jitter;

                            double strongBand = Math.max(1.0, shellMargin * 0.60);
                            double taperBand = Math.max(2.0, shellMargin * 1.75);

                            if (maxRadius > 100.0) {
                                strongBand *= Math.max(1.0, maxRadius / 100.0);
                                taperBand *= Math.max(1.0, maxRadius / 100.0);
                            }

                            double rStrongInner = Math.max(0.0, rEdge - strongBand);
                            double rTaperInner = Math.max(0.0, rEdge - strongBand - taperBand);

                            double rEdgeSq = rEdge * rEdge;
                            double rStrongInnerSq = rStrongInner * rStrongInner;
                            double rTaperInnerSq = rTaperInner * rTaperInner;

                            if (distSq <= rEdgeSq) {
                                if (distSq > rStrongInnerSq) {
                                    double p = 0.98;
                                    if (hash01(pos.getX(), pos.getY(), pos.getZ(), 4242) <= p) {
                                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                                        destroyed++;
                                        budget--;
                                        maybeDilateAt(pos, rEdgeSq);
                                    }
                                    continue;
                                }

                                if (distSq > rTaperInnerSq) {
                                    double dist = Math.sqrt(distSq);
                                    double t = (rStrongInner <= rTaperInner) ? 1.0
                                            : (dist - rTaperInner) / (rStrongInner - rTaperInner);
                                    t = Math.max(0.0, Math.min(1.0, t));
                                    double bias = easeOutCubic(t);
                                    double p = 0.30 + 0.65 * bias;
                                    p *= (1.0 - (currentRadius / maxRadius) * 0.15);

                                    if (hash01(pos.getX(), pos.getY(), pos.getZ(), 7777) <= p) {
                                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                                        destroyed++;
                                        budget--;
                                    }
                                    continue;
                                }
                            }
                        }
                        sz = -scanRadius;
                    }
                    sy = minY - center.getY();
                }

                sx = sy = sz = Integer.MIN_VALUE;
                return destroyed;
            }

            private void spawnThrottledParticles() {
                int particleBudget = Math.min((int) (currentRadius * 6), MAX_PARTICLES_PER_TICK);

                for (int i = 0; i < particleBudget; i++) {
                    double phi = world.random.nextDouble() * Math.PI * 2.0;
                    double theta = world.random.nextDouble() * Math.PI;

                    double r = currentRadius;
                    double x = center.getX() + 0.5 + r * Math.sin(theta) * Math.cos(phi);
                    double y = center.getY() + 0.5 + r * Math.cos(theta);
                    double z = center.getZ() + 0.5 + r * Math.sin(theta) * Math.sin(phi);

                    world.addParticle(
                            (currentRadius < maxRadius * 0.4) ? ParticleTypes.FLAME
                                    : (currentRadius < maxRadius * 0.8) ? ParticleTypes.SMOKE
                                            : ParticleTypes.EXPLOSION,
                            x, y, z, 0, 0.05, 0);
                }
            }
        }
    }
}
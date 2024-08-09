package tfar.mineanything.world;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.mixin.ExplosionAccess;
import tfar.mineanything.platform.Services;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class WeakExplosion extends Explosion {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR = new ExplosionDamageCalculator();
    private static final int MAX_DROPS_PER_COMBINED_STACK = 16;

    public WeakExplosion(Level pLevel, @javax.annotation.Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, List<BlockPos> pPositions) {
        this(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY, pPositions);
    }

    public WeakExplosion(Level pLevel, @javax.annotation.Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction, List<BlockPos> pPositions) {
        this(pLevel, pSource, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
        this.getToBlow().addAll(pPositions);
    }

    public WeakExplosion(Level pLevel, @javax.annotation.Nullable Entity pSource, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction) {
        this(pLevel, pSource, null, null, pToBlowX, pToBlowY, pToBlowZ, pRadius, pFire, pBlockInteraction);
    }

    public WeakExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction) {
        super(pLevel, pSource,pDamageSource,pDamageCalculator,pToBlowX, pToBlowY, pToBlowZ,pRadius,pFire,pBlockInteraction);
    }

    private ExplosionDamageCalculator makeDamageCalculator(@javax.annotation.Nullable Entity pEntity) {
        return pEntity == null ? EXPLOSION_DAMAGE_CALCULATOR : new EntityBasedExplosionDamageCalculator(pEntity);
    }

    public static float getSeenPercent(Vec3 pExplosionVector, Entity pEntity) {
        AABB aabb = pEntity.getBoundingBox();
        double d0 = 1.0D / ((aabb.maxX - aabb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((aabb.maxY - aabb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((aabb.maxZ - aabb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
        if (!(d0 < 0.0D) && !(d1 < 0.0D) && !(d2 < 0.0D)) {
            int i = 0;
            int j = 0;

            for(double d5 = 0.0D; d5 <= 1.0D; d5 += d0) {
                for(double d6 = 0.0D; d6 <= 1.0D; d6 += d1) {
                    for(double d7 = 0.0D; d7 <= 1.0D; d7 += d2) {
                        double d8 = Mth.lerp(d5, aabb.minX, aabb.maxX);
                        double d9 = Mth.lerp(d6, aabb.minY, aabb.maxY);
                        double d10 = Mth.lerp(d7, aabb.minZ, aabb.maxZ);
                        Vec3 vec3 = new Vec3(d8 + d3, d9, d10 + d4);
                        if (pEntity.level().clip(new ClipContext(vec3, pExplosionVector, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, pEntity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void explode() {
        Level level = $getLevel();
        double x = $getX();
        double y = $getY();
        double z = $getZ();
        float radius = $getRadius();
        ExplosionDamageCalculator damageCalculator = $getDamageCalculator();
        level.gameEvent(getDirectSourceEntity(), GameEvent.EXPLODE, new Vec3(x, y, z));
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = j / 15.0F * 2.0F - 1.0F;
                        double d1 = k / 15.0F * 2.0F - 1.0F;
                        double d2 = l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = radius * (0.7F + level.random.nextFloat() * 0.6F);
                        double d4 = x;
                        double d6 = y;
                        double d8 = z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = level.getBlockState(blockpos);
                            FluidState fluidstate = level.getFluidState(blockpos);
                            if (!level.isInWorldBounds(blockpos)) {
                                break;
                            }

                            Optional<Float> optional = damageCalculator.getBlockExplosionResistance(this, level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && damageCalculator.shouldBlockExplode(this, level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }

                            d4 += d0 * (double)0.3F;
                            d6 += d1 * (double)0.3F;
                            d8 += d2 * (double)0.3F;
                        }
                    }
                }
            }
        }

        getToBlow().addAll(set);
        float f2 = radius * 2.0F;
        int k1 = Mth.floor(x - f2 - 1.0D);
        int l1 = Mth.floor(x + f2 + 1.0D);
        int i2 = Mth.floor(y - f2 - 1.0D);
        int i1 = Mth.floor(y + f2 + 1.0D);
        int j2 = Mth.floor(z - f2 - 1.0D);
        int j1 = Mth.floor(z + f2 + 1.0D);
        List<Entity> list = level.getEntities(getDirectSourceEntity(), new AABB(k1, i2, j2, l1, i1, j1));
        Services.PLATFORM.onExplosionDetonate(level, this, list, f2);
        Vec3 vec3 = new Vec3(x, y, z);

        for (Entity entity : list) {
            if (!entity.ignoreExplosion()) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / f2;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - y;
                    double d9 = entity.getZ() - z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = getSeenPercent(vec3, entity);
                        double d10 = (1.0D - d12) * d14;
                        float damage =  (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * f2 + 1.0D));
                        dealExplosionDamage(entity,damage);
                        double d11;
                        if (entity instanceof LivingEntity livingentity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(livingentity, d10);
                        } else {
                            d11 = d10;
                        }

                        d5 *= d11;
                        d7 *= d11;
                        d9 *= d11;
                        Vec3 vec31 = new Vec3(d5, d7, d9);
                        entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
                        if (entity instanceof Player player) {
                            if (!player.isSpectator() && (!player.isCreative() || !player.getAbilities().flying)) {
                                getHitPlayers().put(player, vec31);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void dealExplosionDamage(Entity entity,float amount) {
        entity.hurt(this.getDamageSource(),amount / 4);
    }

    ExplosionAccess getAccess() {
        return (ExplosionAccess) this;
    }

    Level $getLevel() {
        return getAccess().getLevel();
    }

    double $getX() {
        return getAccess().getX();
    }

    double $getY() {
        return getAccess().getY();
    }

    double $getZ() {
        return getAccess().getZ();
    }

    BlockInteraction $getBlockInteraction() {
        return getAccess().getBlockInteraction();
    }

    float $getRadius() {
        return getAccess().getRadius();
    }
    boolean $isFire() {
        return getAccess().getFire();
    }
    RandomSource $getRandom() {
        return getAccess().getRandom();
    }
    ExplosionDamageCalculator $getDamageCalculator() {
        return getAccess().getDamageCalculator();
    }

}

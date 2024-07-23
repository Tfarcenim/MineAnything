package tfar.mineanything;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.mixin.ExplosionAccess;
import tfar.mineanything.platform.Services;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LavaExplosion extends Explosion {

    public LavaExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction) {
        super(pLevel, pSource,pDamageSource,pDamageCalculator,pToBlowX, pToBlowY, pToBlowZ,pRadius,pFire,pBlockInteraction);
    }

  /*  @Override
    public void explode() {
        Level level = $getLevel();
        float radius = $getRadius();
        Entity source = getDirectSourceEntity();
        double x = $getX();
        double y = $getX();
        double z = $getX();
        List<BlockPos> toBlow = getToBlow();
        ExplosionDamageCalculator damageCalculator = $getDamageCalculator();
        level.gameEvent(source, GameEvent.EXPLODE, new Vec3(x, y, z));
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for(int j = 0; j < 16; ++j) {
            for(int k = 0; k < 16; ++k) {
                for(int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        double d0 = (float)j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float)k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float)l / 15.0F * 2.0F - 1.0F;
                        double dist = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 /= dist;
                        d1 /= dist;
                        d2 /= dist;
                        float f = radius * (0.7F + level.random.nextFloat() * 0.6F);
                        double d4 = x;
                        double d6 = y;
                        double d8 = z;

                        for(float f1 = 0.3F; f > 0.0F; f -= 0.225F) {
                            BlockPos blockpos = BlockPos.containing(d4, d6, d8);
                            BlockState blockstate = level.getBlockState(blockpos);
                            FluidState fluidstate = level.getFluidState(blockpos);
                            if (!level.isInWorldBounds(blockpos)) {
                                break;
                            }

                            Optional<Float> optional = damageCalculator.getBlockExplosionResistance(this, level, blockpos, blockstate, fluidstate);
                            if (optional.isPresent()) {
                                f -= (optional.get() + f1) * f1;
                            }

                            if (f > 0.0F && damageCalculator.shouldBlockExplode(this, level, blockpos, blockstate, f)) {
                                set.add(blockpos);
                            }

                            d4 += d0 * f1;
                            d6 += d1 * f1;
                            d8 += d2 * f1;
                        }
                    }
                }
            }
        }

        toBlow.addAll(set);
        float diameter = radius * 2.0F;
        int k1 = Mth.floor(x - diameter - 1);
        int l1 = Mth.floor(x + diameter + 1);
        int i2 = Mth.floor(y - diameter - 1);
        int i1 = Mth.floor(y + diameter + 1);
        int j2 = Mth.floor(z - diameter - 1);
        int j1 = Mth.floor(z + diameter + 1);
        List<Entity> list = level.getEntities(source, new AABB(k1, i2, j2, l1, i1, j1));
        Services.PLATFORM.onExplosionDetonate(level, this, list, diameter);
        Vec3 vec3 = new Vec3(x, y, z);

        for (Entity entity : list) {
            if (!entity.ignoreExplosion()) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) diameter;
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
                        entity.hurt(getDamageSource(), (float) (int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) diameter + 1.0D));
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
    }*/


    @Override
    public void finalizeExplosion(boolean pSpawnParticles) {
        Level level = $getLevel();
        if (level.isClientSide) {
            level.playLocalSound($getX(), $getY(), $getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (level.random.nextFloat() - level.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        float radius = $getRadius();

        boolean flag = this.interactsWithBlocks();
        if (pSpawnParticles) {
            if (!(radius < 2.0F) && flag) {
                level.addParticle(ParticleTypes.EXPLOSION_EMITTER, $getX(), $getY(), $getZ(), 1.0D, 0.0D, 0.0D);
            } else {
                level.addParticle(ParticleTypes.EXPLOSION, $getX(), $getY(), $getZ(), 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            boolean flag1 = this.getIndirectSourceEntity() instanceof Player;
            ObjectArrayList<BlockPos> toBlow = (ObjectArrayList<BlockPos>)this.getToBlow();
            Util.shuffle(toBlow, level.random);

            for(BlockPos blockpos : toBlow) {
                BlockState blockstate = level.getBlockState(blockpos);
                if (true || !blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    level.getProfiler().push("lava_explosion_blocks");
                    if (Services.PLATFORM.canDropFromExplosion(blockstate,level, blockpos, this)) {
                        if (level instanceof ServerLevel serverlevel) {
                            BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockpos) : null;
                            LootParams.Builder lootparams$builder = new LootParams.Builder(serverlevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, getDirectSourceEntity());
                            if ($getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, radius);
                            }

                            blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                            blockstate.getDrops(lootparams$builder).forEach(drops -> addBlockDrops(objectarraylist, drops, blockpos1));
                        }
                    }

                    Services.PLATFORM.onBlockExploded(blockstate,level, blockpos, this);
                    if (level.getBlockState(blockpos).isAir()) {
                        level.setBlock(blockpos,Blocks.LAVA.defaultBlockState(),3);
                    }

                    level.getProfiler().pop();
                }
            }

            for(Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(level, pair.getSecond(), pair.getFirst());
            }
        }

        if ($isFire()) {
            for(BlockPos blockpos2 : this.getToBlow()) {
                if (this.$getRandom().nextInt(3) == 0 && level.getBlockState(blockpos2).isAir() && level.getBlockState(blockpos2.below()).isSolidRender(level, blockpos2.below())) {
                    level.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(level, blockpos2));
                }
            }
        }
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

package tfar.mineanything;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.mixin.ExplosionAccess;
import tfar.mineanything.platform.Services;

import javax.annotation.Nullable;

public class LavaExplosion extends Explosion {

    public LavaExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction) {
        super(pLevel, pSource,pDamageSource,pDamageCalculator,pToBlowX, pToBlowY, pToBlowZ,pRadius,pFire,pBlockInteraction);
    }

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
            Util.shuffle((ObjectArrayList<BlockPos>)this.getToBlow(), level.random);

            for(BlockPos blockpos : this.getToBlow()) {
                BlockState blockstate = level.getBlockState(blockpos);
                if (!blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    level.getProfiler().push("explosion_blocks");
                    if (Services.PLATFORM.canDropFromExplosion(blockstate,level, blockpos, this)) {
                        if (level instanceof ServerLevel serverlevel) {
                            BlockEntity blockentity = blockstate.hasBlockEntity() ? level.getBlockEntity(blockpos) : null;
                            LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, getDirectSourceEntity());
                            if ($getBlockInteraction() == Explosion.BlockInteraction.DESTROY_WITH_DECAY) {
                                lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, radius);
                            }

                            blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);
                            blockstate.getDrops(lootparams$builder).forEach((drops) -> addBlockDrops(objectarraylist, drops, blockpos1));
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
}

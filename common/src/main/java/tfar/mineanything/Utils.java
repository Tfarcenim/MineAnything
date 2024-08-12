package tfar.mineanything;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.init.ModBlocks;
import tfar.mineanything.platform.Services;
import tfar.mineanything.world.LavaExplosion;
import tfar.mineanything.world.LavaExplosionCircle;
import tfar.mineanything.world.WeakExplosion;

public class Utils {



    public enum ExplosionType {
        LAVA,WEAK,LAVA_CIRCLE;
    }

    public static Explosion explode(Level level, @Nullable Entity pSource, double x, double y, double z, float pRadius, Level.ExplosionInteraction pExplosionInteraction,ExplosionType type) {
        return explode(level,pSource, null, null, x, y, z, pRadius, false, pExplosionInteraction,type );
    }

    public static Explosion explode(Level level, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double x, double y, double z, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction,ExplosionType type) {
        return explode(level,pSource, pDamageSource, pDamageCalculator, x, y, z, pRadius, pFire, pExplosionInteraction, true,type);
    }

    public static Explosion explode(Level level, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double x, double y, double z, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction, boolean pSpawnParticles, ExplosionType type) {

        Explosion.BlockInteraction explosion$blockinteraction = switch (pExplosionInteraction) {
            case NONE -> Explosion.BlockInteraction.KEEP;
            case BLOCK -> getDestroyType(level, GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                    Services.PLATFORM.getMobGriefingEvent(level, pSource) ? getDestroyType(level,GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT -> getDestroyType(level,GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
        };
        Explosion explosion = createTypedExplosion(level, pSource, pDamageSource, pDamageCalculator, x, y, z, pRadius, pFire, explosion$blockinteraction,type);
        if (Services.PLATFORM.onExplosionStart(level, explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(pSpawnParticles);

        if (!explosion.interactsWithBlocks()) {
            explosion.clearToBlow();
        }

        for(ServerPlayer serverplayer : ((ServerLevel)level).players()) {
            if (serverplayer.distanceToSqr(x, y, z) < 4096.0D) {
                serverplayer.connection.send(new ClientboundExplodePacket(x, y, z, pRadius, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer)));
            }
        }

        return explosion;
    }

    public static Explosion createTypedExplosion(Level level, @Nullable Entity source, @Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float pRadius, boolean pFire, Explosion.BlockInteraction blockInteraction, ExplosionType type) {
        switch (type) {
            case LAVA -> {
                return new LavaExplosion(level, source, damageSource, damageCalculator, x, y, z, pRadius, pFire, blockInteraction);
            }
            case WEAK -> {
                return new WeakExplosion(level, source, damageSource, damageCalculator, x, y, z, pRadius, pFire, blockInteraction);
            }
            case LAVA_CIRCLE -> {
                return new LavaExplosionCircle(level, source, damageSource, damageCalculator, x, y, z, pRadius, pFire, blockInteraction);
            }
        }
        return null;
    }

    public static Explosion.BlockInteraction getDestroyType(Level level, GameRules.Key<GameRules.BooleanValue> rule) {
        return level.getGameRules().getBoolean(rule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }
    
    static final String FLIGHT = "flight";
    static final String HOVER = "hover";

    public static boolean isFlightOn(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(FLIGHT);
    }

    public static boolean toggleFlight(ItemStack stack) {
        boolean current = isFlightOn(stack);
        stack.getOrCreateTag().putBoolean(FLIGHT,!current);
        return !current;
    }

    public static boolean isHovering(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(HOVER);
    }

    public static boolean toggleHover(ItemStack stack) {
        boolean current = stack.getOrCreateTag().getBoolean(HOVER);
        stack.getOrCreateTag().putBoolean(HOVER,!current);
        return !current;
    }

    public static Jetpack getJetpack(ItemStack stack) {
        return CREATIVE;
    }

    private static final Jetpack IRON = new Jetpack("iron", 2, 0xD8D8D8, 3, 9, "tag:forge:ingots/iron", 0F, 0F);
    private static final Jetpack EMERALD = new Jetpack("emerald", 5, 0x4DD979, 4, 15, "tag:forge:gems/emerald", 0F, 0F);
    private static final Jetpack CREATIVE = new Jetpack("creative", 0, 0xCF1AE9, 8, 0, "null", 0F, 0F).setCreative();

    static {
        CREATIVE.setStats(0, 0, 1.03D, 0.17D, 0.21D, 0.45D, 0.25D, 0.0D, 2.0D, 1.5D, 0.0D);
    }

    private static final Direction[] ALL_DIRECTIONS = Direction.values();


    public static boolean convertWaterBreadthFirstSearch(Level level, BlockPos pos) {
        return BlockPos.breadthFirstTraversal(pos, 6, 65, (pos1, consumer) -> {

            for (Direction direction : ALL_DIRECTIONS) {
                consumer.accept(pos1.relative(direction));
            }

        }, (pos2) -> {
            if (pos2.equals(pos)) {
                return true;
            } else {
                BlockState state = level.getBlockState(pos2);
                FluidState fluidState = level.getFluidState(pos2);
                if (!fluidState.is(FluidTags.WATER) && !fluidState.is(FluidTags.LAVA)) {
                    return false;
                } else {
                    Block block = state.getBlock();

                    if (block instanceof LiquidBlock) {
                        if (block == Blocks.WATER) {
                            level.setBlock(pos2, ModBlocks.MINEABLE_WATER.defaultBlockState(), 3);
                        }
                        if (block == Blocks.LAVA) {
                            level.setBlock(pos2, ModBlocks.MINEABLE_LAVA.defaultBlockState(), 3);
                        }
                    }
                    return true;
                }
            }
        }) > 1;
    }

}

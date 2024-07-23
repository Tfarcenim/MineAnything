package tfar.mineanything;

import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.platform.Services;
import tfar.mineanything.world.LavaExplosion;
import tfar.mineanything.world.WeakExplosion;

public class Utils {

    public enum ExplosionType {
        LAVA,WEAK;
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
        }
        return null;
    }

    public static Explosion.BlockInteraction getDestroyType(Level level, GameRules.Key<GameRules.BooleanValue> rule) {
        return level.getGameRules().getBoolean(rule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }
}

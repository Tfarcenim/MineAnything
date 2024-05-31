package tfar.mineanything.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.LavaExplosion;
import tfar.mineanything.platform.Services;

public class PrimedLavaTnt extends PrimedTnt {
    
    public static float POWER = 4;
    public PrimedLavaTnt(EntityType<? extends PrimedTnt> $$0, Level $$1) {
        super($$0, $$1);
    }

    public PrimedLavaTnt(Level $$0, double $$1, double $$2, double $$3, @Nullable LivingEntity $$4) {
        super($$0, $$1, $$2, $$3, $$4);
    }

    @Override
    protected void explode() {
        this.explode(this, this.getX(), this.getY(0.0625), this.getZ(), POWER, Level.ExplosionInteraction.TNT);
    }


    public Explosion explode(@Nullable Entity pSource, double x, double y, double z, float pRadius, Level.ExplosionInteraction pExplosionInteraction) {
        return this.explode(pSource, null, null, x, y, z, pRadius, false, pExplosionInteraction);
    }

    public Explosion explode(@Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double x, double y, double z, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction) {
        return this.explode(pSource, pDamageSource, pDamageCalculator, x, y, z, pRadius, pFire, pExplosionInteraction, true);
    }

    public Explosion explode(@Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double x, double y, double z, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction, boolean pSpawnParticles) {

        Explosion.BlockInteraction explosion$blockinteraction = switch (pExplosionInteraction) {
            case NONE -> Explosion.BlockInteraction.KEEP;
            case BLOCK -> getDestroyType(level(),GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                    Services.PLATFORM.getMobGriefingEvent(level(), pSource) ? getDestroyType(level(),GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT -> getDestroyType(level(),GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
        };
        Explosion explosion = createExplosion(level(), pSource, pDamageSource, pDamageCalculator, x, y, z, pRadius, pFire, explosion$blockinteraction);
        if (Services.PLATFORM.onExplosionStart(level(), explosion)) return explosion;
        explosion.explode();
        explosion.finalizeExplosion(pSpawnParticles);
        return explosion;
    }

    protected Explosion createExplosion(Level level,@Nullable Entity source,@Nullable DamageSource damageSource, @Nullable ExplosionDamageCalculator damageCalculator, double x, double y, double z, float pRadius, boolean pFire, Explosion.BlockInteraction blockInteraction) {
       return new LavaExplosion(level, source, damageSource, damageCalculator, x, y, z, pRadius, pFire, blockInteraction);
    }

    protected static Explosion.BlockInteraction getDestroyType(Level level,GameRules.Key<GameRules.BooleanValue> rule) {
        return level.getGameRules().getBoolean(rule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }


}

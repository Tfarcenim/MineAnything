package tfar.mineanything;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class LavaExplosion extends Explosion {

    public LavaExplosion(Level pLevel, @Nullable Entity pSource, @Nullable DamageSource pDamageSource, @Nullable ExplosionDamageCalculator pDamageCalculator, double pToBlowX, double pToBlowY, double pToBlowZ, float pRadius, boolean pFire, Explosion.BlockInteraction pBlockInteraction) {
        super(pLevel, pSource,pDamageSource,pDamageCalculator,pToBlowX, pToBlowY, pToBlowZ,pRadius,pFire,pBlockInteraction);
    }


}

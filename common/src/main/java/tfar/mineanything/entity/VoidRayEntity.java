package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.init.ModEntities;

import java.util.HashSet;
import java.util.Set;

public class VoidRayEntity extends AbstractHurtingProjectile {

    private float power = 5;

    int hits;

    public VoidRayEntity(EntityType<? extends AbstractHurtingProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public VoidRayEntity(LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(ModEntities.VOID_RAY,pShooter.getX(), pShooter.getY(), pShooter.getZ(), pOffsetX, pOffsetY, pOffsetZ, pLevel);
        this.setOwner(pShooter);
        this.setRot(pShooter.getYRot(), pShooter.getXRot());
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!level().isClientSide) {
            createCrater();
        }
    }

    protected void createCrater() {
        Set<BlockPos> affected = new HashSet<>();

        int r = 3;
        for (int y = -r; y <= 1;y++){
            for (int z = -r;z < r;z++) {
                for (int x = -r;x < r;x++) {
                    BlockPos pos = new BlockPos(blockPosition().getX()+x,blockPosition().getY()+y,blockPosition().getZ()+z);
                    affected.add(pos);
                }
            }
        }

        affected.add(blockPosition().below());

        for (BlockPos pos : affected) {
            FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level(),pos,level().getBlockState(pos));
            fallingBlockEntity.addDeltaMovement(new Vec3(0,0.15,0));
            this.level().explode(this, this.getX(), this.getY() - r, this.getZ(), this.power, false, Level.ExplosionInteraction.NONE);
        }
        hits++;

        if (hits >= 7) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData() {

    }
}

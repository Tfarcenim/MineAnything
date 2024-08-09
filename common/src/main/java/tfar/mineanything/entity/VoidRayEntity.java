package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.init.ModEntities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VoidRayEntity extends AbstractHurtingProjectile {

    private float power = 5;

    int hits;

    public VoidRayEntity(EntityType<? extends AbstractHurtingProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public VoidRayEntity(LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(ModEntities.VOID_RAY,pShooter.getX(), pShooter.getY(), pShooter.getZ(), 0, 0, 0, pLevel);
        this.setOwner(pShooter);
        setDeltaMovement(pOffsetX,pOffsetY,pOffsetZ);
        this.setRot(pShooter.getYRot(), pShooter.getXRot());
        setNoGravity(true);
        createOrderedBreakPositions();
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        setDeltaMovement(Vec3.ZERO);
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        double spped = getDeltaMovement().lengthSqr();
        if (!level().isClientSide && spped < .05) {
            level().getProfiler().push("voidHole");
            for (int i = 0; i < speed; i++) {
                progress++;
                if (progress < breakPositions.size()) {
                    BlockPos offset = breakPositions.get(progress);
                    BlockPos off = blockPosition().offset(offset);
                    BlockState state = level().getBlockState(off);
               //     List<ItemStack> loot = Block.getDrops(state, (ServerLevel) level(),off,level().getBlockEntity(off),getOwner(),getOwner().getItemInHand(InteractionHand.MAIN_HAND));
              //      giveItemsToOwner(loot);
                   // level().removeBlock(off,false);
                    if (random.nextDouble() < .5) {
                        FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(level(), off, state);
                        fallingBlockEntity.addDeltaMovement(new Vec3(offset.getX() / 5d, 2, offset.getZ() / 5d));
                    } else {
                         level().removeBlock(off,false);
                    }
                } else {
                    discard();
                }
            }


            level().getProfiler().pop();
        }

    }

    public int progress;

    public List<BlockPos> breakPositions = new ArrayList<>();
    public static int speed = 20;

    public static int radius = 10;

    public void createOrderedBreakPositions() {
        breakPositions = new ArrayList<>();
        for (int y = -radius;y < radius;y++) {
            for (int z = -radius;z < radius;z++) {
                for (int x = -radius;x < radius;x++) {
                    if (x * x + y * y + z * z < radius * radius) {
                        breakPositions.add(new BlockPos(x,y,z));
                    }
                }
            }
        }
        breakPositions.sort((pos1, pos2) -> {
            double product1 = pos1.distSqr(Vec3i.ZERO);
            double product2 = pos2.distSqr(Vec3i.ZERO);
            if (product1 > product2) {
                return 1;
            } else if (product1 == product2) {
                return 0;
            }
            return -1;
        });

    }



    @Override
    protected void defineSynchedData() {

    }
}

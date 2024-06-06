package tfar.mineanything.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import tfar.mineanything.init.ModEntities;

public class SkeletonArrowEntity extends AbstractArrow {
    public SkeletonArrowEntity(EntityType<? extends AbstractArrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected SkeletonArrowEntity(double $$1, double $$2, double $$3, Level $$4) {
        super(ModEntities.SKELETON_ARROW, $$1, $$2, $$3, $$4);
    }

    public SkeletonArrowEntity(LivingEntity $$1, Level $$2) {
        super(ModEntities.SKELETON_ARROW, $$1, $$2);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (!level().isClientSide) {
            spawnSkeleton();
        }
    }

    protected void spawnSkeleton() {
        for (int i = 0; i < 3; i++) {
            ServerLevel serverLevel = (ServerLevel)level();
            Skeleton skeleton = EntityType.SKELETON.create(level());
            DifficultyInstance difficultyinstance = serverLevel.getCurrentDifficultyAt(blockPosition());
            skeleton.finalizeSpawn(serverLevel,difficultyinstance, MobSpawnType.TRIGGERED, (SpawnGroupData)null, (CompoundTag)null);
            skeleton.setPos(getX(),getY(),getZ());
            serverLevel.addFreshEntity(skeleton);
            discard();
        }
    }

}

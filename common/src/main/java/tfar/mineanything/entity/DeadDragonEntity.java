package tfar.mineanything.entity;

import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.EndDragonDuck;

import java.util.function.Function;

public class DeadDragonEntity extends Mob {

    private static final EntityDataAccessor<CompoundTag> ENTITY_DATA = SynchedEntityData.defineId(DeadDragonEntity.class,EntityDataSerializers.COMPOUND_TAG);

    @Nullable
    Entity displayEntity;

    public DeadDragonEntity(EntityType<? extends Mob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ENTITY_DATA,new CompoundTag());
    }


    public static AttributeSupplier.Builder custom() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100);
    }

    public void setDisplayEntity(Entity displayEntity) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(displayEntity.getType()).toString());

        displayEntity.save(tag);
        setEntity(tag);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.4F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.4F));
            this.setYRot(this.random.nextFloat() * 360.0F);
            this.setOnGround(false);
            this.hasImpulse = true;
            if (!level().isClientSide) {
                AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level(), this.getX(), this.getY(), this.getZ());
               // Entity entity = this.getOwner();
               // if (entity instanceof LivingEntity) {
               //     areaeffectcloud.setOwner((LivingEntity)entity);
              //  }

                areaeffectcloud.setParticle(ParticleTypes.DRAGON_BREATH);
                areaeffectcloud.setRadius(3.0F);
                areaeffectcloud.setDuration(600);
                areaeffectcloud.setRadiusPerTick((7.0F - areaeffectcloud.getRadius()) / (float)areaeffectcloud.getDuration());
                areaeffectcloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));

                this.level().addFreshEntity(areaeffectcloud);
            }
        }
    }

    public Entity getOrCreateDisplayEntity() {
        if (displayEntity == null) {
            displayEntity = EntityType.loadEntityRecursive(getEntity(), level(), Function.identity());
            fixJitter();
        }
        return displayEntity;
    }

    protected void fixJitter() {
        if (displayEntity != null) {
            displayEntity.xRotO = displayEntity.getXRot();
            displayEntity.yRotO = displayEntity.getYRot();
            if (displayEntity instanceof LivingEntity livingEntity) {

                livingEntity.yBodyRotO = livingEntity.yBodyRot;
                livingEntity.yHeadRotO = livingEntity.yHeadRot;
            }
        }
    }

    @Override
    public boolean addEffect(MobEffectInstance $$0, @Nullable Entity $$1) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance $$0) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void die(DamageSource $$0) {
        super.die($$0);
        if (!level().isClientSide) {
            EndDragonFight endDragonFight = ((ServerLevel) level()).getDragonFight();
            if (endDragonFight != null) {
                ((EndDragonDuck)endDragonFight).endPart2();
            }
        }
    }

    public void setEntity(CompoundTag tag) {
        entityData.set(ENTITY_DATA,tag);
    }

    public CompoundTag getEntity() {
        return entityData.get(ENTITY_DATA);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("entity_data",getEntity());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setEntity(tag.getCompound("entity_data"));
    }
}

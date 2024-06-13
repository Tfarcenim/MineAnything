package tfar.mineanything.entity;

import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

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

    public void setDisplayEntity(Entity displayEntity) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(displayEntity.getType()).toString());

        displayEntity.save(tag);
        setEntity(tag);
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

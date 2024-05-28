package tfar.mineanything.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.client.MineAnythingClient;

import java.util.Optional;
import java.util.UUID;

public class ClonePlayerEntity extends PathfinderMob implements OwnableEntity {

    protected static final EntityDataAccessor<Optional<UUID>> DATA_CLONE_ID = SynchedEntityData.defineId(ClonePlayerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected UUID owner;

    public ClonePlayerEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE,1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, living -> living.getUUID() != owner));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CLONE_ID, Optional.empty());
    }

    public void setClone(@Nullable UUID clone) {
        entityData.set(DATA_CLONE_ID,Optional.ofNullable(clone));
    }

    public UUID getClone() {
        return entityData.get(DATA_CLONE_ID).orElse(null);
    }

    public ResourceLocation getSkinTextureLocation() {
        return MineAnythingClient.lookupSkin(getClone());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        UUID uuid = getClone();
        if (uuid != null) {
            tag.putUUID("clone",uuid);
        }
        if (owner != null) {
            tag.putUUID("owner",owner);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("clone")) {
            setClone(tag.getUUID("clone"));
        }
        if (tag.hasUUID("owner")) {
            setOwnerUUID(tag.getUUID("owner"));
        }
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwnerUUID(UUID owner) {
        this.owner = owner;
    }
}

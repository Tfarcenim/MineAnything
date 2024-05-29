package tfar.mineanything.entity;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.HasFakeItems;
import tfar.mineanything.client.MineAnythingClient;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ClonePlayerEntity extends PathfinderMob implements OwnableEntity, HasFakeItems {

    protected static final EntityDataAccessor<Optional<UUID>> DATA_CLONE_ID = SynchedEntityData.defineId(ClonePlayerEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    protected static final EntityDataAccessor<ItemStack> DATA_HEAD = SynchedEntityData.defineId(ClonePlayerEntity.class,EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<ItemStack> DATA_CHEST = SynchedEntityData.defineId(ClonePlayerEntity.class,EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<ItemStack> DATA_LEGS = SynchedEntityData.defineId(ClonePlayerEntity.class,EntityDataSerializers.ITEM_STACK);
    protected static final EntityDataAccessor<ItemStack> DATA_FEET = SynchedEntityData.defineId(ClonePlayerEntity.class,EntityDataSerializers.ITEM_STACK);

    protected UUID owner;

    private final NonNullList<ItemStack> fakeHandItems;

    public ClonePlayerEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.fakeHandItems = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE,1).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, this::shouldAttack));
    }

    boolean shouldAttack(LivingEntity living) {
        UUID uuid1 = living.getUUID();
        UUID uuid2 = getOwnerUUID();
        return !Objects.equals(uuid1,uuid2);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_CLONE_ID, Optional.empty());

        entityData.define(DATA_HEAD,ItemStack.EMPTY);
        entityData.define(DATA_CHEST,ItemStack.EMPTY);
        entityData.define(DATA_LEGS,ItemStack.EMPTY);
        entityData.define(DATA_FEET,ItemStack.EMPTY);
    }

    public ItemStack getHead() {
        return entityData.get(DATA_HEAD);
    }

    public ItemStack getChest() {
        return entityData.get(DATA_CHEST);
    }

    public ItemStack getLegs() {
        return entityData.get(DATA_LEGS);
    }

    public ItemStack getFeet() {
        return entityData.get(DATA_FEET);
    }


    public void setHead(ItemStack stack) {
        entityData.set(DATA_HEAD,stack);
    }

    public void setChest(ItemStack stack) {
        entityData.set(DATA_CHEST,stack);
    }

    public void setLegs(ItemStack stack) {
        entityData.set(DATA_LEGS,stack);
    }
    public void setFeet(ItemStack stack) {
        entityData.set(DATA_FEET,stack);
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
    public void tick() {
        super.tick();
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

        tag.put("FakeHead",getHead().save(new CompoundTag()));
        tag.put("FakeChest",getChest().save(new CompoundTag()));
        tag.put("FakeLegs",getLegs().save(new CompoundTag()));
        tag.put("FakeFeet",getFeet().save(new CompoundTag()));


        ListTag fakeHandTag = new ListTag();

        CompoundTag tag2;
        for(Iterator<ItemStack> iterator = this.fakeHandItems.iterator(); iterator.hasNext(); fakeHandTag.add(tag2)) {
            ItemStack stack = iterator.next();
            tag2 = new CompoundTag();
            if (!stack.isEmpty()) {
                stack.save(tag2);
            }
        }

        tag.put("FakeHandItems", fakeHandTag);

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

        setHead(ItemStack.of(tag.getCompound("FakeHead")));
        setChest(ItemStack.of(tag.getCompound("FakeChest")));
        setLegs(ItemStack.of(tag.getCompound("FakeLegs")));
        setFeet(ItemStack.of(tag.getCompound("FakeFeet")));


        ListTag $$7;
        int $$8;

        if (tag.contains("FakeHandItems", 9)) {
            $$7 = tag.getList("FakeHandItems", 10);

            for($$8 = 0; $$8 < this.fakeHandItems.size(); ++$$8) {
                this.fakeHandItems.set($$8, ItemStack.of($$7.getCompound($$8)));
            }
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

    @Override
    public ItemStack[] getFakeHandSlots() {
        return new ItemStack[]{fakeHandItems.get(0),fakeHandItems.get(1)};
    }

    @Override
    public ItemStack[] getFakeArmorSlots() {
        return new ItemStack[]{getFeet(),getLegs(),getChest(),getHead()};
    }

    @Override
    public void setFakeItemSlot(EquipmentSlot slot, ItemStack stack) {
        switch (slot) {
            case MAINHAND -> {
            }
            case OFFHAND -> {
            }
            case FEET -> {
                setFeet(stack);
            }
            case LEGS -> {
                setLegs(stack);
            }
            case CHEST -> {
                setChest(stack);
            }
            case HEAD -> {
                setHead(stack);
            }
        }
    }
}

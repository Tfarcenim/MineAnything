package tfar.mineanything.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import tfar.mineanything.network.client.S2CFakeEquipmentPacket;
import tfar.mineanything.platform.Services;

import java.util.*;

public class ClonePlayerEntity extends PathfinderMob implements OwnableEntity, HasFakeItems {

    protected static final EntityDataAccessor<Optional<UUID>> DATA_CLONE_ID = SynchedEntityData.defineId(ClonePlayerEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    protected UUID owner;
    private final NonNullList<ItemStack> lastFakeHandItems = NonNullList.withSize(2,ItemStack.EMPTY);

    private final NonNullList<ItemStack> fakeHandItems;
    private final NonNullList<ItemStack> lastFakeArmorItems = NonNullList.withSize(4,ItemStack.EMPTY);
    private final NonNullList<ItemStack> fakeArmorItems;

    public ClonePlayerEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        this.fakeHandItems = NonNullList.withSize(2, ItemStack.EMPTY);
        this.fakeArmorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE,1).add(Attributes.MOVEMENT_SPEED, 0.1);
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
        if (!level().isClientSide) {
            checkEquips();
        }
    }

    void checkEquips() {
        Map<EquipmentSlot, ItemStack> map = this.collectFakeEquipmentChanges();
        if (map != null) {
            this.handleHandSwap(map);
            if (!map.isEmpty()) {
                this.handleFakeEquipmentChanges(map);
            }
        }
    }

    @Nullable
    private Map<EquipmentSlot, ItemStack> collectFakeEquipmentChanges() {
        Map<EquipmentSlot, ItemStack> map = null;

        for(EquipmentSlot equipmentslot : EquipmentSlot.values()) {
            ItemStack itemstack;
            switch (equipmentslot.getType()) {
                case HAND -> itemstack = this.getLastFakeHandItem(equipmentslot);
                case ARMOR -> itemstack = this.getLastFakeArmorItem(equipmentslot);
                default -> {
                    continue;
                }
            }

            ItemStack itemstack1 = this.getFakeItemBySlot(equipmentslot);
            if (this.equipmentHasChanged(itemstack, itemstack1)) {
                //net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent(this, equipmentslot, itemstack, itemstack1));
                if (map == null) {
                    map = Maps.newEnumMap(EquipmentSlot.class);
                }

                map.put(equipmentslot, itemstack1);
                //don't add attribute modifiers, this is fake armor
            }
        }

        return map;
    }

    private void handleHandSwap(Map<EquipmentSlot, ItemStack> pHands) {
        ItemStack itemstack = pHands.get(EquipmentSlot.MAINHAND);
        ItemStack itemstack1 = pHands.get(EquipmentSlot.OFFHAND);
        if (itemstack != null && itemstack1 != null && ItemStack.matches(itemstack, this.getLastFakeHandItem(EquipmentSlot.OFFHAND)) && ItemStack.matches(itemstack1, this.getLastFakeHandItem(EquipmentSlot.MAINHAND))) {
          //  ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundEntityEventPacket(this, EntityEvent.SWAP_HANDS));
            pHands.remove(EquipmentSlot.MAINHAND);
            pHands.remove(EquipmentSlot.OFFHAND);
            this.setLastFakeHandItem(EquipmentSlot.MAINHAND, itemstack.copy());
            this.setLastFakeHandItem(EquipmentSlot.OFFHAND, itemstack1.copy());
        }

    }

    private void handleFakeEquipmentChanges(Map<EquipmentSlot, ItemStack> pEquipments) {
        List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayListWithCapacity(pEquipments.size());
        pEquipments.forEach((slot, stack) -> {
            ItemStack itemstack = stack.copy();
            list.add(Pair.of(slot, itemstack));
            switch (slot.getType()) {
                case HAND -> this.setLastFakeHandItem(slot, itemstack);
                case ARMOR -> this.setLastFakeArmorItem(slot, itemstack);
            }

        });
        Services.PLATFORM.sendToTrackingClients(new S2CFakeEquipmentPacket(this.getId(),list),this);
     //   ((ServerLevel)this.level()).getChunkSource().broadcast(this, new ClientboundSetEquipmentPacket(this.getId(), list));
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


        ListTag fakeArmorTag = new ListTag();

        CompoundTag tag1;
        for(Iterator<ItemStack> iterator = this.fakeArmorItems.iterator(); iterator.hasNext(); fakeArmorTag.add(tag1)) {
            ItemStack stack = iterator.next();
            tag1 = new CompoundTag();
            if (!stack.isEmpty()) {
                stack.save(tag1);
            }
        }

        tag.put("FakeArmorItems", fakeArmorTag);
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

        ListTag $$7;
        int $$8;
        if (tag.contains("FakeArmorItems", 9)) {
            $$7 = tag.getList("FakeArmorItems", 10);

            for($$8 = 0; $$8 < this.fakeArmorItems.size(); ++$$8) {
                this.fakeArmorItems.set($$8, ItemStack.of($$7.getCompound($$8)));
            }
        }

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
    public NonNullList<ItemStack> getLastFakeHandItems() {
        return lastFakeHandItems;
    }

    @Override
    public NonNullList<ItemStack> getFakeHandSlots() {
        return fakeHandItems;
    }

    @Override
    public NonNullList<ItemStack> getLastFakeArmorItems() {
        return lastFakeArmorItems;
    }
    @Override
    public NonNullList<ItemStack> getFakeArmorSlots() {
        return fakeArmorItems;
    }
}

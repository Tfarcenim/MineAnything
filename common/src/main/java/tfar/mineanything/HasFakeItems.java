package tfar.mineanything;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface HasFakeItems {

    NonNullList<ItemStack> getLastFakeHandItems();
    NonNullList<ItemStack> getFakeHandSlots();
    NonNullList<ItemStack> getLastFakeArmorItems();
    NonNullList<ItemStack> getFakeArmorSlots();

    default ItemStack getLastFakeArmorItem(EquipmentSlot pSlot) {
        return getLastFakeArmorItems().get(pSlot.getIndex());
    }

    default void setLastFakeArmorItem(EquipmentSlot pSlot, ItemStack pStack) {
        this.getLastFakeArmorItems().set(pSlot.getIndex(), pStack);
    }

    default ItemStack getLastFakeHandItem(EquipmentSlot pSlot) {
        return this.getLastFakeHandItems().get(pSlot.getIndex());
    }

    default void setLastFakeHandItem(EquipmentSlot pSlot, ItemStack pStack) {
        this.getLastFakeHandItems().set(pSlot.getIndex(), pStack);
    }

    default ItemStack getFakeItemBySlot(EquipmentSlot slot) {
        return switch (slot.getType()) {
            case HAND -> this.getFakeHandSlots().get(slot.getIndex());
            case ARMOR -> this.getFakeArmorSlots().get(slot.getIndex());
        };
    }

    default void setFakeItemSlot(EquipmentSlot slot, ItemStack stack) {
        //this.verifyEquippedItem(stack);
        switch (slot.getType()) {
            case HAND -> getFakeHandSlots().set(slot.getIndex(), stack);
            case ARMOR -> getFakeArmorSlots().set(slot.getIndex(), stack);
        }

    }

}

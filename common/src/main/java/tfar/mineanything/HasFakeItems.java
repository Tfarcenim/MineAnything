package tfar.mineanything;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface HasFakeItems {

    ItemStack[] getFakeHandSlots();

    ItemStack[] getFakeArmorSlots();


    default ItemStack getFakeItemBySlot(EquipmentSlot slot) {
        return switch (slot.getType()) {
            case HAND -> this.getFakeHandSlots()[slot.getIndex()];
            case ARMOR -> this.getFakeArmorSlots()[slot.getIndex()];
        };
    }

    void setFakeItemSlot(EquipmentSlot slot, ItemStack stack) ;

}

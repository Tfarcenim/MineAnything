package tfar.mineanything.network.client;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import tfar.mineanything.client.MineAnythingClient;

import java.util.List;

public class S2CFakeEquipmentPacket implements S2CModPacket<S2CFakeEquipmentPacket>{
    private static final byte CONTINUE_MASK = Byte.MIN_VALUE;
    private final int entity;
    private final List<Pair<EquipmentSlot, ItemStack>> slots;

    public S2CFakeEquipmentPacket(int pEntity, List<Pair<EquipmentSlot, ItemStack>> pSlots) {
        this.entity = pEntity;
        this.slots = pSlots;
    }

    public S2CFakeEquipmentPacket(FriendlyByteBuf pBuffer) {
        this.entity = pBuffer.readVarInt();
        EquipmentSlot[] aequipmentslot = EquipmentSlot.values();
        this.slots = Lists.newArrayList();

        int i;
        do {
            i = pBuffer.readByte();
            EquipmentSlot equipmentslot = aequipmentslot[i & Byte.MAX_VALUE];
            ItemStack itemstack = pBuffer.readItem();
            this.slots.add(Pair.of(equipmentslot, itemstack));
        } while((i & CONTINUE_MASK) != 0);
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.entity);
        int i = this.slots.size();

        for(int j = 0; j < i; ++j) {
            Pair<EquipmentSlot, ItemStack> pair = this.slots.get(j);
            EquipmentSlot equipmentslot = pair.getFirst();
            boolean flag = j != i - 1;
            int k = equipmentslot.ordinal();
            pBuffer.writeByte(flag ? k | -128 : k);
            pBuffer.writeItem(pair.getSecond());
        }
    }

    @Override
    public void handleClient() {
        MineAnythingClient.setFakeClientEquipment(entity,slots);
    }
}

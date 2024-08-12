package tfar.mineanything.network.client;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import tfar.mineanything.client.MineAnythingClient;

import java.util.List;

public class S2CDisguisePacket implements S2CModPacket {
    private final int entity;
    private final GameProfile profile;

    public S2CDisguisePacket(int pEntity,GameProfile profile) {
        this.entity = pEntity;
        this.profile = profile;
    }

    public S2CDisguisePacket(FriendlyByteBuf pBuffer) {
        this.entity = pBuffer.readVarInt();
        profile = pBuffer.readGameProfile();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.entity);
        pBuffer.writeGameProfile(profile);
    }

    @Override
    public void handleClient() {
        MineAnythingClient.setDisguise(entity, profile);
    }
}

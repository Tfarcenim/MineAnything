package tfar.mineanything.network.client;

import net.minecraft.network.FriendlyByteBuf;
import tfar.mineanything.client.MineAnythingClient;

public class S2CClearDisguisePacket implements S2CModPacket {
    private final int entity;

    public S2CClearDisguisePacket(int pEntity) {
        this.entity = pEntity;
    }

    public S2CClearDisguisePacket(FriendlyByteBuf pBuffer) {
        this.entity = pBuffer.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeVarInt(this.entity);
    }

    @Override
    public void handleClient() {
        MineAnythingClient.setDisguise(entity, null);
    }
}

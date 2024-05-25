package tfar.mineanything.network;

import net.minecraft.network.FriendlyByteBuf;

public interface ModPacket<T> {
    void write(FriendlyByteBuf to);
}

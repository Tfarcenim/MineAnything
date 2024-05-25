package tfar.mineanything.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.mineanything.network.ModPacket;

public interface C2SModPacket<T> extends ModPacket<T> {

    void handleServer(ServerPlayer player);

}

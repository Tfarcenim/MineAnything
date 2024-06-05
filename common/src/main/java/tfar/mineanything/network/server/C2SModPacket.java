package tfar.mineanything.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.mineanything.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}

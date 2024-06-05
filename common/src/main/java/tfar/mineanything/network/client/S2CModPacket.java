package tfar.mineanything.network.client;

import tfar.mineanything.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}

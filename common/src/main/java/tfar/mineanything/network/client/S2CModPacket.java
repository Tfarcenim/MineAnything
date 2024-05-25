package tfar.mineanything.network.client;

import tfar.mineanything.network.ModPacket;

public interface S2CModPacket<T> extends ModPacket<T> {

    void handleClient();

}

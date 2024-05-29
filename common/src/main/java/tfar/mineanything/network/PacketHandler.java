package tfar.mineanything.network;


import tfar.mineanything.MineAnything;
import tfar.mineanything.network.client.S2CFakeEquipmentPacket;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;
import tfar.mineanything.platform.Side;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SKeyActionPacket.class, C2SKeyActionPacket::new);
        if (Services.PLATFORM.getPlatformName().equals("Forge") || MineAnything.SIDE == Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CFakeEquipmentPacket.class,S2CFakeEquipmentPacket::new);
    }
}

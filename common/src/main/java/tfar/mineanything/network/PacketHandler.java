package tfar.mineanything.network;


import tfar.mineanything.MineAnything;
import tfar.mineanything.network.client.S2CClearDisguisePacket;
import tfar.mineanything.network.client.S2CDisguisePacket;
import tfar.mineanything.network.client.S2CFakeEquipmentPacket;
import tfar.mineanything.network.server.C2SInputPacket;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;
import tfar.mineanything.platform.Side;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerPacket(C2SKeyActionPacket.class, C2SKeyActionPacket::new);
        Services.PLATFORM.registerServerPacket(C2SInputPacket.class, C2SInputPacket::new);
        if (!Services.PLATFORM.getPlatformName().equals("Fabric") || MineAnything.SIDE == Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientPacket(S2CFakeEquipmentPacket.class,S2CFakeEquipmentPacket::new);
        Services.PLATFORM.registerClientPacket(S2CDisguisePacket.class,S2CDisguisePacket::new);
        Services.PLATFORM.registerClientPacket(S2CClearDisguisePacket.class,S2CClearDisguisePacket::new);

    }
}

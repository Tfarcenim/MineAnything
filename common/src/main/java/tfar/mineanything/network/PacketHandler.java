package tfar.mineanything.network;


import tfar.mineanything.MineAnything;
import tfar.mineanything.platform.Services;
import tfar.mineanything.platform.Side;

public class PacketHandler {

    public static void registerPackets() {
       // Services.PLATFORM.registerServerMessage(C2STeamCreatePacket.ID, C2STeamCreatePacket::new);
        if (Services.PLATFORM.getPlatformName().equals("Forge") || MineAnything.SIDE == Side.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {

    }
}

package tfar.mineanything.platform;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfar.mineanything.MineAnything;
import tfar.mineanything.client.MineAnythingClient;
import tfar.mineanything.network.PacketHandlerForge;
import tfar.mineanything.network.client.S2CModPacket;
import tfar.mineanything.network.server.C2SModPacket;
import tfar.mineanything.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public Side getSide() {
        return FMLEnvironment.dist.isClient() ? Side.CLIENT : Side.SERVER;
    }

    @Override
    public void registerKeyBinding(KeyMapping keyMapping) {
        MineAnythingClient.registerKeybinding(keyMapping);
    }

    @Override
    public <MSG extends S2CModPacket<MSG>> void sendToClient(S2CModPacket<MSG> msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg,player);
    }

    @Override
    public <MSG extends C2SModPacket<MSG>> void sendToServer(C2SModPacket<MSG> msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    int i;

    @Override
    public <MSG extends S2CModPacket<MSG>> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public <MSG extends C2SModPacket<MSG>> void registerServerPacket(Class<MSG>  packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }

}
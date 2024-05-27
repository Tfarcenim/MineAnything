package tfar.mineanything.platform.services;

import net.minecraft.client.KeyMapping;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.mineanything.network.client.S2CModPacket;
import tfar.mineanything.network.server.C2SModPacket;
import tfar.mineanything.platform.Side;

import java.util.Collection;
import java.util.function.Function;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    Side getSide();
    void registerKeyBinding(KeyMapping keyMapping);

    <MSG extends S2CModPacket<MSG>> void sendToClient(S2CModPacket<MSG> msg, ServerPlayer player);

    default <MSG extends S2CModPacket<MSG>> void sendToClients(S2CModPacket<MSG> msg, Collection<ServerPlayer> playerList) {
        playerList.forEach(player -> sendToClient(msg,player));
    }
    <MSG extends C2SModPacket<MSG>> void sendToServer(C2SModPacket<MSG> msg);

    <MSG extends S2CModPacket<MSG>> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);

    <MSG extends C2SModPacket<MSG>> void registerServerPacket(Class<MSG> packetLocation,Function<FriendlyByteBuf,MSG> reader);

    <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter);


}
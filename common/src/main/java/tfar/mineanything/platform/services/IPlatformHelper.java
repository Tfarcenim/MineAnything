package tfar.mineanything.platform.services;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tfar.mineanything.network.client.S2CModPacket;
import tfar.mineanything.network.server.C2SModPacket;
import tfar.mineanything.platform.Side;

import javax.annotation.Nullable;
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

    <MSG extends S2CModPacket<MSG>> void sendToTrackingClients(S2CModPacket<MSG> msg, Entity entity);

    <MSG extends C2SModPacket<MSG>> void sendToServer(C2SModPacket<MSG> msg);

    <MSG extends S2CModPacket<MSG>> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);

    <MSG extends C2SModPacket<MSG>> void registerServerPacket(Class<MSG> packetLocation,Function<FriendlyByteBuf,MSG> reader);

    <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter);

    <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> Model getForgeModel(T entity, ItemStack itemStack, EquipmentSlot slot, A model);

    <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> ResourceLocation getArmorResource(HumanoidArmorLayer<T,M,A> layer,Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type);

}
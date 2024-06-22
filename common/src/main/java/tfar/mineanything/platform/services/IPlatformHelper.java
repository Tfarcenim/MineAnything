package tfar.mineanything.platform.services;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
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

    void sendToClient(S2CModPacket msg, ServerPlayer player);

    default void sendToClients(S2CModPacket msg, Collection<ServerPlayer> playerList) {
        playerList.forEach(player -> sendToClient(msg,player));
    }

    void sendToTrackingClients(S2CModPacket msg, Entity entity);

    void sendToServer(C2SModPacket msg);

    <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);

    <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);

    <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter);

    <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> Model getForgeModel(T entity, ItemStack itemStack, EquipmentSlot slot, A model);

    <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> ResourceLocation getArmorResource(HumanoidArmorLayer<T,M,A> layer,Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type);


    //forge hooks
    boolean canDropFromExplosion(BlockState blockState, Level level, BlockPos pos, Explosion explosion);

    void onBlockExploded(BlockState blockState, Level level, BlockPos pos, Explosion explosion);

    boolean onExplosionStart(Level level, Explosion explosion);

    boolean getMobGriefingEvent(Level level,Entity source);

    FlowingFluid createCreeperWaterFluid();
    FlowingFluid createCreeperWaterFlowingFluid();

    ServerPlayer makeFakePlayer(ServerLevel level, GameProfile profile);

    void registerRenderLayer(Block block, RenderType renderType);
    void registerRenderLayer(Fluid fluid, RenderType renderType);
    ShieldItem fortifiedShield(Item.Properties properties);

}
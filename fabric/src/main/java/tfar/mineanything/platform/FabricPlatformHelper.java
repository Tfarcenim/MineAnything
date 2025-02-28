package tfar.mineanything.platform;

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
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.network.client.S2CModPacket;
import tfar.mineanything.network.server.C2SModPacket;
import tfar.mineanything.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;
import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Side getSide() {
        return null;
    }

    @Override
    public void registerKeyBinding(KeyMapping keyMapping) {

    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {

    }

    @Override
    public void sendToTrackingClients(S2CModPacket msg, Entity entity) {

    }

    @Override
    public void sendToServer(C2SModPacket msg) {

    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter) {

    }

    @Override
    public <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> Model getForgeModel(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
        return null;
    }

    @Override
    public <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> ResourceLocation getArmorResource(HumanoidArmorLayer<T, M, A> layer, Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        return null;
    }

    @Override
    public boolean canDropFromExplosion(BlockState blockState, Level level, BlockPos pos, Explosion explosion) {
        return false;
    }

    @Override
    public void onBlockExploded(BlockState blockState, Level level, BlockPos pos, Explosion explosion) {

    }

    @Override
    public boolean onExplosionStart(Level level, Explosion explosion) {
        return false;
    }

    @Override
    public boolean getMobGriefingEvent(Level level, Entity source) {
        return false;
    }

    @Override
    public void onExplosionDetonate(Level level, Explosion explosion, List<Entity> list, double diameter) {

    }

    @Override
    public FlowingFluid createCreeperWaterFluid() {
        return null;
    }

    @Override
    public FlowingFluid createCreeperWaterFlowingFluid() {
        return null;
    }

    @Override
    public ServerPlayer makeFakePlayer(ServerLevel level, GameProfile profile) {
        return null;
    }

    @Override
    public void registerRenderLayer(Block block, RenderType renderType) {

    }

    @Override
    public void registerRenderLayer(Fluid fluid, RenderType renderType) {

    }

    @Override
    public ShieldItem fortifiedShield(Item.Properties properties) {
        return null;
    }

    @Override
    public InteractionResultHolder<ItemStack> onBucketUse(@NotNull Player player, @NotNull Level level, @NotNull ItemStack stack, @Nullable HitResult target) {
        return null;
    }
}

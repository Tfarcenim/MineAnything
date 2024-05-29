package tfar.mineanything.platform;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.MineAnything;
import tfar.mineanything.MineAnythingForge;
import tfar.mineanything.client.MineAnythingClient;
import tfar.mineanything.network.PacketHandlerForge;
import tfar.mineanything.network.client.S2CModPacket;
import tfar.mineanything.network.server.C2SModPacket;
import tfar.mineanything.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

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
    public <MSG extends S2CModPacket<MSG>> void sendToTrackingClients(S2CModPacket<MSG> msg, Entity entity) {
        PacketHandlerForge.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity),msg);
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

    @Override
    public <F> void registerAll(Class<?> clazz, Registry<? extends F> registry, Class<F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = MineAnythingForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Field field : clazz.getFields()) {
            MappedRegistry<?> forgeRegistry = (MappedRegistry<?>) registry;
            forgeRegistry.unfreeze();
            try {
                Object o = field.get(null);
                if (filter.isInstance(o)) {
                    list.add(Pair.of(MineAnything.id(field.getName().toLowerCase(Locale.ROOT)), () -> o));
                }
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
    }

    @Override
    public <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> Model getForgeModel(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
        return net.minecraftforge.client.ForgeHooksClient.getArmorModel(entity, itemStack, slot, model);
    }

    @Override
    public <T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> ResourceLocation getArmorResource(HumanoidArmorLayer<T, M, A> layer, Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
        return layer.getArmorResource(entity, stack, slot, type);
    }
}
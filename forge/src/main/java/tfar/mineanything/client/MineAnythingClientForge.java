package tfar.mineanything.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.codehaus.plexus.util.CachedMap;
import tfar.mineanything.MineAnything;
import tfar.mineanything.PlayerDuck;
import tfar.mineanything.client.render.layers.DragonElytraLayer;
import tfar.mineanything.mixin.LivingEntityRendererAccess;

import java.util.*;

public class MineAnythingClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(MineAnythingClientForge::clientSetup);
        bus.addListener(MineAnythingClientForge::registerRenderers);
        bus.addListener(MineAnythingClientForge::itemColors);
        bus.addListener(MineAnythingClientForge::layers);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::renderPlayerPre);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL,true,MineAnythingClientForge::renderPlayerPost);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientPlayerTick);
    }

    static void itemColors(RegisterColorHandlersEvent.Item event) {
        MineAnythingClient.itemColors(event.getItemColors());
    }

    static void clientSetup(FMLClientSetupEvent event) {
        MineAnythingClient.clientSetup();
    }

    static void clientTick(TickEvent.ClientTickEvent event) {
        MineAnythingClient.clientTick();
    }

    static void layers(EntityRenderersEvent.AddLayers event) {

        for (String model : new String[]{"default","slim"}) {
            PlayerRenderer playerRenderer = event.getSkin(model);
            playerRenderer.addLayer(new DragonElytraLayer<>(playerRenderer,event.getEntityModels()));
        }
        ArmorStandRenderer renderer = event.getRenderer(EntityType.ARMOR_STAND);
        renderer.addLayer(new DragonElytraLayer<>(renderer,event.getEntityModels()));
    }

    static void clientPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
            if (headItem.is(Items.PLAYER_HEAD)) {
                CompoundTag tag = headItem.getTag();
                if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                    GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                    if (!Objects.equals(player.getGameProfile(), gameProfile)) {
                        PlayerDuck.of(player).setDisguise(gameProfile);
                        return;
                    }
                }
            }
            PlayerDuck.of(player).setDisguise(null);
        }
    }
    static PlayerModel<?> original;

    static Map<EquipmentSlot,ItemStack> originalItems = new EnumMap<>(EquipmentSlot.class);

    static void renderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();

        GameProfile disguise = PlayerDuck.of(player).disguise();

        if (disguise != null) {
            String modelInfo = getModelInfo(disguise);
            original = event.getRenderer().getModel();
            PlayerModel<?> newPlayerModel = ((PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get(modelInfo)).getModel();

            ((LivingEntityRendererAccess)event.getRenderer()).setModel(newPlayerModel);

            Player imitate = Minecraft.getInstance().level.getPlayerByUUID(disguise.getId());

            if (imitate != null) {//todo, use a packet to send items?
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    originalItems.put(slot,player.getItemBySlot(slot));
                    player.setItemSlot(slot, imitate.getItemBySlot(slot));
                }
            }
        }
    }

    static void renderPlayerPost(RenderPlayerEvent.Post event) {
        Player player = event.getEntity();

        GameProfile disguise = PlayerDuck.of(player).disguise();
        if (disguise != null) {
            ((LivingEntityRendererAccess)event.getRenderer()).setModel(original);
            originalItems.forEach(player::setItemSlot);
        }
    }
    public static String getModelInfo(GameProfile gameProfile) {

        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.getInsecureSkinInformation(gameProfile);

        if (!map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return DefaultPlayerSkin.getSkinModelName(gameProfile.getId());
        } else {
            String model = map.get(MinecraftProfileTexture.Type.SKIN).getMetadata("model");
            return model != null ? model : "default";
        }
    }


    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        MineAnythingClient.registerRenderers();
    }

}

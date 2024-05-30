package tfar.mineanything.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.codehaus.plexus.util.CachedMap;
import tfar.mineanything.MineAnything;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class MineAnythingClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(MineAnythingClientForge::clientSetup);
        bus.addListener(MineAnythingClientForge::registerRenderers);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::renderPlayer);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientPlayerTick);
    }

    static void clientSetup(FMLClientSetupEvent event) {
        MineAnythingClient.clientSetup();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        MineAnythingClient.clientTick();
    }

    static void clientPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.START) {
            Player player = event.player;
            ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
            if (headItem.is(Items.PLAYER_HEAD)) {
                CompoundTag tag = headItem.getTag();
                if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                    GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                    if (!Objects.equals(player.getGameProfile(), gameProfile)) {
                        RemotePlayer fakePlayer = playerCache.computeIfAbsent(gameProfile.getId(),uuid -> new RemotePlayer((ClientLevel)player.level(), gameProfile));
                        fakePlayer.tick();
                    }
                }
            }
        }
    }

    private static final Map<UUID, RemotePlayer> playerCache = new HashMap<>();

    public static boolean isDisguised(Player player) {
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.is(Items.PLAYER_HEAD)) {
            CompoundTag tag = headItem.getTag();
            if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                if (!Objects.equals(player.getGameProfile(), gameProfile)) {
                    return true;
                }
            }
        }
        return false;
    }


    static void renderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.is(Items.PLAYER_HEAD)) {
            CompoundTag tag = headItem.getTag();
            if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                if (!Objects.equals(player.getGameProfile(),gameProfile)) {

                    String modelInfo = getModelInfo(gameProfile);

                    PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get(modelInfo);

                    RemotePlayer fakePlayer = playerCache.computeIfAbsent(gameProfile.getId(),uuid -> new RemotePlayer((ClientLevel)player.level(), gameProfile));

                    Player imitate = Minecraft.getInstance().level.getPlayerByUUID(gameProfile.getId());

                    if (imitate != null) {
                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            fakePlayer.setItemSlot(slot,imitate.getItemBySlot(slot));
                        }
                    }

                    float yaw = Mth.lerp(event.getPartialTick(), player.yRotO, player.getYRot());

                    fakePlayer.setXRot(player.getXRot());
                    fakePlayer.setYRot(player.getYRot());

                    fakePlayer.xRotO = player.xRotO;
                    fakePlayer.yRotO = player.yRotO;

                    fakePlayer.yHeadRotO = player.yHeadRotO;
                    fakePlayer.yHeadRot = player.yHeadRot;

                    fakePlayer.yBodyRotO = player.yBodyRotO;
                    fakePlayer.yBodyRot = player.yBodyRot;
                    fakePlayer.setDeltaMovement(player.getDeltaMovement());
                    ;
                    playerRenderer.render(fakePlayer,yaw,event.getPartialTick(),event.getPoseStack(),event.getMultiBufferSource(),event.getPackedLight());


                    event.setCanceled(true);
                }
            }
        }
    }

    public static ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.getInsecureSkinInformation(gameProfile);
        return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? skinManager.registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) :
                DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile));
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

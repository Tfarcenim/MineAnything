package tfar.mineanything.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
    }

    static void clientSetup(FMLClientSetupEvent event) {
        MineAnythingClient.clientSetup();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        MineAnythingClient.clientTick();
    }

    private static final Map<UUID, RemotePlayer> playerCache = new HashMap<>();


    static void renderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        if (headItem.is(Items.PLAYER_HEAD)) {
            CompoundTag tag = headItem.getTag();
            if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                if (!Objects.equals(player.getGameProfile(),gameProfile)) {
                    PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(gameProfile.getId());
                    if (playerInfo != null) {
                        PlayerRenderer playerRenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().get(playerInfo.getModelName());

                        RemotePlayer fakePlayer = playerCache.computeIfAbsent(gameProfile.getId(),uuid -> new RemotePlayer((ClientLevel)player.level(), gameProfile));

                        float yaw = Mth.lerp(event.getPartialTick(), player.yRotO, player.getYRot());
;
                        playerRenderer.render(fakePlayer,yaw,event.getPartialTick(),event.getPoseStack(),event.getMultiBufferSource(),event.getPackedLight());


                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        MineAnythingClient.registerRenderers();
    }

}

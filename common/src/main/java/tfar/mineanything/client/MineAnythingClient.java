package tfar.mineanything.client;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
import tfar.mineanything.client.render.ClonePlayerEntityRenderer;
import tfar.mineanything.client.render.PlayerBodyBlockEntityRenderer;
import tfar.mineanything.init.ModBlockEntities;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineAnythingClient {

    public static void clientTick() {
        List<ModKeybinds.ModKeybind> keys = ModKeybinds.KEYS;
        for (ModKeybinds.ModKeybind key : keys) {
            if (key.consumeClick()) {
                key.onPress.run();
            }
        }
    }

    public static void clientSetup() {
        ModKeybinds.KEYS.forEach(Services.PLATFORM::registerKeyBinding);
    }

    public static void spawnPingParticles() {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level != null && player != null) {


            int particleCount = 32;

            for (int i = 0; i < particleCount;i++) {

                double angle = 360d * i/particleCount;

                double x = Math.cos(angle * Math.PI /180);
                double z = Math.sin(angle * Math.PI /180);

                //   public void addParticle(ParticleOptions pParticleData, boolean pForceAlwaysRender,
                //   double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
                level.addParticle(ParticleTypes.END_ROD, false, player.getX(), player.getY()+1, player.getZ(), x, 0, z);
            }
            Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.PING));
        }
    }

    public static ResourceLocation lookupSkin(UUID profile) {
        if (profile == null) profile = Util.NIL_UUID;
        GameProfile profile1 = new GameProfile(profile,null);
        return getPlayerSkin(profile1);
    }

    public static ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.getInsecureSkinInformation(gameProfile);
        return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? skinManager.registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) :
                DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile));
    }

    public static void registerRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.PLAYER_BODY, PlayerBodyBlockEntityRenderer::new);
        EntityRenderers.register(ModEntities.CLONE_PLAYER, context -> new ClonePlayerEntityRenderer(context,false));
    }

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

}

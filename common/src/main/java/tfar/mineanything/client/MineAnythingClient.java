package tfar.mineanything.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.commons.lang3.ArrayUtils;
import tfar.mineanything.client.render.PlayerBodyBlockEntityRenderer;
import tfar.mineanything.init.ModBlockEntities;
import tfar.mineanything.platform.Services;

import java.util.List;

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
        }
    }

    public static void registerRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.PLAYER_BODY, PlayerBodyBlockEntityRenderer::new);
    }

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

}

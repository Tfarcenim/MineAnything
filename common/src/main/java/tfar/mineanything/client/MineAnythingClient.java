package tfar.mineanything.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
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
            //   public void addParticle(ParticleOptions pParticleData, boolean pForceAlwaysRender,
            //   double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            level.addParticle(ParticleTypes.ASH, false, player.getX(),player.getY(),player.getZ(),0,0,0);
        }
    }

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

}

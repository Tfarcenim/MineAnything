package tfar.mineanything.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import tfar.mineanything.network.server.C2SInputPacket;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {
    public static final List<ModKeybind> KEYS = new ArrayList<>();
    static final String CAT = "mineanything";
    public static final ModKeybind LEVEL_UP = new ModKeybind("level_up", GLFW.GLFW_KEY_L,CAT, () -> Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.LEVEL_UP)));

    public static final ModKeybind PING = new ModKeybind("ping", GLFW.GLFW_KEY_P,CAT, MineAnythingClient::spawnPingParticles);
    public static final ModKeybind END_PING = new ModKeybind("end_ping", GLFW.GLFW_KEY_P,CAT, MineAnythingClient::spawnEndPingParticles);



    public static final ModKeybind CLONE = new ModKeybind("clone",GLFW.GLFW_KEY_COMMA,CAT,() -> Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.CLONE)));
    public static final ModKeybind TOGGLE_FLIGHT = new ModKeybind("toggle_flight", GLFW.GLFW_KEY_F,CAT, () -> Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.TOGGLE_FLIGHT)));
    public static final ModKeybind TOGGLE_HOVER = new ModKeybind("toggle_hover",GLFW.GLFW_KEY_V,CAT,() -> Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.TOGGLE_HOVER)));

    public static class ModKeybind extends KeyMapping {

        public final Runnable onPress;

        public ModKeybind(String $$0, int $$1, String $$2,Runnable onPress) {
            this($$0, InputConstants.Type.KEYSYM,$$1, $$2,onPress);
        }

        public ModKeybind(String $$0, InputConstants.Type $$1, int $$2, String $$3,Runnable onPress) {
            super($$0, $$1, $$2, $$3);
            this.onPress = onPress;
        }
    }

    static {
        for (Field field : ModKeybinds.class.getFields()) {
            try {
                if (field.get(null) instanceof ModKeybind keyMapping) {
                    KEYS.add(keyMapping);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package tfar.mineanything.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ModKeybinds {
    public static final List<ModKeybind> KEYS = new ArrayList<>();
    public static final ModKeybind LEVEL_UP = new ModKeybind("level_up", GLFW.GLFW_KEY_L,"mineanything",new Runnable(){
        @Override
        public void run() {

        }
    });

    public static final ModKeybind PING = new ModKeybind("ping", GLFW.GLFW_KEY_P,"mineanything", MineAnythingClient::spawnPingParticles);

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

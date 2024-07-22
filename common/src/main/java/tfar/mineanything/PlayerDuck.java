package tfar.mineanything;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface PlayerDuck {

    @Nullable GameProfile disguise();
    void setDisguise(@Nullable GameProfile disguise);
    int getCloneCooldown();
    void setCloneCooldown(int cooldown);

    static PlayerDuck of(Player player) {
        return (PlayerDuck) player;
    }
}

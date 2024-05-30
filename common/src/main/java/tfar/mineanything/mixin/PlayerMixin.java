package tfar.mineanything.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import tfar.mineanything.PlayerDuck;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Nullable GameProfile disguise;

    @Override
    public @Nullable GameProfile disguise() {
        return disguise;
    }

    @Override
    public void setDisguise(@Nullable GameProfile disguise) {
        this.disguise = disguise;
    }
}

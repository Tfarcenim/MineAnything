package tfar.mineanything.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.mineanything.PlayerDuck;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Unique
    @Nullable GameProfile disguise;
    @Unique
    int cloneCooldown;

    @Override
    public @Nullable GameProfile disguise() {
        return disguise;
    }

    @Override
    public void setDisguise(@Nullable GameProfile disguise) {
        this.disguise = disguise;
    }

    @Override
    public int getCloneCooldown() {
        return cloneCooldown;
    }

    @Override
    public void setCloneCooldown(int cooldown) {
        cloneCooldown = cooldown;
    }
}

package tfar.mineanything.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mineanything.PlayerDuck;

@Mixin(Player.class)
public class PlayerMixin implements PlayerDuck {

    @Unique
    @Nullable GameProfile disguise;
    @Unique
    int cloneCooldown;
    @Unique
    boolean runner;

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

    @Override
    public boolean isRunner() {
        return runner;
    }

    @Override
    public void setRunner(boolean runner) {
        this.runner = runner;
    }


    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag $$0, CallbackInfo ci){
        $$0.putBoolean("runner",runner);
    }


    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag $$0, CallbackInfo ci){
        runner = $$0.getBoolean("runner");
    }

}

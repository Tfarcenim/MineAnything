package tfar.mineanything.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.mineanything.PlayerDuck;
import tfar.mineanything.client.MineAnythingClient;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {

    @Inject(method = "getTextureLocation(Lnet/minecraft/client/player/AbstractClientPlayer;)Lnet/minecraft/resources/ResourceLocation;",at = @At("HEAD"),cancellable = true)
    private void handleDisguise(AbstractClientPlayer player, CallbackInfoReturnable<ResourceLocation> cir) {
        GameProfile disguise = PlayerDuck.of(player).disguise();
        if (disguise != null) {
            cir.setReturnValue(MineAnythingClient.getPlayerSkin(disguise));
        }
    }
}

package tfar.mineanything.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.mineanything.client.MineAnythingClient;
import tfar.mineanything.entity.ClonePlayerEntity;

import java.util.Map;
import java.util.UUID;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    private Map<String, EntityRenderer<ClonePlayerEntity>> cloneRenderers = ImmutableMap.of();


    @Inject(at = @At("HEAD"), method = "getRenderer",cancellable = true)
    private <T extends Entity> void init(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        if (entity instanceof ClonePlayerEntity clonePlayerEntity) {
            UUID clone = clonePlayerEntity.getClone();
            PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(clone);
            if (playerInfo != null) {
                cir.setReturnValue((EntityRenderer<? super T>) cloneRenderers.get(playerInfo.getModelName()));
            } else {
                cir.setReturnValue((EntityRenderer<? super T>) cloneRenderers.get("default"));
            }
        }
    }

    @Inject(method = "onResourceManagerReload",at = @At("RETURN"),locals = LocalCapture.CAPTURE_FAILHARD)
    private void resourceHook(ResourceManager $$0, CallbackInfo ci, EntityRendererProvider.Context context) {
        cloneRenderers = MineAnythingClient.createCloneRenderers(context);
    }
}
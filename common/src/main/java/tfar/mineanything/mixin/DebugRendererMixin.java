package tfar.mineanything.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.debug.PathfindingRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

    @Shadow @Final public PathfindingRenderer pathfindingRenderer;

    @Inject(method = "render",at = @At("RETURN"))
    private void forcePath(PoseStack $$0, MultiBufferSource.BufferSource $$1, double $$2, double $$3, double $$4, CallbackInfo ci) {
        this.pathfindingRenderer.render($$0,$$1,$$2,$$3,$$4);
    }

}

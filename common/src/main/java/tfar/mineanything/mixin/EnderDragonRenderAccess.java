package tfar.mineanything.mixin;

import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonRenderer.class)
public interface EnderDragonRenderAccess {
    @Accessor
    EnderDragonRenderer.DragonModel getModel();
}

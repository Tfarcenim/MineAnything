package tfar.mineanything.mixin;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnderDragonRenderer.DragonModel.class)
public interface DragonModelAccess {

    @Accessor
    ModelPart getLeftWing();

    @Accessor
    ModelPart getRightWing();

}

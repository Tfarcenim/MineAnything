package tfar.mineanything.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.mineanything.MineAnything;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canAttack(Lnet/minecraft/world/entity/LivingEntity;)Z",at = @At("HEAD"),cancellable = true)
    private void blockAttack(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        if (!MineAnything.TEST.test(living)) {
            cir.setReturnValue(false);
        }
    }

}

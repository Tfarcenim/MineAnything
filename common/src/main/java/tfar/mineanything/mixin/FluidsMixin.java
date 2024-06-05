package tfar.mineanything.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mineanything.MineAnything;
import tfar.mineanything.init.ModFluids;

@Mixin(Fluids.class)
public abstract class FluidsMixin {

    @Inject(method = "<clinit>",at = @At("RETURN"))
    private static void onFluidRegister(CallbackInfo ci) {
        Registry.register(BuiltInRegistries.FLUID, MineAnything.id("creeper_water"),ModFluids.CREEPER_WATER);
        Registry.register(BuiltInRegistries.FLUID, MineAnything.id("flowing_creeper_water"),ModFluids.FLOWING_CREEPER_WATER);
    }
}

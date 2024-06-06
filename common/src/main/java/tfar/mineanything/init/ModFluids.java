package tfar.mineanything.init;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import tfar.mineanything.platform.Services;

public class ModFluids {
    public static final Fluid FLOWING_CREEPER_WATER = Services.PLATFORM.createCreeperWaterFlowingFluid();
    public static final FlowingFluid CREEPER_WATER = Services.PLATFORM.createCreeperWaterFluid();
}

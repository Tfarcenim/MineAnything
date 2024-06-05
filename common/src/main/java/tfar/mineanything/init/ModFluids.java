package tfar.mineanything.init;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import tfar.mineanything.fluid.CreeperWaterFluid;

public class ModFluids {
    public static final Fluid FLOWING_CREEPER_WATER = new CreeperWaterFluid.Flowing();
    public static final FlowingFluid CREEPER_WATER = new CreeperWaterFluid.Source();
}

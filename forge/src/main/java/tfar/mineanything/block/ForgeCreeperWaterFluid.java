package tfar.mineanything.block;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import tfar.mineanything.fluid.CreeperWaterFluid;

public class ForgeCreeperWaterFluid {

    public static class Flowing extends CreeperWaterFluid.Flowing {

        @Override
        public FluidType getFluidType() {
            return ForgeMod.WATER_TYPE.get();
        }
    }

    public static class Source extends CreeperWaterFluid.Source {

        @Override
        public FluidType getFluidType() {
            return ForgeMod.WATER_TYPE.get();
        }
    }

}

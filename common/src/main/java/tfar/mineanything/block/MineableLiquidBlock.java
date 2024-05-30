package tfar.mineanything.block;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class MineableLiquidBlock extends LiquidBlock {
    public MineableLiquidBlock(FlowingFluid fluid, Properties properties) {
        super(fluid, properties);
    }
}

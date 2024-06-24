package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BluePortalBlock extends NetherPortalBlock {
    public BluePortalBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {

    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext $$0) {
        return super.getStateForPlacement($$0);
    }
}

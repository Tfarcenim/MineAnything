package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.blockentity.VoidBlockEntity;

public class VoidBlock extends Block implements EntityBlock {
    public VoidBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new VoidBlockEntity(blockPos,blockState);
    }
}

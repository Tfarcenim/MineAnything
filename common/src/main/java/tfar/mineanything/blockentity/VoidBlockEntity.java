package tfar.mineanything.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.mineanything.init.ModBlockEntities;

public class VoidBlockEntity extends TheEndPortalBlockEntity {
    public VoidBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public VoidBlockEntity(BlockPos $$1, BlockState $$2) {
        this(ModBlockEntities.VOID,$$1,$$2);
    }

    @Override
    public boolean shouldRenderFace(Direction $$0) {
        return Block.shouldRenderFace(this.getBlockState(), this.level, this.getBlockPos(), $$0, this.getBlockPos().relative($$0));
    }
}

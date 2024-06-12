package tfar.mineanything.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.mineanything.init.ModBlockEntities;

public class FortifiedSpawnerBlockEntity extends SpawnerBlockEntity {
    public FortifiedSpawnerBlockEntity(BlockPos $$0, BlockState $$1) {
        super($$0, $$1);
    }


    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.FORTIFIED_SPAWNER;
    }

    public void destroy() {
        this.level.removeBlock(getBlockPos(),false);
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return false;
    }
}

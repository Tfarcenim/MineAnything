package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import tfar.mineanything.blockentity.FortifiedSpawnerBlockEntity;
import tfar.mineanything.init.ModBlockEntities;

import javax.annotation.Nullable;

public class FortifiedSpawnerBlock extends SpawnerBlock {
    public FortifiedSpawnerBlock(Properties $$0) {
        super($$0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos $$0, BlockState $$1) {
        return new FortifiedSpawnerBlockEntity($$0, $$1);
    }


    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModBlockEntities.FORTIFIED_SPAWNER, pLevel.isClientSide ? SpawnerBlockEntity::clientTick : SpawnerBlockEntity::serverTick);
    }
}

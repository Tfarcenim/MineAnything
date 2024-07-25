package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.portal.PortalShape;
import tfar.mineanything.world.CustomPortalShaper;

import java.util.Optional;

public class BluePortalBlock extends NetherPortalBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BluePortalBlock(Properties $$0) {
        super($$0);
        registerDefaultState(defaultBlockState().setValue(LIT,false));
    }

    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$0.getValue(LIT)) {

        }
    }

    protected final BlockBehaviour.StatePredicate FRAME = (state, p_77721_, p_77722_) -> {
        return state.is(Blocks.REINFORCED_DEEPSLATE);
    };

    protected final CustomPortalShaper shaper = new CustomPortalShaper(FRAME,this);

    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            BlockPos bottom = pPos;
            while (pLevel.getBlockState(bottom).is(this)) {
                bottom = bottom.below();
            }
            bottom = bottom.above();
            Optional<CustomPortalShaper.CustomPortalShape> optional =
                    shaper.findCustomPortalShape(pLevel, bottom, portalShape -> portalShape.isComplete(), Direction.Axis.X);
         //   optional = net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(pLevel, pPos, optional);
            if (optional.isPresent()) {
                System.out.println("Custom Portal detected!");
              optional.get().createPortalBlocks();
                return;
            }

            if (!pState.canSurvive(pLevel, pPos)) {
                pLevel.removeBlock(pPos, false);
            }

        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPosition) {
        Direction.Axis $$6 = direction.getAxis();
        Direction.Axis $$7 = state.getValue(AXIS);
        boolean flag = $$7 != $$6 && $$6.isHorizontal();
        return !flag && !neighborState.is(this) && !shaper.generate(level, pos, $$7).isComplete() ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, level, pos, neighborPosition);
    }


    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource $$3) {
        if (state.getValue(LIT)) {
            super.animateTick(state, level, pos, $$3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> $$0) {
        super.createBlockStateDefinition($$0);
        $$0.add(LIT);
    }
}

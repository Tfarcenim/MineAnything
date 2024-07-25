package tfar.mineanything.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import tfar.mineanything.block.BluePortalBlock;

import java.util.Optional;
import java.util.function.Predicate;

public class CustomPortalShaper {

    private final BlockBehaviour.StatePredicate frame;
    private final Block portal;

    public CustomPortalShaper(BlockBehaviour.StatePredicate frame, Block portal) {
        this.frame = frame;
        this.portal = portal;
    }

    public Optional<CustomPortalShape> findCustomEmptyPortalShape(LevelAccessor pLevel, BlockPos pBottomLeft, Direction.Axis pAxis) {
        return findCustomPortalShape(pLevel, pBottomLeft, shape -> shape.isValid() && shape.numPortalBlocks == 0, pAxis);
    }

    public Optional<CustomPortalShape> findCustomPortalShape(LevelAccessor pLevel, BlockPos pBottomLeft, Predicate<PortalShape> pPredicate, Direction.Axis pAxis) {
        Optional<CustomPortalShape> optional = Optional.of(generate(pLevel, pBottomLeft, pAxis)).filter(pPredicate);
        if (optional.isPresent()) {
            return optional;
        } else {
            Direction.Axis direction$axis = pAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            return Optional.of(generate(pLevel, pBottomLeft, direction$axis)).filter(pPredicate);
        }
    }


    public CustomPortalShape generate(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        return new CustomPortalShape(level, pos, axis);
    }

    public class CustomPortalShape extends PortalShape {
        public CustomPortalShape(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
            super(level, pos, axis);
        }


        @Override
        public void createPortalBlocks() {
            BlockState blockstate = portal.defaultBlockState().setValue(BluePortalBlock.LIT,true).setValue(NetherPortalBlock.AXIS, this.axis);
            BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1)
                            .relative(this.rightDir, this.width - 1))
                    .forEach(pos -> this.level.setBlock(pos, blockstate, 18));
        }

        @Override
        public boolean hasTopFrame(BlockPos.MutableBlockPos pPos, int pDistanceToTop) {
            for(int i = 0; i < this.width; ++i) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = pPos.set(this.bottomLeft).move(Direction.UP, pDistanceToTop).move(this.rightDir, i);
                if (!frame.test(this.level.getBlockState(blockpos$mutableblockpos), this.level, blockpos$mutableblockpos)) {
                    return false;
                }
            }

            return true;
        }

        @Override
        public int getDistanceUntilTop(BlockPos.MutableBlockPos pos) {
            for(int i = 0; i < MAX_HEIGHT; ++i) {
                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, -1);
                if (!frame.test(this.level.getBlockState(pos), this.level, pos)) {
                    return i;
                }

                pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, this.width);
                if (!frame.test(this.level.getBlockState(pos), this.level, pos)) {
                    return i;
                }

                for(int $$2 = 0; $$2 < this.width; ++$$2) {
                    pos.set(this.bottomLeft).move(Direction.UP, i).move(this.rightDir, $$2);
                    BlockState $$3 = this.level.getBlockState(pos);
                    if (!isEmpty($$3)) {
                        return i;
                    }

                    if ($$3.is(portal)) {
                        ++this.numPortalBlocks;
                    }
                }
            }
            return 21;
        }

        @Override
        public int getDistanceUntilEdgeAboveFrame(BlockPos pPos, Direction pDirection) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int i = 0; i <= 21; ++i) {
                blockpos$mutableblockpos.set(pPos).move(pDirection, i);
                BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
                if (!isEmpty(blockstate)) {
                    if (frame.test(blockstate, this.level, blockpos$mutableblockpos)) {
                        return i;
                    }
                    break;
                }

                BlockState blockstate1 = this.level.getBlockState(blockpos$mutableblockpos.move(Direction.DOWN));
                if (!frame.test(blockstate1, this.level, blockpos$mutableblockpos)) {
                    break;
                }
            }

            return 0;
        }

        protected boolean isEmpty(BlockState state) {
            return state.isAir() || state.is(BlockTags.FIRE) || state.is(portal);
        }
    }
}

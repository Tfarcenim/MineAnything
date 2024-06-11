package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MinerZombieEntity extends Zombie {

    private Direction direction;
    private int mined;
    private BlockPos target;

    public MinerZombieEntity(EntityType<? extends Zombie> $$0, Level $$1) {
        super($$0, $$1);
    }

    public MinerZombieEntity(Level $$0) {
        super($$0);
    }

    @Override
    protected void registerGoals() {
    }

    public void advance() {
        mined++;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void setJumping(boolean $$0) {
        //super.setJumping($$0);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {

            if (target == null) {
                target = findClosestBlock(16);
            }

            if (target != null) {
                BlockPos pos = target.relative(direction);
                getNavigation().moveTo(pos.getX()+.5,pos.getY(),pos.getZ()+.5,1);
            }

            if (target == null) {
                double b = 2;
                switch (direction) {
                    case NORTH -> {
                        getNavigation().moveTo(position().x, position().y, position().z - b, 1);
                    }
                    case SOUTH -> {
                        getNavigation().moveTo(position().x, position().y, position().z + b, 1);

                    }
                    case EAST -> {
                        getNavigation().moveTo(position().x + b, position().y, position().z, 1);
                    }
                    case WEST -> {
                        getNavigation().moveTo(position().x - b, position().y, position().z, 1);
                    }
                }
            } else {
                double distSq = distanceToSqr(target.getCenter());
                if (distSq <4.5) {
                    break3x3();
                }
            }
        }

        if (mined > 30) {
            discard();
        }
    }



    public BlockPos findClosestBlock(int max) {
        BlockPos base = blockPosition();
        for (int i = 0; i <max;i++) {
            BlockPos offset = base.relative(direction,i);
            BlockState blockState = level().getBlockState(offset);
            if (!blockState.isAir()) return offset;
        }
        return null;
    }

    void break3x3() {
        BlockPos base = blockPosition();
        BlockPos offset = base.relative(direction);
        level().destroyBlock(offset,true);
        switch (direction) {
            case NORTH,SOUTH -> {
                level().destroyBlock(new BlockPos(offset.offset(-1,0,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(1,0,0)),true);

                level().destroyBlock(new BlockPos(offset.offset(-1,1,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,1,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(1,1,0)),true);

                level().destroyBlock(new BlockPos(offset.offset(-1,2,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,2,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(1,2,0)),true);
            }

            case EAST,WEST -> {
                level().destroyBlock(new BlockPos(offset.offset(0,0,-1)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,0,1)),true);

                level().destroyBlock(new BlockPos(offset.offset(0,1,-1)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,1,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,1,1)),true);

                level().destroyBlock(new BlockPos(offset.offset(0,2,-1)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,2,0)),true);
                level().destroyBlock(new BlockPos(offset.offset(0,2,1)),true);
            }
        }
        target = null;
    }

    boolean isThereABlock() {
        BlockPos base = blockPosition().above();
        BlockPos offset = base.relative(direction);
        BlockState state = level().getBlockState(offset);
        BlockState state1 = level().getBlockState(base.relative(direction,2));
        return !state.isAir() || !state1.isAir();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("direction",direction.ordinal());
        tag.putInt("mined",mined);

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        direction = Direction.values()[tag.getInt("direction")];
        mined = tag.getInt("mined");
    }
}

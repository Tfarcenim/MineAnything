package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MinerZombieEntity extends Zombie {

    private Direction direction;
    private int mined;
    private BlockPos target;

    public MinerZombieEntity(EntityType<? extends Zombie> $$0, Level $$1) {
        super($$0, $$1);
        moveControl = new GolemMoveControl(this);
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
    public void aiStep() {
        super.aiStep();

        if (!level().isClientSide) {

            if (target == null) {
                target = findClosestBlock(16);
            }

            if (target != null) {
                BlockPos pos = target;//.relative(direction.getOpposite());
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

    public static class GolemMoveControl extends MoveControl {
        public GolemMoveControl(Mob pMob) {
            super(pMob);
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.STRAFE) {
                float speed = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                float totalSpeed = (float)this.speedModifier * speed;
                float f2 = this.strafeForwards;
                float f3 = this.strafeRight;
                float strafe = Mth.sqrt(f2 * f2 + f3 * f3);
                if (strafe < 1.0F) {
                    strafe = 1.0F;
                }

                strafe = totalSpeed / strafe;
                f2 *= strafe;
                f3 *= strafe;
                float f5 = Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F));
                float f6 = Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F));
                float f7 = f2 * f6 - f3 * f5;
                float f8 = f3 * f6 + f2 * f5;
                if (!this.isWalkable1(f7, f8)) {
                    this.strafeForwards = 1.0F;
                    this.strafeRight = 0.0F;
                }

                this.mob.setSpeed(totalSpeed);
                this.mob.setZza(this.strafeForwards);
                this.mob.setXxa(this.strafeRight);
                this.operation = MoveControl.Operation.WAIT;
            } else if (this.operation == MoveControl.Operation.MOVE_TO) {
                this.operation = MoveControl.Operation.WAIT;
                double xWantedDist = this.wantedX - this.mob.getX();
                double zWantedDist = this.wantedZ - this.mob.getZ();
                double yWantedDist = this.wantedY - this.mob.getY();
                double wantedDistSq = xWantedDist * xWantedDist + yWantedDist * yWantedDist + zWantedDist * zWantedDist;
                if (wantedDistSq < 2.5000003E-7F) {
                    this.mob.setZza(0.0F);
                    return;
                }

                float f9 = (float)(Mth.atan2(zWantedDist, xWantedDist) * (double)(180F / (float)Math.PI)) - 90;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                BlockPos blockpos = this.mob.blockPosition();
                BlockState blockstate = this.mob.level().getBlockState(blockpos);
                VoxelShape collisionShape = blockstate.getCollisionShape(this.mob.level(), blockpos, CollisionContext.of(mob));//the only change
                if (yWantedDist > .5 && xWantedDist * xWantedDist + zWantedDist * zWantedDist < Math.max(1.0F, this.mob.getBbWidth()) || !collisionShape.isEmpty()
                        && this.mob.getY() < collisionShape.max(Direction.Axis.Y) + blockpos.getY() && !blockstate.is(BlockTags.DOORS)
                        && !blockstate.is(BlockTags.FENCES)) {
                    this.mob.getJumpControl().jump();
                    this.operation = MoveControl.Operation.JUMPING;
                }
            } else if (this.operation == MoveControl.Operation.JUMPING) {
                this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                if (this.mob.onGround()) {
                    this.operation = MoveControl.Operation.WAIT;
                }
            } else {
                this.mob.setZza(0.0F);
            }
        }

        private boolean isWalkable1(float pRelativeX, float pRelativeZ) {
            PathNavigation pathnavigation = this.mob.getNavigation();
            if (pathnavigation != null) {
                NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
                return nodeevaluator == null || nodeevaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double) pRelativeX), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double) pRelativeZ)) == BlockPathTypes.WALKABLE;
            }
            return true;
        }
    }

    public boolean canHarvest(BlockState state) {
        return !state.isAir();
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
    protected PathNavigation createNavigation(Level $$0) {
        return new GroundPathNavigation(this,$$0) {
            @Override
            protected boolean canMoveDirectly(Vec3 $$0, Vec3 $$1) {
                return true;
            }
        };
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        direction = Direction.values()[tag.getInt("direction")];
        mined = tag.getInt("mined");
    }
}

package tfar.mineanything.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.platform.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MinerZombieEntity extends Zombie implements OwnableEntity {

    protected UUID owner;

    private Direction direction;
    private int mined;
    private BlockPos target;

    private ServerPlayer fakePlayer;

    static final UUID uuif = UUID.fromString("8433bdf9-3917-4038-8911-51e6338181c2");

    public MinerZombieEntity(EntityType<? extends Zombie> $$0, Level $$1) {
        super($$0, $$1);
        moveControl = new GolemMoveControl(this);
        if (!$$1.isClientSide) {
            fakePlayer = Services.PLATFORM.makeFakePlayer((ServerLevel) $$1,new GameProfile(uuif,"miner_zombie"));
        }
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

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwnerUUID(UUID owner) {
        this.owner = owner;
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

        List<BlockPos> extraPos = new ArrayList<>();
        extraPos.add(offset);
        switch (direction) {
            case NORTH,SOUTH -> {
                extraPos.add(new BlockPos(offset.offset(-1,0,0)));
                extraPos.add(new BlockPos(offset.offset(1,0,0)));

                extraPos.add(new BlockPos(offset.offset(-1,1,0)));
                extraPos.add(new BlockPos(offset.offset(0,1,0)));
                extraPos.add(new BlockPos(offset.offset(1,1,0)));

                extraPos.add(new BlockPos(offset.offset(-1,2,0)));
                extraPos.add(new BlockPos(offset.offset(0,2,0)));
                extraPos.add(new BlockPos(offset.offset(1,2,0)));
            }

            case EAST,WEST -> {
                extraPos.add(new BlockPos(offset.offset(0,0,-1)));
                extraPos.add(new BlockPos(offset.offset(0,0,1)));

                extraPos.add(new BlockPos(offset.offset(0,1,-1)));
                extraPos.add(new BlockPos(offset.offset(0,1,0)));
                extraPos.add(new BlockPos(offset.offset(0,1,1)));

                extraPos.add(new BlockPos(offset.offset(0,2,-1)));
                extraPos.add(new BlockPos(offset.offset(0,2,0)));
                extraPos.add(new BlockPos(offset.offset(0,2,1)));
            }
        }

        for (BlockPos blockPos : extraPos) {
            BlockState state = level().getBlockState(blockPos);
            if (fakePlayer.hasCorrectToolForDrops(state)) {
                LivingEntity own = getOwner();
                if (own != null) {
                    List<ItemStack> drops = Block.getDrops(state, (ServerLevel) level(), blockPos, level().getBlockEntity(blockPos), fakePlayer, getItemInHand(InteractionHand.MAIN_HAND));
                    for (ItemStack stack : drops) {
                        if (own instanceof Player player) {
                            player.getInventory().add(stack);
                        }
                    }
                    level().destroyBlock(blockPos, false, fakePlayer);
                } else {
                    level().destroyBlock(blockPos, true, fakePlayer);
                }
            }
        }
        advance();
        target = null;
    }

    @Override
    public void setItemInHand(InteractionHand $$0, ItemStack $$1) {
        super.setItemInHand($$0, $$1);
        fakePlayer.setItemInHand($$0,$$1);
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
        return fakePlayer.hasCorrectToolForDrops(state);
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
        if (owner != null) {
            tag.putUUID("owner",owner);
        }

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
        if (tag.hasUUID("owner")) {
            setOwnerUUID(tag.getUUID("owner"));
        }
    }
}

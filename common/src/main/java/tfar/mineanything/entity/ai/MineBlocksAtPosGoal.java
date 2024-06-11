package tfar.mineanything.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.entity.MinerZombieEntity;

public class MineBlocksAtPosGoal extends MoveToBlockGoal {
    private final MinerZombieEntity removerMob;
    private int ticksSinceReachedGoal;
    private static final int WAIT_AFTER_BLOCK_FOUND = 20;

    public MineBlocksAtPosGoal(MinerZombieEntity pRemoverMob, double pSpeedModifier, int pSearchRange) {
        super(pRemoverMob, pSpeedModifier, 24, pSearchRange);
        this.removerMob = pRemoverMob;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        //   if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.removerMob.level(), this.removerMob)) {
        //       return false;
        //   }
        // else


        if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else if (this.findNearestBlock()) {
            this.nextStartTick = reducedTickDelay(WAIT_AFTER_BLOCK_FOUND);
            return true;
        } else {
            this.nextStartTick = this.nextStartTick(this.mob);
            return false;
        }
    }

    protected int nextStartTick(PathfinderMob pCreature) {
        return reducedTickDelay(20 + pCreature.getRandom().nextInt(20));
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();
        this.removerMob.fallDistance = 1.0F;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        super.start();
        this.ticksSinceReachedGoal = 0;
    }

    public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
    }

    public void playBreakSound(Level pLevel, BlockPos pPos) {
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        super.tick();
        Level level = this.removerMob.level();
        RandomSource randomsource = this.removerMob.getRandom();
        if (this.isReachedTarget() && blockPos != BlockPos.ZERO) {
            if (this.ticksSinceReachedGoal > 0) {
                Vec3 vec3 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(vec3.x, 0.3D, vec3.z);
                if (!level.isClientSide) {
                    double d0 = 0.08D;
                    ((ServerLevel) level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockPos)), blockPos.getX() + 0.5D, blockPos.getY() + 0.7D, (double) blockPos.getZ() + 0.5D, 3, ((double) randomsource.nextFloat() - 0.5D) * 0.08D, ((double) randomsource.nextFloat() - 0.5D) * 0.08D, (randomsource.nextFloat() - 0.5D) * 0.08D, 0.15F);
                }
            }

            if (this.ticksSinceReachedGoal % 2 == 0) {
                Vec3 vec31 = this.removerMob.getDeltaMovement();
                this.removerMob.setDeltaMovement(vec31.x, -0.3D, vec31.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound(level, this.blockPos);
                }
            }

            if (this.ticksSinceReachedGoal > 60) {
                level.removeBlock(blockPos, false);
                if (!level.isClientSide) {
                    for (int i = 0; i < 5; ++i) {
                        double d3 = randomsource.nextGaussian() * 0.02D;
                        double d1 = randomsource.nextGaussian() * 0.02D;
                        double d2 = randomsource.nextGaussian() * 0.02D;
                        ((ServerLevel) level).sendParticles(ParticleTypes.POOF, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, 1, d3, d1, d2, 0.15F);
                    }

                    this.playBreakSound(level, blockPos);
                }
                removerMob.advance();
            }

            ++this.ticksSinceReachedGoal;
        }
    }

    //super moves the mob ON TOP of the block, we want to move it NEXT to the block
    @Override
    protected void moveMobToBlock() {

        Direction direction = removerMob.getDirection();
        BlockPos targetPos = blockPos.relative(direction.getOpposite());
        this.mob.getNavigation().moveTo(targetPos.getX()+.5,mob.getY(),targetPos.getZ(),speedModifier);
       // this.mob.getNavigation().moveTo(this.blockPos.getX() + 0.5, this.blockPos.getY() + 1, this.blockPos.getZ() + 0.5, this.speedModifier);
    }

    @Override
    protected boolean findNearestBlock() {
        Direction direction = removerMob.getDirection();
        BlockPos $$2 = this.mob.blockPosition();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        int height = 3;

        switch (direction) {
            case NORTH -> {
                for (int z = 0; z < 16; z++) {
                    for (int y = 1; y < height; y++) {
                        for (int x = 0; x < 3; x++) {
                            mutable.setWithOffset($$2, x, y - 1, -z);
                            if (this.mob.isWithinRestriction(mutable) && this.isValidTarget(this.mob.level(), mutable)) {
                                this.blockPos = mutable;
                                return true;
                            }
                        }
                    }
                }
            }

            case SOUTH -> {
                for (int z = 0; z < 16; z++) {
                    for (int y = 1; y < height; y++) {
                        for (int x = 0; x < 3; x++) {
                            mutable.setWithOffset($$2, x, y - 1, z);
                            if (this.mob.isWithinRestriction(mutable) && this.isValidTarget(this.mob.level(), mutable)) {
                                this.blockPos = mutable;
                                return true;
                            }
                        }
                    }
                }
            }

            case EAST -> {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 1; y < height; y++) {
                            for (int z = 0; z < 3; z++) {
                                mutable.setWithOffset($$2, x, y - 1, z);
                                if (this.mob.isWithinRestriction(mutable) && this.isValidTarget(this.mob.level(), mutable)) {
                                    this.blockPos = mutable;
                                    return true;
                                }
                            }
                        }
                    }
            }

            case WEST -> {
                for (int x = 0; x < 16; x++) {
                    for (int y = 1; y < height; y++) {
                        for (int z = 0; z < 3; z++) {
                            mutable.setWithOffset($$2, -x, y - 1, z);
                            if (this.mob.isWithinRestriction(mutable) && this.isValidTarget(this.mob.level(), mutable)) {
                                this.blockPos = mutable;
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    boolean canTarget(BlockState state) {
        return !state.isAir();
    }

    /**
     * Return {@code true} to set given position as destination
     */
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        ChunkAccess chunkaccess = pLevel.getChunk(SectionPos.blockToSectionCoord(pPos.getX()), SectionPos.blockToSectionCoord(pPos.getZ()), ChunkStatus.FULL, false);
        if (chunkaccess == null) {
            return false;
        } else {
            //if (!chunkaccess.getBlockState(pPos).canEntityDestroy(pLevel, pPos, this.removerMob)) return false;
            return canTarget(chunkaccess.getBlockState(pPos));
        }
    }
}

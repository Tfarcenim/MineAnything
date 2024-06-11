package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.entity.ai.MineBlocksAtPosGoal;

public class MinerZombieEntity extends Zombie {

    private Direction direction;
    private int mined;

    public MinerZombieEntity(EntityType<? extends Zombie> $$0, Level $$1) {
        super($$0, $$1);
    }

    public MinerZombieEntity(Level $$0) {
        super($$0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new MineBlocksAtPosGoal(this, 1.0D, 3));
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
        if (mined > 30) {
            discard();
        }
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

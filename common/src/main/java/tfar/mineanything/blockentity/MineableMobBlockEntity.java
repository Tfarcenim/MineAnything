package tfar.mineanything.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.init.ModBlockEntities;

import java.util.function.Function;

public class MineableMobBlockEntity extends BlockEntity {

    private @Nullable Entity displayEntity;
    CompoundTag data = new CompoundTag();

    public MineableMobBlockEntity(BlockPos $$1, BlockState $$2) {
        super(ModBlockEntities.MINEABLE_MOB, $$1, $$2);
    }

    public void setDisplayEntity(Entity displayEntity) {
        data.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(displayEntity.getType()).toString());
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        setChanged();
    }

    public Entity getOrCreateDisplayEntity() {
        if (displayEntity == null) {
           displayEntity = EntityType.loadEntityRecursive(data, level, Function.identity());
        }
        return displayEntity;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("data", data);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        data = tag.getCompound("data");
    }
}

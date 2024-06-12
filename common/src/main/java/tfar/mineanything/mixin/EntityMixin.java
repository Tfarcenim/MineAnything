package tfar.mineanything.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tfar.mineanything.EntityDuck;

@Mixin(Entity.class)
public class EntityMixin implements EntityDuck {

    @Unique
    private BlockPos spawnerPos;

    @Override
    public BlockPos getSpawnerPos() {
        return spawnerPos;
    }

    @Override
    public void setSpawnerPos(BlockPos spawnerPos) {
        this.spawnerPos = spawnerPos;
    }

}

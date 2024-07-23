package tfar.mineanything.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.Utils;
import tfar.mineanything.init.ModEntities;

public class PrimedLavaTnt extends PrimedTnt {
    
    public static float POWER = 4;
    public PrimedLavaTnt(EntityType<? extends PrimedTnt> $$0, Level $$1) {
        super($$0, $$1);
    }

    public PrimedLavaTnt(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
        this(ModEntities.LAVA_TNT, level);
        this.setPos(x, y, z);
        double radians = level.random.nextDouble() * 2 * Math.PI;
        this.setDeltaMovement(-Math.sin(radians) * 0.02, 0.2, -Math.cos(radians) * 0.02);
        this.setFuse(80);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = owner;
    }

    @Override
    protected void explode() {
        Utils.explode(level(), this, this.getX(), this.getY(0.0625), this.getZ(), POWER, Level.ExplosionInteraction.TNT, Utils.ExplosionType.LAVA);
    }
}

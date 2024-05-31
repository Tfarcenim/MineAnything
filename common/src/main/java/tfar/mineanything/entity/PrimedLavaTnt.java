package tfar.mineanything.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PrimedLavaTnt extends PrimedTnt {
    public PrimedLavaTnt(EntityType<? extends PrimedTnt> $$0, Level $$1) {
        super($$0, $$1);
    }

    public PrimedLavaTnt(Level $$0, double $$1, double $$2, double $$3, @Nullable LivingEntity $$4) {
        super($$0, $$1, $$2, $$3, $$4);
    }
}

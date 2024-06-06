package tfar.mineanything.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import tfar.mineanything.entity.SkeletonArrowEntity;

public class SkeletonBowItem extends BowItem {
    public SkeletonBowItem(Properties $$0) {
        super($$0);
    }

    //forge patch
    public AbstractArrow customArrow(AbstractArrow arrow) {
        SkeletonArrowEntity skeletonArrowEntity = new SkeletonArrowEntity((LivingEntity)arrow.getOwner(), arrow.level());
        return skeletonArrowEntity;
    }


}

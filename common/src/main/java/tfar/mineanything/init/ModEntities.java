package tfar.mineanything.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.entity.PrimedLavaTnt;
import tfar.mineanything.entity.SkeletonArrowEntity;

public class ModEntities {

    public static final EntityType<ClonePlayerEntity> CLONE_PLAYER = EntityType.Builder.of(ClonePlayerEntity::new, MobCategory.MISC).sized(.8f,1.8f).build("");
    public static final EntityType<PrimedLavaTnt> LAVA_TNT = EntityType.Builder.<PrimedLavaTnt>of(PrimedLavaTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build("");
    public static final EntityType<SkeletonArrowEntity> SKELETON_ARROW = EntityType.Builder.<SkeletonArrowEntity>of(SkeletonArrowEntity::new,MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("");
}

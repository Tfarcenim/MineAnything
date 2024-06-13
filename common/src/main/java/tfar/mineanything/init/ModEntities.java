package tfar.mineanything.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.mineanything.entity.*;

public class ModEntities {

    public static final EntityType<ClonePlayerEntity> CLONE_PLAYER = EntityType.Builder.of(ClonePlayerEntity::new, MobCategory.MISC).sized(.8f,1.8f).build("");
    public static final EntityType<PrimedLavaTnt> LAVA_TNT = EntityType.Builder.<PrimedLavaTnt>of(PrimedLavaTnt::new, MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build("");
    public static final EntityType<SkeletonArrowEntity> SKELETON_ARROW = EntityType.Builder.<SkeletonArrowEntity>of(SkeletonArrowEntity::new,MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("");
    public static final EntityType<MinerZombieEntity> MINER_ZOMBIE = EntityType.Builder.<MinerZombieEntity>of(MinerZombieEntity::new,MobCategory.MONSTER)
            .sized(.8f,1.8f).build("");

    public static final EntityType<BedrockBlazeBossEntity> BEDROCK_BLAZE_BOSS = EntityType.Builder.of(BedrockBlazeBossEntity::new, MobCategory.MONSTER).fireImmune().sized(0.6F, 1.8F).clientTrackingRange(8).build("");
    public static final EntityType<BedrockSilverfishEntity> FORTIFIED_SILVERFISH = EntityType.Builder
            .of(BedrockSilverfishEntity::new, MobCategory.MONSTER).sized(2* 0.4F,2* 0.3F).clientTrackingRange(8).build("");

    public static final EntityType<VoidRayEntity> VOID_RAY = EntityType.Builder.<VoidRayEntity>of(VoidRayEntity::new,MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("");

}

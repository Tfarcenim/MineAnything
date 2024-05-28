package tfar.mineanything.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.mineanything.entity.ClonePlayerEntity;

public class ModEntities {

    public static final EntityType<ClonePlayerEntity> CLONE_PLAYER = EntityType.Builder.of(ClonePlayerEntity::new, MobCategory.MISC).sized(.8f,1.8f).build("");
}

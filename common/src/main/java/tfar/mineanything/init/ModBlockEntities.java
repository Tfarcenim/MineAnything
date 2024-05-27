package tfar.mineanything.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.mineanything.blockentity.PlayerBodyBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<PlayerBodyBlockEntity> PLAYER_BODY = BlockEntityType.Builder.of(PlayerBodyBlockEntity::new,ModBlocks.PLAYER_BODY).build(null);

}

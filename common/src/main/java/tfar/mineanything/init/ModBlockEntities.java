package tfar.mineanything.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import tfar.mineanything.blockentity.FortifiedSpawnerBlockEntity;
import tfar.mineanything.blockentity.PlayerBodyBlockEntity;

public class ModBlockEntities {

    public static final BlockEntityType<PlayerBodyBlockEntity> PLAYER_BODY = BlockEntityType.Builder.of(PlayerBodyBlockEntity::new,ModBlocks.PLAYER_BODY).build(null);
    public static final BlockEntityType<FortifiedSpawnerBlockEntity> FORTIFIED_SPAWNER = BlockEntityType.Builder.of(FortifiedSpawnerBlockEntity::new,ModBlocks.FORTIFIED_SPAWNER).build(null);

}

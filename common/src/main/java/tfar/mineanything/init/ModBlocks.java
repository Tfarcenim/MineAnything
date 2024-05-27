package tfar.mineanything.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tfar.mineanything.block.PlayerBodyBlock;

public class ModBlocks {
    public static final Block PLAYER_BODY = new PlayerBodyBlock(BlockBehaviour.Properties.of().noOcclusion());
}

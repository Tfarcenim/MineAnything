package tfar.mineanything.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import tfar.mineanything.block.MineableLiquidBlock;
import tfar.mineanything.block.PlayerBodyBlock;

public class ModBlocks {
    public static final Block PLAYER_BODY = new PlayerBodyBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.COW_BELL).strength(0.5F).speedFactor(0.4F).sound(SoundType.SOUL_SAND).noOcclusion());
    public static final Block MINEABLE_WATER = new MineableLiquidBlock(Fluids.WATER, BlockBehaviour.Properties.copy(Blocks.WATER));

}

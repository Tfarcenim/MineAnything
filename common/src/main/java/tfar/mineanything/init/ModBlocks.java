package tfar.mineanything.init;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.block.LavaTntBlock;
import tfar.mineanything.block.MineableLiquidBlock;
import tfar.mineanything.block.PlayerBodyBlock;

public class ModBlocks {
    public static final Block PLAYER_BODY = new PlayerBodyBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.COW_BELL).strength(0.5F).speedFactor(0.4F).sound(SoundType.SOUL_SAND).noOcclusion());
    public static final Block MINEABLE_WATER = new MineableLiquidBlock(Fluids.WATER, BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().strength(10).pushReaction(PushReaction.DESTROY).liquid().sound(SoundType.EMPTY));
    public static final Block MINEABLE_LAVA = new MineableLiquidBlock(Fluids.LAVA,  BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission().randomTicks().strength(10).lightLevel(state -> 15).pushReaction(PushReaction.DESTROY).liquid().sound(SoundType.EMPTY));
    public static final Block LAVA_TNT = new LavaTntBlock(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).ignitedByLava().isRedstoneConductor(ModBlocks::never));
    public static final Block CREEPER_WATER = new LiquidBlock(ModFluids.CREEPER_WATER,BlockBehaviour.Properties.of().mapColor(MapColor.WATER).replaceable().noCollission().strength(10).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY)){
        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            if (!level.isClientSide) {
                Vec3 boomPos = findPos(pos,level.random);
                level.explode(null,boomPos.x,boomPos.y,boomPos.z,3, Level.ExplosionInteraction.MOB);
            }
        }

        public Vec3 findPos(BlockPos pos, RandomSource randomSource) {
            double x = pos.getX() + 4 * randomSource.nextDouble() - 2;
            double y = pos.getY() + 4 * randomSource.nextDouble() - 2;
            double z = pos.getZ() + 4 * randomSource.nextDouble() - 2;
            return new Vec3(x,y,z);
        }

    };
    private static boolean never(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return false;
    }

}

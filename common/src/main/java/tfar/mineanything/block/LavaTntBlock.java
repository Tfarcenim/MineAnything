package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.entity.PrimedLavaTnt;

public class LavaTntBlock extends TntBlock {
    public LavaTntBlock(Properties properties) {
        super(properties);
    }

    //IFORGEBLOCK METHOD
    /**
     * If the block is flammable, this is called when it gets lit on fire.
     *
     * @param state The current state
     * @param level The current level
     * @param pos Block position in level
     * @param direction The direction that the fire is coming from
     * @param igniter The entity that lit the fire
     */
    public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction direction, @Nullable LivingEntity igniter) {
        customExplode(level,pos,igniter);
    }

    private static void customExplode(Level pLevel, BlockPos pPos, @Nullable LivingEntity pEntity) {
        if (!pLevel.isClientSide) {
            PrimedTnt primedtnt = new PrimedLavaTnt(pLevel, pPos.getX() + 0.5D, pPos.getY(), pPos.getZ() + 0.5D, pEntity);
            pLevel.addFreshEntity(primedtnt);
            pLevel.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent(pEntity, GameEvent.PRIME_FUSE, pPos);
        }
    }
}
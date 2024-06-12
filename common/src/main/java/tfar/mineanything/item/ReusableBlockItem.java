package tfar.mineanything.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class ReusableBlockItem extends BlockItem {
    private final int cooldown;

    public ReusableBlockItem(Block $$0, Properties $$1, int cooldown) {
        super($$0, $$1);
        this.cooldown = cooldown;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext placeContext = this.updatePlacementContext(context);
            if (placeContext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState state = this.getPlacementState(placeContext);
                if (state == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(placeContext, state)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos pos = placeContext.getClickedPos();
                    Level level = placeContext.getLevel();
                    Player player = placeContext.getPlayer();
                    ItemStack stack = placeContext.getItemInHand();
                    BlockState $$7 = level.getBlockState(pos);
                    if ($$7.is(state.getBlock())) {
                        $$7 = this.updateBlockStateFromTag(pos, level, stack, $$7);
                        this.updateCustomBlockEntityTag(pos, level, player, stack, $$7);
                        $$7.getBlock().setPlacedBy(level, pos, $$7, player, stack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, pos, stack);
                        }
                    }

                    SoundType $$8 = $$7.getSoundType();
                    level.playSound(player, pos, this.getPlaceSound($$7), SoundSource.BLOCKS, ($$8.getVolume() + 1.0F) / 2.0F, $$8.getPitch() * 0.8F);
                    level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, $$7));
                    /*if (player == null || !player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }*/

                    player.getCooldowns().addCooldown(this, cooldown);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

}

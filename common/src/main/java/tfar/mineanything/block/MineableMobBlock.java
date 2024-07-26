package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.blockentity.MineableMobBlockEntity;
import tfar.mineanything.entity.DeadDragonEntity;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.init.ModItems;

import java.util.ArrayList;
import java.util.List;

public class MineableMobBlock extends Block implements EntityBlock {
    public MineableMobBlock(Properties $$0) {
        super($$0);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MineableMobBlockEntity(blockPos,blockState);
    }

   /* @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState blockState, boolean pistonPush) {

    }*/

    /**
     * Called when a player removes a block.  This is responsible for
     * actually destroying the block, and the block is intact at time of call.
     * This is called regardless of whether the player can harvest the block or
     * not.
     *
     * Return true if the block is actually destroyed.
     *
     * Note: When used in multiplayer, this is called on both client and
     * server sides!
     *
     * @param state The current state.
     * @param level The current level
     * @param player The player damaging the block, may be null
     * @param pos Block position in level
     * @param willHarvest True if Block.harvestBlock will be called after this, if the return in true.
     *        Can be useful to delay the destruction of tile entities till after harvestBlock
     * @param fluid The current fluid state at current position
     * @return True if the block is actually destroyed.
     */
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (player.isCreative()) {
              playerWillDestroy(level, pos, state, player);
            return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide ? 11 : 3);
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof MineableMobBlockEntity mineableMobBlockEntity) {
            Entity displayEntity = mineableMobBlockEntity.getOrCreateDisplayEntity();
            if (displayEntity instanceof EnderDragon enderDragon && !level.isClientSide) {
                    player.addItem(new ItemStack(ModItems.DRAGON_ELYTRA));
                    DeadDragonEntity newDeadDragonEntity = ModEntities.DEAD_DRAGON.spawn((ServerLevel) level,pos, MobSpawnType.EVENT);
                    newDeadDragonEntity.setDisplayEntity(enderDragon);
            }
        }
        return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide ? 11 : 3);
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     * @deprecated call via {@link net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase#getRenderShape}
     * whenever possible. Implementing/overriding is fine.
     */
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    //faster than trying to use datagen
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        LootParams params = builder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
        ServerLevel level = params.getLevel();
        Vec3 vec3 = params.getParameter(LootContextParams.ORIGIN);
        BlockPos pos = BlockPos.containing(vec3);
        BlockEntity blockEntity = params.getParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof MineableMobBlockEntity mineableMobBlockEntity) {
            Entity entity = mineableMobBlockEntity.getOrCreateDisplayEntity();
            if (entity != null) {
                EntityType<?> type = entity.getType();
                if (type ==EntityType.ZOMBIE || type == EntityType.HUSK) {
                    drops.add(new ItemStack(Items.ZOMBIE_HEAD));
                }
                else if (type == EntityType.STRAY || type == EntityType.SKELETON) {
                    drops.add(new ItemStack(Items.SKELETON_SKULL));
                }

                else if (type == EntityType.CREEPER) {
                    drops.add(new ItemStack(Items.CREEPER_HEAD));
                }

                else if (type == EntityType.PIGLIN) {
                    drops.add(new ItemStack(Items.PIGLIN_HEAD));
                } else if (type == ModEntities.DEAD_DRAGON) {
                    drops.add(new ItemStack(ModItems.DRAGON_ELYTRA));
                }

            }
        }
        return drops;
    }
}

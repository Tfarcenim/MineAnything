package tfar.mineanything.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.blockentity.MineableMobBlockEntity;

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
                }

            }
        }
        return drops;
    }
}

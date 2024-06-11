package tfar.mineanything.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import tfar.mineanything.entity.MinerZombieEntity;
import tfar.mineanything.init.ModEntities;

public class ZombieSwordItem extends SwordItem {
    public ZombieSwordItem(Tier $$0, int $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (!level.isClientSide) {
            MinerZombieEntity minerZombieEntity = ModEntities.MINER_ZOMBIE.spawn((ServerLevel) level,pos.above(), MobSpawnType.EVENT);
            minerZombieEntity.setDirection(context.getHorizontalDirection());
            minerZombieEntity.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.NETHERITE_PICKAXE));
        }

        return InteractionResult.SUCCESS;
    }
}

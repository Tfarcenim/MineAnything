package tfar.mineanything.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.entity.VoidRayEntity;
import tfar.mineanything.init.ModEntities;

public class VoidRayItem extends Item {
    public VoidRayItem(Properties $$0) {
        super($$0);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            VoidRayEntity voidRayEntity = new VoidRayEntity(player,look.x,look.y,look.z,level);
            level.addFreshEntity(voidRayEntity);
        }

        return InteractionResultHolder.sidedSuccess(stack,level.isClientSide);
    }
}

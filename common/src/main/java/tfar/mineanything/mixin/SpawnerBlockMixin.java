package tfar.mineanything.mixin;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import tfar.mineanything.MineAnything;

import java.util.List;

@Mixin(SpawnerBlock.class)
public class SpawnerBlockMixin extends Block {

    public SpawnerBlockMixin(Properties $$0) {
        super($$0);
    }

    @Override
    public List<ItemStack> getDrops(BlockState $$0, LootParams.Builder builder) {
        List<ItemStack> drops1 = super.getDrops($$0, builder);
        MineAnything.modifySpawnerDrops(drops1,$$0,builder);
        return drops1;
    }
}

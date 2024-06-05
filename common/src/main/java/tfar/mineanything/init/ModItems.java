package tfar.mineanything.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import tfar.mineanything.block.LavaTntBlock;

public class ModItems {

    public static final BlockItem MINEABLE_WATER = new BlockItem(ModBlocks.MINEABLE_WATER,new Item.Properties());
    public static final BlockItem MINEABLE_LAVA = new BlockItem(ModBlocks.MINEABLE_LAVA,new Item.Properties().fireResistant());
    public static final BlockItem LAVA_TNT = new BlockItem(ModBlocks.LAVA_TNT,new Item.Properties());


}

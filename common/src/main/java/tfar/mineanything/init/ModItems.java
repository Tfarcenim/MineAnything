package tfar.mineanything.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ModItems {

    public static final BlockItem MINEABLE_WATER = new BlockItem(ModBlocks.MINEABLE_WATER,new Item.Properties());
    public static final BlockItem MINEABLE_LAVA = new BlockItem(ModBlocks.MINEABLE_LAVA,new Item.Properties().fireResistant());

}

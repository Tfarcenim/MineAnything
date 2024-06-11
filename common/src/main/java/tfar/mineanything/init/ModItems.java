package tfar.mineanything.init;

import net.minecraft.world.item.*;
import tfar.mineanything.item.SkeletonBowItem;
import tfar.mineanything.item.ZombieSwordItem;

public class ModItems {

    public static final Item PICKAXE = new PickaxeItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant()){};
    public static final BlockItem MINEABLE_WATER = new BlockItem(ModBlocks.MINEABLE_WATER,new Item.Properties());
    public static final BlockItem MINEABLE_LAVA = new BlockItem(ModBlocks.MINEABLE_LAVA,new Item.Properties().fireResistant());
    public static final BlockItem LAVA_TNT = new BlockItem(ModBlocks.LAVA_TNT,new Item.Properties());
    public static final Item CREEPER_WATER_BUCKET = new BucketItem(ModFluids.CREEPER_WATER,new Item.Properties());
    public static final Item SKELETON_BOW = new SkeletonBowItem(new Item.Properties().durability(1000));
    public static final Item ZOMBIE_SWORD = new ZombieSwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
    public static final Item FORTIFIED_SPAWNER = new BlockItem(ModBlocks.FORTIFIED_SPAWNER,new Item.Properties());

}

package tfar.mineanything.init;

import net.minecraft.world.item.*;
import tfar.mineanything.item.ReusableBlockItem;
import tfar.mineanything.item.SkeletonBowItem;
import tfar.mineanything.item.VoidRayItem;
import tfar.mineanything.item.ZombieSwordItem;

public class ModItems {

    public static final Item PICKAXE = new PickaxeItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant()){};
    public static final BlockItem MINEABLE_WATER = new BlockItem(ModBlocks.MINEABLE_WATER,new Item.Properties());
    public static final BlockItem MINEABLE_LAVA = new BlockItem(ModBlocks.MINEABLE_LAVA,new Item.Properties().fireResistant());
    public static final BlockItem LAVA_TNT = new BlockItem(ModBlocks.LAVA_TNT,new Item.Properties());
    public static final Item CREEPER_WATER_BUCKET = new BucketItem(ModFluids.CREEPER_WATER,new Item.Properties());
    public static final Item SKELETON_BOW = new SkeletonBowItem(new Item.Properties().durability(1000));
    public static final Item ZOMBIE_SWORD = new ZombieSwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
    public static final Item FORTIFIED_SPAWNER = new ReusableBlockItem(ModBlocks.FORTIFIED_SPAWNER,new Item.Properties(),200);

    public static final Item BEDROCK_BLAZE_SPAWN_EGG = new SpawnEggItem(ModEntities.BEDROCK_BLAZE_BOSS,0,0,new Item.Properties());
    public static final Item FORTIFIED_SILVERFISH_SPAWN_EGG = new SpawnEggItem(ModEntities.FORTIFIED_SILVERFISH,0,0,new Item.Properties());

    public static final Item VOID_RAY = new VoidRayItem(new Item.Properties());
    public static final ElytraItem DRAGON_ELYTRA = new ElytraItem(new Item.Properties().durability(432*4).rarity(Rarity.RARE));

}

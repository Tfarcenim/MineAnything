package tfar.mineanything.init;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.item.*;
import tfar.mineanything.platform.Services;

public class ModItems {

    public static final Item PICKAXE = new NamedPickaxeItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
    public static final BlockItem MINEABLE_WATER = new BlockItem(ModBlocks.MINEABLE_WATER,new Item.Properties());
    public static final BlockItem MINEABLE_LAVA = new BlockItem(ModBlocks.MINEABLE_LAVA,new Item.Properties().fireResistant());
    public static final BlockItem LAVA_TNT = new BlockItem(ModBlocks.LAVA_TNT,new Item.Properties());
    public static final Item CREEPER_BUCKET = new CreeperBucketItem(Fluids.EMPTY,new Item.Properties());
    public static final Item CREEPER_WATER_BUCKET = new CreeperBucketItem(ModFluids.CREEPER_WATER,new Item.Properties());
    public static final Item SKELETON_BOW = new SkeletonBowItem(new Item.Properties().durability(1000));
    public static final Item ZOMBIE_SWORD = new ZombieSwordItem(Tiers.NETHERITE, 3, -2.4F, new Item.Properties().fireResistant());
    public static final Item FORTIFIED_SPAWNER = new ReusableBlockItem(ModBlocks.FORTIFIED_SPAWNER,new Item.Properties(),200);

    public static final Item BEDROCK_BLAZE_SPAWN_EGG = new SpawnEggItem(ModEntities.BEDROCK_BLAZE_BOSS,0,0,new Item.Properties());
    public static final Item FORTIFIED_SILVERFISH_SPAWN_EGG = new SpawnEggItem(ModEntities.FORTIFIED_SILVERFISH,0,0,new Item.Properties());

    public static final Item VOID_RAY = new VoidRayItem(new Item.Properties());
    public static final DragonElytraItem DRAGON_ELYTRA = new DragonElytraItem(new Item.Properties().durability(432*4).rarity(Rarity.RARE));
    public static final ShieldItem FORTIFIED_SHIELD = Services.PLATFORM.fortifiedShield(new Item.Properties());

    public static final BlockItem NETHER_PORTAL = new BlockItem(Blocks.NETHER_PORTAL,new Item.Properties()){
        @Nullable
        @Override
        protected BlockState getPlacementState(BlockPlaceContext context) {
            if (isNextToDeepSlate(context.getLevel(),context.getClickedPos())) {
                return ModBlocks.BLUE_PORTAL.getStateForPlacement(context);
            }
            return super.getPlacementState(context);
        }

        public static boolean isNextToDeepSlate(Level level,BlockPos pos) {
            for (Direction direction : Direction.values()) {
                BlockPos pos1 = pos.relative(direction);
                BlockState state = level.getBlockState(pos1);
                if (state.is(Blocks.REINFORCED_DEEPSLATE)) return true;
            }
            return false;
        }

    };

    public static final BlockItem VOID = new BlockItem(ModBlocks.VOID,new Item.Properties());

}

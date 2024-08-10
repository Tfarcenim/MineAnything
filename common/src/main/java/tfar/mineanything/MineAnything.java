package tfar.mineanything;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.mineanything.blockentity.FortifiedSpawnerBlockEntity;
import tfar.mineanything.blockentity.MineableMobBlockEntity;
import tfar.mineanything.blockentity.PlayerBodyBlockEntity;
import tfar.mineanything.init.*;
import tfar.mineanything.mixin.BlockBehaviorAccess;
import tfar.mineanything.mixin.BlockStateBaseAccess;
import tfar.mineanything.mixin.TargetingConditionsAccess;
import tfar.mineanything.network.PacketHandler;
import tfar.mineanything.platform.Services;
import tfar.mineanything.platform.Side;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class MineAnything {

    public static final String MOD_ID = "mineanything";
    public static final String MOD_NAME = "MineAnything";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static Side SIDE;
    public static void preInit() {
        SIDE = Services.PLATFORM.getSide();
        Services.PLATFORM.registerAll(ModEntities.class, BuiltInRegistries.ENTITY_TYPE, EntityType.class);

        //Services.PLATFORM.registerAll(ModFluids.class, BuiltInRegistries.FLUID, Fluid.class);
        Services.PLATFORM.registerAll(ModBlocks.class, BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.registerAll(ModItems.class, BuiltInRegistries.ITEM, Item.class);
        Services.PLATFORM.registerAll(ModBlockEntities.class, BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.class);
        Services.PLATFORM.registerAll(ModEnchantments.class, BuiltInRegistries.ENCHANTMENT, Enchantment.class);
        Services.PLATFORM.registerAll(ModRecipeSerializers.class, BuiltInRegistries.RECIPE_SERIALIZER, RecipeSerializer.class);
        Services.PLATFORM.registerAll(ModCreativeTabs.class, BuiltInRegistries.CREATIVE_MODE_TAB, CreativeModeTab.class);
        ((BlockBehaviorAccess)Blocks.BEDROCK).setDrops(null);
    }

    public static void setup() {
        PacketHandler.registerPackets();
        setDestroySpeed(Blocks.NETHER_PORTAL,1);
        setDestroySpeed(Blocks.BEDROCK,100);
    }

    public static void setDestroySpeed(Block block,float speed) {
        ImmutableList<BlockState> possibleStates = block.getStateDefinition().getPossibleStates();

        for (BlockState blockState : possibleStates) {
            ((BlockStateBaseAccess)blockState).setDestroySpeed(speed);
        }
    }

    public static void onDeath(LivingEntity entity, DamageSource source) {
        if (entity instanceof ServerPlayer serverPlayer) {

            if (source.getEntity() instanceof Player playerAttacker) {
                PlayerDuck playerDuck = PlayerDuck.of(playerAttacker);
                if (!playerDuck.isRunner()) return;
            }

            Level level = serverPlayer.serverLevel();
            level.setBlock(serverPlayer.blockPosition(),ModBlocks.PLAYER_BODY.defaultBlockState(),3);
            if (level.getBlockEntity(serverPlayer.blockPosition()) instanceof PlayerBodyBlockEntity playerBodyBlockEntity) {
                playerBodyBlockEntity.setGameProfile(serverPlayer.getGameProfile());
            }
        }

        BlockPos spawnerPos = ((EntityDuck)entity).getSpawnerPos();
        if (spawnerPos != null) {
            BlockEntity blockEntity = entity.level().getBlockEntity(spawnerPos);
            if (blockEntity instanceof FortifiedSpawnerBlockEntity fortifiedSpawnerBlockEntity) {
                fortifiedSpawnerBlockEntity.destroy();
            }
        }
    }

    public static void onDamage(LivingEntity entity, DamageSource source) {
        Entity attacker = source.getEntity();
        if (attacker instanceof Player player && entity instanceof Mob) {
            PlayerDuck playerDuck = PlayerDuck.of(player);
            if (!playerDuck.isRunner()) return;
            ItemStack stack = ((Player) attacker).getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.is(ModItems.PICKAXE)) {

                if (entity instanceof EnderDragon enderDragon) {
                    BlockPos spawnPos = entity.blockPosition().offset(0,0,4);
                    entity.level().setBlock(spawnPos,ModBlocks.MINEABLE_MOB.defaultBlockState(),3);

                    BlockEntity blockEntity = entity.level().getBlockEntity(spawnPos);
                    if (blockEntity instanceof MineableMobBlockEntity mineableMobBlockEntity) {
                        mineableMobBlockEntity.setDisplayEntity(entity);
                    }
                    FallingBlockEntity fallingBlockEntity = FallingBlockEntity.fall(entity.level(),spawnPos,ModBlocks.MINEABLE_MOB.defaultBlockState());
                    fallingBlockEntity.blockData = blockEntity.saveWithoutMetadata();
                    // DeadDragonEntity deadDragonEntity = ModEntities.DEAD_DRAGON.spawn((ServerLevel) entity.level(),spawnPos, MobSpawnType.SPAWN_EGG);
                    //deadDragonEntity.setDisplayEntity(enderDragon);
                    entity.discard();
                    EndDragonFight dragonFight = enderDragon.getDragonFight();
                    if (dragonFight != null) {
                        dragonFight.updateDragon(enderDragon);
                        dragonFight.setDragonKilled(enderDragon);
                    }

                    return;
                }


                entity.level().setBlock(entity.blockPosition(),ModBlocks.MINEABLE_MOB.defaultBlockState(),3);

                BlockEntity blockEntity = entity.level().getBlockEntity(entity.blockPosition());
                if (blockEntity instanceof MineableMobBlockEntity mineableMobBlockEntity) {
                    mineableMobBlockEntity.setDisplayEntity(entity);
                }

                entity.discard();
            }
        }
    }

    public static final Predicate<LivingEntity> TEST = living -> !living.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.ZOMBIE_SWORD) && !living.getItemInHand(InteractionHand.OFF_HAND).is(ModItems.ZOMBIE_SWORD);

    public static void modifyTargetingConditions(TargetingConditions conditions, Class<?> clazz) {
        if (clazz.isAssignableFrom(Player.class)) {

            Predicate<LivingEntity> oldSelector = ((TargetingConditionsAccess)conditions).getSelector();


            Predicate<LivingEntity> newSelector = oldSelector != null  ? oldSelector.and(TEST) : TEST;

            conditions.selector(newSelector);
        }
    }

    public static void modifySpawnerDrops(List<ItemStack> drops, BlockState state, LootParams.Builder builder) {
            BlockEntity blockEntity = builder.getParameter(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof SpawnerBlockEntity spawnerBlockEntity) {
                BaseSpawner spawner = spawnerBlockEntity.getSpawner();
                Entity displayEntity = spawner.getOrCreateDisplayEntity(blockEntity.getLevel(),blockEntity.getLevel().random,blockEntity.getBlockPos());
                if (displayEntity != null) {
                    drops.add(displayEntity.getPickResult().copyWithCount(16));
                }
            }

    }

    public static void playerTick(Player player){
        PlayerDuck playerDuck = PlayerDuck.of(player);
        if (playerDuck.getCloneCooldown() > 0) {
            playerDuck.setCloneCooldown(playerDuck.getCloneCooldown() - 1);
        }
    }

    public static void copyFrom(ServerPlayer oldPlayer,ServerPlayer newPlayer,boolean alive) {
        PlayerDuck oldPlayerDuck = PlayerDuck.of(oldPlayer);
        PlayerDuck newPlayerDuck = PlayerDuck.of(newPlayer);
        newPlayerDuck.setRunner(oldPlayerDuck.isRunner());
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(MineAnything.MOD_ID));
    }

}
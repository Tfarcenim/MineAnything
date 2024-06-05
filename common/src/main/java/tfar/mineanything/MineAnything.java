package tfar.mineanything;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tfar.mineanything.blockentity.PlayerBodyBlockEntity;
import tfar.mineanything.init.*;
import tfar.mineanything.network.PacketHandler;
import tfar.mineanything.platform.Services;
import tfar.mineanything.platform.Side;

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
        //Services.PLATFORM.registerAll(ModFluids.class, BuiltInRegistries.FLUID, Fluid.class);
        Services.PLATFORM.registerAll(ModBlocks.class, BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.registerAll(ModItems.class, BuiltInRegistries.ITEM, Item.class);
        Services.PLATFORM.registerAll(ModBlockEntities.class, BuiltInRegistries.BLOCK_ENTITY_TYPE, BlockEntityType.class);
        Services.PLATFORM.registerAll(ModEntities.class, BuiltInRegistries.ENTITY_TYPE, EntityType.class);

    }

    public static void setup() {
        PacketHandler.registerPackets();
    }

    public static void onDeath(LivingEntity entity, DamageSource source) {
        if (entity instanceof ServerPlayer serverPlayer) {
            Level level = serverPlayer.serverLevel();
            level.setBlock(serverPlayer.blockPosition(),ModBlocks.PLAYER_BODY.defaultBlockState(),3);
            if (level.getBlockEntity(serverPlayer.blockPosition()) instanceof PlayerBodyBlockEntity playerBodyBlockEntity) {
                playerBodyBlockEntity.setGameProfile(serverPlayer.getGameProfile());
            }
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

}
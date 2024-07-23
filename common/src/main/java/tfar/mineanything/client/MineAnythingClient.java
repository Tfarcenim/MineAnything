package tfar.mineanything.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SpawnerRenderer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.HasFakeItems;
import tfar.mineanything.MineAnything;
import tfar.mineanything.client.render.*;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.init.*;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;

import java.util.List;
import java.util.Map;

public class MineAnythingClient {

    public static void clientTick() {
        List<ModKeybinds.ModKeybind> keys = ModKeybinds.KEYS;
        for (ModKeybinds.ModKeybind key : keys) {
            if (key.consumeClick()) {
                key.onPress.run();
            }
        }
    }

    public static Material REINFORCED_BASE;

    public static void clientSetup() {
        ModKeybinds.KEYS.forEach(Services.PLATFORM::registerKeyBinding);

        ItemProperties.register(ModItems.SKELETON_BOW, new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });

        ItemProperties.register(ModItems.SKELETON_BOW, new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });

        ItemProperties.register(ModItems.PICKAXE, MineAnything.id("level"), new ClampedItemPropertyFunction() {
            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MINE_ANYTHING, itemStack);
                return enchantmentLevel;
            }

            @Override
            public float call(ItemStack $$0, @Nullable ClientLevel $$1, @Nullable LivingEntity $$2, int $$3) {
                return unclampedCall($$0, $$1, $$2, $$3);
            }
        });

        Services.PLATFORM.registerRenderLayer(ModFluids.CREEPER_WATER,RenderType.translucent());
        Services.PLATFORM.registerRenderLayer(ModFluids.FLOWING_CREEPER_WATER,RenderType.translucent());

        Services.PLATFORM.registerRenderLayer(ModBlocks.FORTIFIED_SPAWNER, RenderType.cutoutMipped());
        Services.PLATFORM.registerRenderLayer(ModBlocks.POINTED_BEDROCK,RenderType.cutoutMipped());
    }

    public static void spawnPingParticles() {
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;
        if (level != null && player != null) {


            int particleCount = 32;

            for (int i = 0; i < particleCount;i++) {

                double angle = 360d * i/particleCount;

                double x = Math.cos(angle * Math.PI /180);
                double z = Math.sin(angle * Math.PI /180);

                //   public void addParticle(ParticleOptions pParticleData, boolean pForceAlwaysRender,
                //   double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
                level.addParticle(ParticleTypes.END_ROD, false, player.getX(), player.getY()+1, player.getZ(), x, 0, z);
            }
            Services.PLATFORM.sendToServer(new C2SKeyActionPacket(C2SKeyActionPacket.Action.PING));
        }
    }

    public static ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.getInsecureSkinInformation(gameProfile);
        return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? skinManager.registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) :
                DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile));
    }

    private static final Map<String, EntityRendererProvider<ClonePlayerEntity>> CLONE_PROVIDERS = ImmutableMap.of(
            "default", (context) -> new ClonePlayerEntityRenderer(context, false),
            "slim", (context) -> new ClonePlayerEntityRenderer(context, true));



    public static void registerRenderers() {
        BlockEntityRenderers.register(ModBlockEntities.PLAYER_BODY, PlayerBodyBlockEntityRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.FORTIFIED_SPAWNER, SpawnerRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.MINEABLE_MOB,MineableMobBlockEntityRenderer::new);
        EntityRenderers.register(ModEntities.LAVA_TNT, LavaTntRenderer::new);
        EntityRenderers.register(ModEntities.SKELETON_ARROW, SkeletonArrowRenderer::new);
        EntityRenderers.register(ModEntities.MINER_ZOMBIE, ZombieRenderer::new);
        EntityRenderers.register(ModEntities.BEDROCK_BLAZE_BOSS, BedrockBlazeBossRenderer::new);
        EntityRenderers.register(ModEntities.FORTIFIED_SILVERFISH,BedrockSilverFishRenderer::new);
        EntityRenderers.register(ModEntities.VOID_RAY,VoidRayRenderer::new);
        EntityRenderers.register(ModEntities.DEAD_DRAGON,DeadDragonEntityRenderer::new);
        //EntityRenderers.register(ModEntities.CLONE_PLAYER, context -> new ClonePlayerEntityRenderer(context,false));
    }

    public static Map<String, EntityRenderer<ClonePlayerEntity>> createCloneRenderers(EntityRendererProvider.Context context) {
        ImmutableMap.Builder<String, EntityRenderer<ClonePlayerEntity>> builder = ImmutableMap.builder();
        CLONE_PROVIDERS.forEach((s, provider) -> {
            try {
                builder.put(s, provider.create(context));
            } catch (Exception var5) {
                throw new IllegalArgumentException("Failed to create player model for " + s, var5);
            }
        });
        return builder.build();
    }

    public static void itemColors(ItemColors itemColors) {
        itemColors.register((itemStack, i) -> {return 0x6666ff;}, ModItems.MINEABLE_WATER);
    }

    public static void blockColors(BlockColors blockColors) {
        blockColors.register(($$0x, $$1, $$2, $$3) -> $$1 != null && $$2 != null ? BiomeColors.getAverageWaterColor($$1, $$2) : -1, ModBlocks.CREEPER_WATER);
    }


    public static void setFakeClientEquipment(int entityId, List<Pair<EquipmentSlot, ItemStack>> slots) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity instanceof HasFakeItems hasFakeItems) {
            slots.forEach((pair) -> hasFakeItems.setFakeItemSlot(pair.getFirst(), pair.getSecond()));
        }
    }

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

}

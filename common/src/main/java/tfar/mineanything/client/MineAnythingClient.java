package tfar.mineanything.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.HasFakeItems;
import tfar.mineanything.MineAnything;
import tfar.mineanything.client.render.ClonePlayerEntityRenderer;
import tfar.mineanything.client.render.LavaTntRenderer;
import tfar.mineanything.client.render.PlayerBodyBlockEntityRenderer;
import tfar.mineanything.client.render.SkeletonArrowRenderer;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.entity.MinerZombieEntity;
import tfar.mineanything.init.ModBlockEntities;
import tfar.mineanything.init.ModEnchantments;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.init.ModItems;
import tfar.mineanything.network.server.C2SKeyActionPacket;
import tfar.mineanything.platform.Services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MineAnythingClient {

    public static void clientTick() {
        List<ModKeybinds.ModKeybind> keys = ModKeybinds.KEYS;
        for (ModKeybinds.ModKeybind key : keys) {
            if (key.consumeClick()) {
                key.onPress.run();
            }
        }
    }


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

    public static ResourceLocation lookupSkin(UUID profile) {
        if (profile == null) profile = Util.NIL_UUID;
        GameProfile profile1 = new GameProfile(profile,null);
        return getPlayerSkin(profile1);
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
        EntityRenderers.register(ModEntities.LAVA_TNT, LavaTntRenderer::new);
        EntityRenderers.register(ModEntities.SKELETON_ARROW, SkeletonArrowRenderer::new);
        EntityRenderers.register(ModEntities.MINER_ZOMBIE, ZombieRenderer::new);
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

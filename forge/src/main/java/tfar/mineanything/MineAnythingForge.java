package tfar.mineanything;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.mineanything.blockentity.FortifiedSpawnerBlockEntity;
import tfar.mineanything.client.MineAnythingClientForge;
import tfar.mineanything.datagen.Datagen;
import tfar.mineanything.entity.BedrockBlazeBossEntity;
import tfar.mineanything.entity.BedrockSilverfishEntity;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.platform.Side;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(MineAnything.MOD_ID)
public class MineAnythingForge {
    
    public MineAnythingForge() {
        MineAnything.preInit();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::register);
        bus.addListener(this::setup);
        bus.addListener(this::attributes);
        bus.addListener(Datagen::gather);
        MinecraftForge.EVENT_BUS.addListener(this::onDeath);
        MinecraftForge.EVENT_BUS.addListener(this::onDamage);
        MinecraftForge.EVENT_BUS.addListener(this::onSpawn);
        if (MineAnything.SIDE == Side.CLIENT) {
            MineAnythingClientForge.init(bus);
        }
    }
    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();
    private void register(RegisterEvent e) {
        for (Map.Entry<Registry<?>,List<Pair<ResourceLocation, Supplier<?>>>> entry : registerLater.entrySet()) {
            Registry<?> registry = entry.getKey();
            List<Pair<ResourceLocation, Supplier<?>>> toRegister = entry.getValue();
            for (Pair<ResourceLocation,Supplier<?>> pair : toRegister) {
                e.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(),(Supplier<Object>)pair.getValue());
            }
        }
    }

    private void setup(FMLCommonSetupEvent event) {
        registerLater.clear();
        MineAnything.setup();
    }

    private void onSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getSpawnType() == MobSpawnType.SPAWNER) {
            BaseSpawner baseSpawner = event.getSpawner();
            if (baseSpawner != null) {
                BlockEntity blockEntity = baseSpawner.getSpawnerBlockEntity();
                if (blockEntity instanceof FortifiedSpawnerBlockEntity fortifiedSpawnerBlockEntity) {
                    ((EntityDuck)event.getEntity()).setSpawnerPos(fortifiedSpawnerBlockEntity.getBlockPos());
                }
            }
        }
    }

    private void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CLONE_PLAYER, ClonePlayerEntity.createAttributes().build());
        event.put(ModEntities.MINER_ZOMBIE, Zombie.createAttributes().build());
        event.put(ModEntities.BEDROCK_BLAZE_BOSS, BedrockBlazeBossEntity.createBossAttributes().build());
        event.put(ModEntities.FORTIFIED_SILVERFISH, BedrockSilverfishEntity.createBossAttributes().build());
    }

    private void onDeath(LivingDeathEvent event) {
        MineAnything.onDeath(event.getEntity(),event.getSource());
    }

    private void onDamage(LivingDamageEvent event) {
        MineAnything.onDamage(event.getEntity(),event.getSource());
    }

}
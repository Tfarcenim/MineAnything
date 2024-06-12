package tfar.mineanything.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import tfar.mineanything.init.ModBlocks;

import java.util.HashSet;
import java.util.Set;

public class BedrockSilverfishEntity extends Silverfish {
    public BedrockSilverfishEntity(EntityType<? extends Silverfish> $$0, Level $$1) {
        super($$0, $$1);
    }

    protected Set<BlockPos> bedrockSpikes = new HashSet<>();
    protected int timer = 100;

    public static AttributeSupplier.Builder createBossAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && !isDeadOrDying()) {
            summonTempBedrockSpikes();
        }
        return hurt;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean b = super.doHurtTarget(target);
        if (b) {
            if (target instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 6 * 20, 0), this);
            }
        }
        return b;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (timer > 0) {
            timer--;
        } else if (!bedrockSpikes.isEmpty()) {
            removeBedrockSpikes();
        }
    }

    void summonTempBedrockSpikes() {
        for (int i = 0; i < 8;i++) {
            BlockPos spikePos = getRandomPositionWithin(5);
            if (spikePos != null) {
                level().setBlock(spikePos.above(), ModBlocks.POINTED_BEDROCK.defaultBlockState().setValue(PointedDripstoneBlock.THICKNESS, DripstoneThickness.TIP),3);
                level().setBlock(spikePos, ModBlocks.POINTED_BEDROCK.defaultBlockState().setValue(PointedDripstoneBlock.THICKNESS, DripstoneThickness.FRUSTUM),3);
                bedrockSpikes.add(spikePos);
            }
        }
        timer = 100;
    }

    @Override
    public void die(DamageSource $$0) {
        super.die($$0);
        removeBedrockSpikes();
    }

    void removeBedrockSpikes() {
        bedrockSpikes.forEach(pos -> {
            level().destroyBlock(pos, false);
            level().destroyBlock(pos.above(), false);
        });
        bedrockSpikes.clear();
    }

    BlockPos getRandomPositionWithin(int r) {
        int randX = random.nextInt(2*r+1) - r;
        int randZ = random.nextInt(2*r+1) - r;
        for (int i = r-1;i > -r;i--) {
            BlockPos pos = new BlockPos(blockPosition().getX() +randX,blockPosition().getY() + i,blockPosition().getZ() + randZ);
            BlockState state = level().getBlockState(pos);
            if (state.canBeReplaced() && Block.canSupportCenter(level(),pos.below(), Direction.UP)) {
                return pos;
            }
        }
        return null;//new BlockPos(blockPosition().getX() +randX,blockPosition().getY(),blockPosition().getZ() + randZ);
    }
}

package tfar.mineanything.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.*;
import tfar.mineanything.init.ModBlocks;
import tfar.mineanything.init.ModFluids;
import tfar.mineanything.init.ModItems;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class CreeperWaterFluid extends FlowingFluid {
        public CreeperWaterFluid() {
        }

        public Fluid getFlowing() {
            return ModFluids.FLOWING_CREEPER_WATER;
        }

        public Fluid getSource() {
            return ModFluids.CREEPER_WATER;
        }

        public Item getBucket() {
            return ModItems.CREEPER_WATER_BUCKET;
        }

        public void animateTick(Level level, BlockPos pos, FluidState $$2, RandomSource $$3) {
            if (!$$2.isSource() && !$$2.getValue(FALLING)) {
                if ($$3.nextInt(64) == 0) {
                    level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, $$3.nextFloat() * 0.25F + 0.75F, $$3.nextFloat() + 0.5F, false);
                }
            } else if ($$3.nextInt(10) == 0) {
                level.addParticle(ParticleTypes.UNDERWATER, pos.getX() + $$3.nextDouble(), pos.getY() + $$3.nextDouble(), pos.getZ() + $$3.nextDouble(), 0.0, 0.0, 0.0);
            }

        }

        @Nullable
        public ParticleOptions getDripParticle() {
            return ParticleTypes.DRIPPING_WATER;
        }

        protected boolean canConvertToSource(Level level) {
            return false;
        }

        protected void beforeDestroyingBlock(LevelAccessor $$0, BlockPos $$1, BlockState $$2) {
            BlockEntity $$3 = $$2.hasBlockEntity() ? $$0.getBlockEntity($$1) : null;
            Block.dropResources($$2, $$0, $$1, $$3);
        }

        public int getSlopeFindDistance(LevelReader $$0) {
            return 4;
        }

        public BlockState createLegacyBlock(FluidState $$0) {
            return ModBlocks.CREEPER_WATER.defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel($$0));
        }

        public boolean isSame(Fluid $$0) {
            return $$0 == ModFluids.CREEPER_WATER || $$0 == ModFluids.FLOWING_CREEPER_WATER;
        }

        public int getDropOff(LevelReader $$0) {
            return 1;
        }

        public int getTickDelay(LevelReader $$0) {
            return 5;
        }

        public boolean canBeReplacedWith(FluidState $$0, BlockGetter $$1, BlockPos $$2, Fluid $$3, Direction $$4) {
            return $$4 == Direction.DOWN && !$$3.is(FluidTags.WATER);
        }

        protected float getExplosionResistance() {
            return 100.0F;
        }

        public Optional<SoundEvent> getPickupSound() {
            return Optional.of(SoundEvents.BUCKET_FILL);
        }

        public static class Flowing extends CreeperWaterFluid {
            public Flowing() {
            }

            protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> $$0) {
                super.createFluidStateDefinition($$0);
                $$0.add(LEVEL);
            }

            public int getAmount(FluidState $$0) {
                return $$0.getValue(LEVEL);
            }

            public boolean isSource(FluidState $$0) {
                return false;
            }
        }

        public static class Source extends CreeperWaterFluid{
            public Source() {
            }

            public int getAmount(FluidState $$0) {
                return 8;
            }

            public boolean isSource(FluidState $$0) {
                return true;
            }
        }
    }


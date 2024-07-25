package tfar.mineanything.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Supplier;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviorAccessForge {
    @Accessor @Mutable
    void setLootTableSupplier(Supplier<ResourceLocation> lootTableSupplier);
}

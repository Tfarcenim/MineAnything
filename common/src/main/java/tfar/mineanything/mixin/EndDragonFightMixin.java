package tfar.mineanything.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mineanything.EndDragonDuck;

@Mixin(EndDragonFight.class)
public abstract class EndDragonFightMixin implements EndDragonDuck {

    @Shadow
    protected abstract void spawnExitPortal(boolean $$0);

    @Shadow
    protected abstract void spawnNewGateway();

    @Shadow
    private boolean previouslyKilled;
    @Shadow
    @Final
    private ServerLevel level;
    @Shadow
    @Final
    private BlockPos origin;
    @Shadow
    private boolean dragonKilled;
    boolean part2;

    @Override
    public boolean isPart2() {
        return part2;
    }

    @Override
    public void setPart2(boolean part2) {
        this.part2 = part2;
    }


    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/dimension/end/EndDragonFight;spawnExitPortal(Z)V"), cancellable = true)
    private void startPart2(EnderDragon $$0, CallbackInfo ci) {
        part2 = true;
        ci.cancel();
    }

    @Override
    public void endPart2() {
        this.spawnExitPortal(true);
        this.spawnNewGateway();
        if (!this.previouslyKilled) {
            this.level.setBlockAndUpdate(this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.getLocation(this.origin)), Blocks.DRAGON_EGG.defaultBlockState());
        }

        this.previouslyKilled = true;
        this.dragonKilled = true;
        part2 = false;
    }
}

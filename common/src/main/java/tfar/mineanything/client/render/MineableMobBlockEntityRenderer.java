package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;
import tfar.mineanything.blockentity.MineableMobBlockEntity;
import tfar.mineanything.mixin.DragonModelAccess;
import tfar.mineanything.mixin.EnderDragonRenderAccess;

public class MineableMobBlockEntityRenderer implements BlockEntityRenderer<MineableMobBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public MineableMobBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.entityRenderer = pContext.getEntityRenderer();
    }

    public void render(MineableMobBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.0F, 0.5F);

        Entity entity = pBlockEntity.getOrCreateDisplayEntity();
        if (entity != null) {

            if (entity instanceof EnderDragon enderDragon) {
                boolean showWings = pBlockEntity.isWings();
                EnderDragonRenderer entityRenderer1 = (EnderDragonRenderer) entityRenderer.getRenderer(enderDragon);
                EnderDragonRenderer.DragonModel dragonModel = ((EnderDragonRenderAccess)entityRenderer1).getModel();
                DragonModelAccess dragonModelAccess = (DragonModelAccess) dragonModel;
                dragonModelAccess.getRightWing().visible = showWings;
                dragonModelAccess.getLeftWing().visible = showWings;
            }

            this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }

        pPoseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(MineableMobBlockEntity $$0) {
        return true;
    }

    @Override
    public boolean shouldRender(MineableMobBlockEntity pBlockEntity, Vec3 pCameraPos) {
        return true;//Vec3.atCenterOf(pBlockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D).closerThan(pCameraPos.multiply(1.0D, 0.0D, 1.0D), this.getViewDistance());
    }
}

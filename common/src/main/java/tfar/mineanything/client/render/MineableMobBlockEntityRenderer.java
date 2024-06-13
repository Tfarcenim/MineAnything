package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import tfar.mineanything.blockentity.MineableMobBlockEntity;

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
            this.entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        }

        pPoseStack.popPose();
    }
}

package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import tfar.mineanything.entity.DeadDragonEntity;

public class DeadDragonEntityRenderer extends EntityRenderer<DeadDragonEntity> {
    public DeadDragonEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(DeadDragonEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.translate(0.5F, 0.0F, 0.5F);

        Entity entity = pEntity.getOrCreateDisplayEntity();
        if (entity != null) {
            entityRenderDispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }

        pPoseStack.popPose();
    }

        @Override
    public ResourceLocation getTextureLocation(DeadDragonEntity deadDragonEntity) {
        return null;
    }
}
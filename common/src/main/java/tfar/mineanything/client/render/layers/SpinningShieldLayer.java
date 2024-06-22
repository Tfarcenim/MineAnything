package tfar.mineanything.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tfar.mineanything.init.ModItems;

public class SpinningShieldLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T,M> {



    private final ItemInHandRenderer itemInHandRenderer;

    public SpinningShieldLayer(RenderLayerParent<T, M> pRenderer, ItemInHandRenderer pItemInHandRenderer) {
        super(pRenderer);
        this.itemInHandRenderer = pItemInHandRenderer;
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = new ItemStack(ModItems.FORTIFIED_SHIELD);
        pPoseStack.pushPose();
        if (this.getParentModel().young) {
            float f = 0.5F;
            pPoseStack.translate(0.0F, 0.75F, 0.0F);
            pPoseStack.scale(0.5F, 0.5F, 0.5F);
        }


        this.renderArmWithItem(pLivingEntity, itemstack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, pPoseStack,pAgeInTicks, pBuffer, pPackedLight);
        pPoseStack.popPose();
    }

    protected void renderArmWithItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack,float pAgeInTicks, MultiBufferSource pBuffer, int pPackedLight) {
        if (!pItemStack.isEmpty()) {
            int shields = 3;
            for (int i = 0; i <shields;i++) {
                pPoseStack.pushPose();
                pPoseStack.mulPose(Axis.YP.rotationDegrees(i * 360f/shields));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(12 * pAgeInTicks));
                //pPoseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
               // pPoseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                pPoseStack.translate(1 / 2F, 4 / 8F, -10 / 8f);
                this.itemInHandRenderer.renderItem(pLivingEntity, pItemStack, pDisplayContext, false, pPoseStack, pBuffer, pPackedLight);
                pPoseStack.popPose();
            }
        }
    }


    @Override
    public ResourceLocation getTextureLocation(T t) {
        return null;
    }
}

package tfar.mineanything.client.render.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import tfar.mineanything.HasFakeItems;
import tfar.mineanything.platform.Services;

public class FakeHumanoidArmorLayer<T extends LivingEntity & HasFakeItems, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends HumanoidArmorLayer<T,M,A> {
    public FakeHumanoidArmorLayer(RenderLayerParent<T, M> $$0, A $$1, A $$2, ModelManager $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Override
    public void renderArmorPiece(PoseStack pPoseStack, MultiBufferSource pBuffer, T pLivingEntity, EquipmentSlot pSlot, int pPackedLight, A pModel) {
        ItemStack itemstack = pLivingEntity.getFakeItemBySlot(pSlot);
        Item $$9 = itemstack.getItem();
        if ($$9 instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == pSlot) {
                this.getParentModel().copyPropertiesTo(pModel);
                this.setPartVisibility(pModel, pSlot);
                net.minecraft.client.model.Model model = getArmorModelHookML(pLivingEntity,itemstack,pSlot,pModel);
                boolean flag = this.usesInnerModel(pSlot);
                if (armoritem instanceof DyeableLeatherItem) {
                    int i = ((DyeableLeatherItem)armoritem).getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    this.renderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, f, f1, f2, this.getArmorResourceML(pLivingEntity, itemstack, pSlot, null));
                    this.renderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F, this.getArmorResourceML(pLivingEntity, itemstack, pSlot, "overlay"));
                } else {
                    this.renderModel(pPoseStack, pBuffer, pPackedLight, armoritem, model, flag, 1.0F, 1.0F, 1.0F,this.getArmorResourceML(pLivingEntity, itemstack, pSlot, null) );
                }

                ArmorTrim.getTrim(pLivingEntity.level().registryAccess(), itemstack).ifPresent((p_289638_) -> {
                    this.renderTrim(armoritem.getMaterial(), pPoseStack, pBuffer, pPackedLight, p_289638_, model, flag);
                });
                if (itemstack.hasFoil()) {
                    this.renderGlint(pPoseStack, pBuffer, pPackedLight, model);
                }

            }
        }
    }

    //forge patch
    private void renderModel(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorItem pArmorItem, net.minecraft.client.model.Model pModel, boolean pWithGlint, float pRed, float pGreen, float pBlue, ResourceLocation armorResource) {
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.armorCutoutNoCull(armorResource));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, pRed, pGreen, pBlue, 1.0F);
    }

    //forge patch
    private void renderTrim(ArmorMaterial pArmorMaterial, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, ArmorTrim pTrim, net.minecraft.client.model.Model pModel, boolean pInnerTexture) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(pInnerTexture ? pTrim.innerTexture(pArmorMaterial) : pTrim.outerTexture(pArmorMaterial));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(pBuffer.getBuffer(Sheets.armorTrimsSheet()));
        pModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    //forge patch
    private void renderGlint(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, net.minecraft.client.model.Model pModel) {
        pModel.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.armorEntityGlint()), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    public ResourceLocation getArmorResourceML(T pLivingEntity, ItemStack itemstack, EquipmentSlot pSlot, String string) {
        return Services.PLATFORM.getArmorResource(this,pLivingEntity,itemstack,pSlot,string);
    }

    protected net.minecraft.client.model.Model getArmorModelHookML(T entity, ItemStack itemStack, EquipmentSlot slot, A model) {
        return Services.PLATFORM.getForgeModel(entity,itemStack,slot,model);
    }
}

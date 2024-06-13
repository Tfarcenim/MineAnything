package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tfar.mineanything.entity.VoidRayEntity;

public class VoidRayRenderer extends EntityRenderer<VoidRayEntity> {
    public VoidRayRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public ResourceLocation getTextureLocation(VoidRayEntity voidRayEntity) {
        return null;
    }

    static final ItemStack item = Items.BLACK_CONCRETE.getDefaultInstance();

    @Override
    public void render(VoidRayEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        Minecraft.getInstance().getItemRenderer().renderStatic(item, ItemDisplayContext.FIXED,0,0,poseStack,buffer,entity.level(),0);
    }

}

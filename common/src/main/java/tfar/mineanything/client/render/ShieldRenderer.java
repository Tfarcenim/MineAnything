package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Holder;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import tfar.mineanything.client.MineAnythingClient;

import java.util.List;

public class ShieldRenderer extends BlockEntityWithoutLevelRenderer {
    public ShieldRenderer(BlockEntityRenderDispatcher $$0, EntityModelSet $$1) {
        super($$0, $$1);
    }


    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, int $$5) {

        boolean $$25 = BlockItem.getBlockEntityData(stack) != null;
        $$2.pushPose();
        $$2.scale(1.0F, -1.0F, -1.0F);
        Material material = $$25 ? ModelBakery.SHIELD_BASE : MineAnythingClient.REINFORCED_BASE;
        ShieldModel shieldModel = getModel();
        VertexConsumer $$27 = material.sprite().wrap(ItemRenderer.getFoilBufferDirect($$3, shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
        shieldModel.handle().render($$2, $$27, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
        shieldModel.plate().render($$2, $$27, $$4, $$5, 1.0F, 1.0F, 1.0F, 1.0F);
        $$2.popPose();
    }

    public static ShieldModel getModel() {
        return Minecraft.getInstance().getBlockRenderer().blockEntityRenderer.shieldModel;
    }

}

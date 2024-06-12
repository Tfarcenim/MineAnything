package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SilverfishRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Silverfish;
import tfar.mineanything.MineAnything;

public class BedrockSilverFishRenderer extends SilverfishRenderer {
    public BedrockSilverFishRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Override
    public ResourceLocation getTextureLocation(Silverfish $$0) {
        return MineAnything.id("textures/entity/fortified_silverfish.png");
    }

    @Override
    protected void scale(Silverfish $$0, PoseStack poseStack, float $$2) {
        poseStack.scale(2,2,2);
    }
}

package tfar.mineanything.client.render;

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
}

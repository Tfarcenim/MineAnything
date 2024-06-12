package tfar.mineanything.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BlazeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.monster.Blaze;
import tfar.mineanything.client.render.layers.SpinningShieldLayer;

public class BedrockBlazeBossRenderer extends BlazeRenderer {
    public BedrockBlazeBossRenderer(EntityRendererProvider.Context context) {
        super(context);
        addLayer(new SpinningShieldLayer<>(this,context.getItemInHandRenderer()));
    }

    @Override
    public void render(Blaze $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        super.render($$0, $$1, $$2, $$3, $$4, $$5);
    }
}

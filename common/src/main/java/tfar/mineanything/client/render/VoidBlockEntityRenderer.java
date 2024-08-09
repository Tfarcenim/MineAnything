package tfar.mineanything.client.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import tfar.mineanything.blockentity.VoidBlockEntity;

public class VoidBlockEntityRenderer extends TheEndPortalRenderer<VoidBlockEntity> {

    public VoidBlockEntityRenderer(BlockEntityRendererProvider.Context p_173683_) {
        super(p_173683_);
    }

    @Override
    protected float getOffsetUp() {
        return 1.0F;
    }

    @Override
    protected float getOffsetDown() {
        return 0.0F;
    }

    @Override
    protected RenderType renderType() {
        return RenderType.endGateway();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
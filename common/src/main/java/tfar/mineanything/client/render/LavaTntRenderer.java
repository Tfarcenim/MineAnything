package tfar.mineanything.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import tfar.mineanything.init.ModBlocks;

public class LavaTntRenderer extends CustomTntRenderer{
    public LavaTntRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public BlockState getBlockState() {
        return ModBlocks.LAVA_TNT.defaultBlockState();
    }
}

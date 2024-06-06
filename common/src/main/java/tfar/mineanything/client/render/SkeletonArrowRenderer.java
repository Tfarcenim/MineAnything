package tfar.mineanything.client.render;

import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import tfar.mineanything.entity.SkeletonArrowEntity;

public class SkeletonArrowRenderer extends ArrowRenderer<SkeletonArrowEntity> {


        public SkeletonArrowRenderer(EntityRendererProvider.Context $$0) {
            super($$0);
        }

        public ResourceLocation getTextureLocation(SkeletonArrowEntity $$0) {
            return TippableArrowRenderer.NORMAL_ARROW_LOCATION;
        }
    }

package tfar.mineanything.client.render.layers;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import tfar.mineanything.MineAnything;
import tfar.mineanything.init.ModItems;

public class DragonElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T,M> {

    static final ResourceLocation TEXTURE = MineAnything.id("textures/entity/dragon_elytra.png");

    public DragonElytraLayer(RenderLayerParent<T, M> $$0, EntityModelSet $$1) {
        super($$0, $$1);
    }


    //forge patches
    /**
     * Determines if the ElytraLayer should render.
     * ItemStack and Entity are provided for modder convenience,
     * For example, using the same ElytraLayer for multiple custom Elytra.
     *
     * @param stack  The Elytra ItemStack
     * @param entity The entity being rendered.
     * @return If the ElytraLayer should render.
     */
    public boolean shouldRender(ItemStack stack, T entity) {
        return stack.getItem() == ModItems.DRAGON_ELYTRA;
    }

    /**
     * Gets the texture to use with this ElytraLayer.
     * This assumes the vanilla Elytra model.
     *
     * @param stack  The Elytra ItemStack.
     * @param entity The entity being rendered.
     * @return The texture.
     */
    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        return TEXTURE;
    }

}

package tfar.mineanything.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import tfar.mineanything.init.ModRecipeSerializers;

public class CopySpawnerTypeShapelessRecipe extends ShapelessRecipe {
    public CopySpawnerTypeShapelessRecipe(ShapelessRecipe recipe) {
        super(recipe.getId(), "spawner",recipe.category(), recipe.getResultItem(null), recipe.getIngredients());
    }

    @Override
    public ItemStack assemble(CraftingContainer $$0, RegistryAccess $$1) {
        ItemStack stack = super.assemble($$0,$$1);

        ItemStack spawner = findSpawner($$0);

        if (!spawner.isEmpty()) {
            CompoundTag tag = spawner.getTag();
            if (tag != null) {
                CompoundTag beTag = tag.getCompound(BlockItem.BLOCK_ENTITY_TAG);
                stack.getOrCreateTag().put(BlockItem.BLOCK_ENTITY_TAG,beTag);
            }
        }

        return stack;
    }

    public ItemStack findSpawner(CraftingContainer craftingContainer) {
        for (int i = 0; i < craftingContainer.getContainerSize();i++) {
            ItemStack stack = craftingContainer.getItem(i);
            if (stack.is(Items.SPAWNER))return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.FORTIFIED_SPAWNER;
    }
}

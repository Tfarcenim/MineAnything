package tfar.mineanything.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import tfar.mineanything.init.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LAVA_TNT).requires(Items.TNT)
                .requires(ModItems.MINEABLE_LAVA)
                .unlockedBy(getHasName(ModItems.MINEABLE_LAVA),has(ModItems.MINEABLE_LAVA))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CREEPER_WATER_BUCKET).requires(Items.WATER_BUCKET)
                .requires(Items.CREEPER_HEAD)
                .unlockedBy(getHasName(Items.CREEPER_HEAD),has(Items.CREEPER_HEAD))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.SKELETON_BOW).requires(Items.BOW)
                .requires(Items.SKELETON_SKULL)
                .unlockedBy(getHasName(Items.SKELETON_SKULL),has(Items.SKELETON_SKULL))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ModItems.ZOMBIE_SWORD).requires(ItemTags.SWORDS)
                .requires(Items.ZOMBIE_HEAD)
                .unlockedBy(getHasName(Items.ZOMBIE_HEAD),has(Items.ZOMBIE_HEAD))
                .save(pWriter);
    }
}

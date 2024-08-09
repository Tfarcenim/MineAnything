package tfar.mineanything.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import tfar.mineanything.init.ModBlocks;
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CREEPER_WATER_BUCKET).requires(ModItems.MINEABLE_WATER)
                .requires(Items.CREEPER_HEAD)
                .requires(Items.BUCKET)
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

        SpawnerRecipeBuilder.shapeless(RecipeCategory.TOOLS,ModItems.FORTIFIED_SPAWNER).requires(Items.SPAWNER).requires(Items.BEDROCK)
                .unlockedBy(getHasName(Items.BEDROCK),has(Items.BEDROCK)).save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT,ModItems.VOID_RAY)
                .define('o', Blocks.OBSIDIAN)
                .define('v', ModBlocks.VOID)
                .pattern("oov")
                .pattern("o  ")
                .unlockedBy(getHasName(ModBlocks.VOID),has(ModBlocks.VOID))
                .save(pWriter);
    }
}

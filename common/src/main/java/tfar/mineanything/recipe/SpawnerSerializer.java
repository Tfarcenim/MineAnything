package tfar.mineanything.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nonnull;

public class SpawnerSerializer extends ShapelessRecipe.Serializer {
    @Override
    public CopySpawnerTypeShapelessRecipe fromJson(ResourceLocation location, JsonObject json) {
        return new CopySpawnerTypeShapelessRecipe(super.fromJson(location, json));
    }


    @Override
    public CopySpawnerTypeShapelessRecipe fromNetwork(@Nonnull ResourceLocation p_199426_1_, FriendlyByteBuf p_199426_2_) {
        return new CopySpawnerTypeShapelessRecipe(super.fromNetwork(p_199426_1_, p_199426_2_));
    }
}
package tfar.mineanything.datagen;

import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import tfar.mineanything.init.ModItems;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelGenerators {

    public ModItemModelProvider(BiConsumer<ResourceLocation, Supplier<JsonElement>> pOutput) {
        super(pOutput);
    }

    @Override
    public void run() {
        this.generateFluidItem(ModItems.MINEABLE_WATER, ModelTemplates.FLAT_ITEM,"water");
        this.generateFluidItem(ModItems.MINEABLE_LAVA, ModelTemplates.FLAT_ITEM,"lava");
        this.generateFlatItem(ModItems.CREEPER_WATER_BUCKET,ModelTemplates.FLAT_ITEM);
    }



    public void generateFluidItem(Item pItem, ModelTemplate pModelTemplate,String fluid) {
        pModelTemplate.create(ModelLocationUtils.getModelLocation(pItem), TextureMapping.layer0(new ResourceLocation(fluid+"_still").withPrefix("block/")), this.output);
    }

    public static ResourceLocation getModelLocation(Item pItem) {
        ResourceLocation resourcelocation = BuiltInRegistries.ITEM.getKey(pItem);
        return resourcelocation.withPrefix("item/");
    }


}

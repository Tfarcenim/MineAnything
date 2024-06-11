package tfar.mineanything.datagen;

import com.google.gson.JsonElement;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import tfar.mineanything.init.ModBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("skull"), Blocks.SOUL_SAND).createWithoutBlockItem(ModBlocks.PLAYER_BODY);
        this.createNonTemplateModelBlock(ModBlocks.MINEABLE_WATER,Blocks.WATER);
        this.createNonTemplateModelBlock(ModBlocks.MINEABLE_LAVA,Blocks.LAVA);
        this.createTrivialBlock(ModBlocks.LAVA_TNT, TexturedModel.CUBE_TOP_BOTTOM);
        this.createNonTemplateModelBlock(ModBlocks.CREEPER_WATER,Blocks.WATER);
        this.createTrivialBlock(ModBlocks.FORTIFIED_SPAWNER,TexturedModel.CUBE_TOP_BOTTOM);
    }


}

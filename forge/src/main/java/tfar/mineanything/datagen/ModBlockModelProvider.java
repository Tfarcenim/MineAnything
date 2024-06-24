package tfar.mineanything.datagen;

import com.google.gson.JsonElement;
import net.minecraft.core.Direction;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import tfar.mineanything.MineAnything;
import tfar.mineanything.init.ModBlocks;
import tfar.mineanything.init.ModItems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModBlockModelProvider extends BlockModelGenerators {

    public ModBlockModelProvider(Consumer<BlockStateGenerator> pBlockStateOutput, BiConsumer<ResourceLocation, Supplier<JsonElement>> pModelOutput, Consumer<Item> pSkippedAutoModelsOutput) {
        super(pBlockStateOutput, pModelOutput, pSkippedAutoModelsOutput);
    }

    @Override
    public void run() {
        this.blockEntityModels(ModelLocationUtils.decorateBlockModelLocation("skull"), Blocks.SOUL_SAND).createWithoutBlockItem(ModBlocks.PLAYER_BODY,ModBlocks.MINEABLE_MOB);
        this.createNonTemplateModelBlock(ModBlocks.MINEABLE_WATER,Blocks.WATER);
        this.createNonTemplateModelBlock(ModBlocks.MINEABLE_LAVA,Blocks.LAVA);
        this.createTrivialBlock(ModBlocks.LAVA_TNT, TexturedModel.CUBE_TOP_BOTTOM);
        this.createNonTemplateModelBlock(ModBlocks.CREEPER_WATER,Blocks.WATER);
        this.createTrivialBlock(ModBlocks.FORTIFIED_SPAWNER,TexturedModel.CUBE_TOP_BOTTOM);
        this.delegateItemModel(ModItems.BEDROCK_BLAZE_SPAWN_EGG, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
        this.delegateItemModel(ModItems.FORTIFIED_SILVERFISH_SPAWN_EGG, ModelLocationUtils.decorateItemModelLocation("template_spawn_egg"));
        createBluePortalBlock();
        this.createPointedBedrockDripstone();
    }

    public void createBluePortalBlock() {
        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(ModBlocks.BLUE_PORTAL)
                .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_AXIS)
                        .select(Direction.Axis.X, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(ModBlocks.BLUE_PORTAL, "_ns")))
                        .select(Direction.Axis.Z, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(ModBlocks.BLUE_PORTAL, "_ew")))));
    }


    public void createPointedBedrockDripstone() {
        this.skipAutoItemBlock(ModBlocks.POINTED_BEDROCK);
        PropertyDispatch.C2<Direction, DripstoneThickness> c2 = PropertyDispatch.properties(BlockStateProperties.VERTICAL_DIRECTION, BlockStateProperties.DRIPSTONE_THICKNESS);

        for(DripstoneThickness dripstonethickness : DripstoneThickness.values()) {
            c2.select(Direction.UP, dripstonethickness, this.createPointedDripstoneVariant1(Direction.UP, dripstonethickness));
        }

        for(DripstoneThickness dripstonethickness1 : DripstoneThickness.values()) {
            c2.select(Direction.DOWN, dripstonethickness1, this.createPointedDripstoneVariant1(Direction.DOWN, dripstonethickness1));
        }

        this.blockStateOutput.accept(MultiVariantGenerator.multiVariant(ModBlocks.POINTED_BEDROCK).with(c2));
    }


    public Variant createPointedDripstoneVariant1(Direction pDirection, DripstoneThickness pDripstoneThickness) {
        String s = "_" + pDirection.getSerializedName() + "_" + pDripstoneThickness.getSerializedName();
        TextureMapping texturemapping = TextureMapping.cross(MineAnything.id("block/pointed_bedrock"+s));
        return Variant.variant().with(VariantProperties.MODEL, ModelTemplates.POINTED_DRIPSTONE.createWithSuffix(ModBlocks.POINTED_BEDROCK, s, texturemapping, this.modelOutput));
    }


}

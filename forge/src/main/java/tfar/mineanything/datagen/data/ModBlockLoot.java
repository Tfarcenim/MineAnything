package tfar.mineanything.datagen.data;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import tfar.mineanything.MineAnything;
import tfar.mineanything.datagen.Datagen;
import tfar.mineanything.init.ModBlocks;
import tfar.mineanything.init.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ModBlockLoot extends VanillaBlockLoot {

    @Override
    protected void generate() {
        this.add(ModBlocks.PLAYER_BODY, (p_250163_) ->
                LootTable.lootTable().withPool(this.applyExplosionCondition(p_250163_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(Items.PLAYER_HEAD).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                .copy("GameProfile", "SkullOwner")
                                .copy("note_block_sound", String.format(Locale.ROOT, "%s.%s", "BlockEntityTag", "note_block_sound")))))));

        dropSelf(ModBlocks.MINEABLE_WATER);
        dropSelf(ModBlocks.MINEABLE_LAVA);
        this.add(ModBlocks.LAVA_TNT, LootTable.lootTable()
                .withPool(this.applyExplosionCondition(ModBlocks.LAVA_TNT, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModBlocks.LAVA_TNT).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.LAVA_TNT).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TntBlock.UNSTABLE, false)))))));
        this.add(ModBlocks.FORTIFIED_SPAWNER,noDrop());
        this.add(ModBlocks.POINTED_BEDROCK,noDrop());
        this.add(ModBlocks.MINEABLE_MOB,noDrop());
        this.dropOther(Blocks.NETHER_PORTAL, ModItems.NETHER_PORTAL);
        this.dropSelf(Blocks.REINFORCED_DEEPSLATE);
        dropSelf(Blocks.BEDROCK);
        this.add(ModBlocks.BLUE_PORTAL,noDrop());
        dropSelf(ModBlocks.VOID);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> blocks = new ArrayList<>();
        blocks.add(Blocks.NETHER_PORTAL);
        blocks.add(Blocks.REINFORCED_DEEPSLATE);
        blocks.add(Blocks.BEDROCK);
        blocks.addAll(MineAnything.getKnownBlocks().toList());
        return blocks;
    }
}

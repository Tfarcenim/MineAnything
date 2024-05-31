package tfar.mineanything.datagen.data;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import tfar.mineanything.datagen.Datagen;
import tfar.mineanything.init.ModBlocks;

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
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return Datagen.getKnownBlocks().toList();
    }
}

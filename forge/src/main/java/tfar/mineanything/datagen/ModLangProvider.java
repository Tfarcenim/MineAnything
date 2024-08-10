package tfar.mineanything.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import org.codehaus.plexus.util.StringUtils;
import tfar.mineanything.MineAnything;
import tfar.mineanything.init.ModBlocks;
import tfar.mineanything.init.ModEnchantments;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.init.ModItems;

import java.util.function.Supplier;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, MineAnything.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addBlock(() -> ModBlocks.MINEABLE_WATER,"Water");
        addBlock(() -> ModBlocks.MINEABLE_LAVA,"Lava");
        addDefaultItem(() -> ModItems.CREEPER_WATER_BUCKET);
        addDefaultItem(() -> ModItems.CREEPER_BUCKET);
        addDefaultItem(() -> ModItems.SKELETON_BOW);
        addDefaultItem(() -> ModItems.ZOMBIE_SWORD);
        addDefaultItem(() -> ModItems.PICKAXE);
        addDefaultItem(() -> ModItems.VOID_RAY);
        addDefaultItem(() -> ModItems.BEDROCK_BLAZE_SPAWN_EGG);
        addDefaultItem(() -> ModItems.FORTIFIED_SILVERFISH_SPAWN_EGG);
        addDefaultItem(() -> ModItems.DRAGON_ELYTRA);
        addDefaultBlock(() -> ModBlocks.VOID);

        addDefaultBlock(() -> ModBlocks.LAVA_TNT);
        addDefaultBlock(() -> ModBlocks.PLAYER_BODY);
        addDefaultEntityType(() -> ModEntities.CLONE_PLAYER);
        addDefaultEnchantment(() -> ModEnchantments.MINE_ANYTHING);
        addDefaultBlock(() -> ModBlocks.FORTIFIED_SPAWNER);
    }


    protected void addDefaultItem(Supplier<? extends Item> supplier) {
        addItem(supplier,getNameFromItem(supplier.get()));
    }

    protected void addDefaultBlock(Supplier<? extends Block> supplier) {
        addBlock(supplier,getNameFromBlock(supplier.get()));
    }

    protected void addDefaultEnchantment(Supplier<? extends Enchantment> supplier) {
        addEnchantment(supplier,getNameFromEnchantment(supplier.get()));
    }

    protected void addDefaultEntityType(Supplier<EntityType<?>> supplier) {
        addEntityType(supplier,getNameFromEntity(supplier.get()));
    }

    public static String getNameFromItem(Item item) {
        return StringUtils.capitaliseAllWords(item.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromBlock(Block block) {
        return StringUtils.capitaliseAllWords(block.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEnchantment(Enchantment enchantment) {
        return StringUtils.capitaliseAllWords(enchantment.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

    public static String getNameFromEntity(EntityType<?> entity) {
        return StringUtils.capitaliseAllWords(entity.getDescriptionId().split("\\.")[2].replace("_", " "));
    }

}

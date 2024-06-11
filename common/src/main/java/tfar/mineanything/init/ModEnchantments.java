package tfar.mineanything.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import tfar.mineanything.enchantment.BasicEnchantment;

public class ModEnchantments {

    public static final Enchantment MINE_ANYTHING = new BasicEnchantment(new BasicEnchantment.Properties(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND)
            .maxLevel(4).setTreasure(true).setTradeable(false).setDiscoverable(false));

}

package tfar.mineanything.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import tfar.mineanything.init.ModEnchantments;

public class NamedPickaxeItem extends PickaxeItem {
    protected NamedPickaxeItem(Tier $$0, int $$1, float $$2, Properties $$3) {
        super($$0, $$1, $$2, $$3);
    }


    @Override
    public Component getName(ItemStack stack) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MINE_ANYTHING,stack);
        if (level <1) return super.getName(stack);
        switch (level) {
            case 1 -> {
                return Component.literal("Iron Pickaxe");
            }
            case 2 -> {
                return Component.literal("Mob Miner");
            }
            case 3 -> {
                return Component.literal("Bedrock Breaker");
            }
            case 4 -> {
                return Component.literal("Dimension Destroyer");
            }
            case 5 -> {
                return Component.literal("Dragon Tamer");
            }
            default -> {
                return Component.literal("Dragon Tamer");
            }
        }
    }
}

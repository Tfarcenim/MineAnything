package tfar.mineanything.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.IntUnaryOperator;

public class BasicEnchantment extends Enchantment {
    protected final int maxLevel;
    protected final IntUnaryOperator minCost;
    protected final IntUnaryOperator range;
    protected final PostAttack postAttack;
    protected final boolean treasure;
    protected final boolean curse;
    protected final boolean tradeable;
    protected final boolean discoverable;

    public BasicEnchantment(Properties properties) {
        super(properties.rarity,properties.category,properties.slots);
        this.maxLevel = properties.maxLevel;
        this.minCost = properties.minCost;
        this.range = properties.range;
        this.postAttack = properties.postAttack;
        this.treasure = properties.treasure;
        this.curse = properties.curse;
        this.tradeable = properties.tradeable;
        this.discoverable = properties.discoverable;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMinCost(int level) {
        return minCost.applyAsInt(level);
    }

    @Override
    public int getMaxCost(int level) {
        return minCost.applyAsInt(level) + range.applyAsInt(level);
    }

    @Override
    public void doPostAttack(LivingEntity $$0, Entity $$1, int $$2) {
        postAttack.postAttack($$0, $$1, $$2);
    }

    /**
     * Checks if the enchantment should be considered a treasure enchantment. These enchantments can not be obtained
     * using the enchantment table. The mending enchantment is an example of a treasure enchantment.
     * @return Whether the enchantment is a treasure enchantment.
     */
    @Override
    public boolean isTreasureOnly() {
        return treasure;
    }

    /**
     * Checks if the enchantment is considered a curse. These enchantments are treated as debuffs and can not be removed
     * from items under normal circumstances.
     * @return Whether the enchantment is a curse.
     */
    @Override
    public boolean isCurse() {
        return curse;
    }

    /**
     * Checks if the enchantment can be traded by NPCs like villagers.
     * @return Whether this enchantment can be traded.
     */
    @Override
    public boolean isTradeable() {
        return tradeable;
    }

    /**
     * Checks if the enchantment can be discovered by game mechanics which pull random enchantments from the enchantment
     * registry.
     * @return Whether the enchantment can be discovered.
     */
    @Override
    public boolean isDiscoverable() {
        return discoverable;
    }

    public static class Properties {
        protected final Rarity rarity;
        protected final EnchantmentCategory category;
        protected final EquipmentSlot[] slots;
        protected int maxLevel = 1;
        protected boolean treasure;
        protected boolean curse;
        protected boolean tradeable = true;
        protected boolean discoverable = true;

        IntUnaryOperator minCost = level -> 1 + level * 10;
        IntUnaryOperator range = level -> 5;
        PostAttack postAttack = (attacker, target, enchantmentLevel) -> {};

        public Properties(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
            this.rarity = rarity;
            this.category = category;
            this.slots = slots;
        }

        public static Properties builder(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
            return new Properties(rarity,category,slots);
        }

        public Properties maxLevel(int level) {
            this.maxLevel = level;
            return this;
        }

        public Properties minCost(IntUnaryOperator minCost) {
            this.minCost = minCost;
            return this;
        }

        public Properties range(IntUnaryOperator range) {
            this.range = range;
            return this;
        }

        public Properties postAttack(PostAttack postAttack) {
            this.postAttack = postAttack;
            return this;
        }

        public Properties setTreasure(boolean treasure) {
            this.treasure = treasure;
            return this;
        }

        public Properties setCurse(boolean curse) {
            this.curse = curse;
            return this;
        }

        public Properties setTradeable(boolean tradeable) {
            this.tradeable = tradeable;
            return this;
        }

        public Properties setDiscoverable(boolean discoverable) {
            this.discoverable = discoverable;
            return this;
        }

    }

    @FunctionalInterface
    public interface PostAttack {
        void postAttack(LivingEntity attacker, Entity target, int enchantmentLevel);
    }
}

package tfar.mineanything.network.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import tfar.mineanything.PlayerDuck;
import tfar.mineanything.Utils;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.init.ModEnchantments;
import tfar.mineanything.init.ModEntities;
import tfar.mineanything.init.ModItems;

import java.util.Map;

public class C2SKeyActionPacket implements C2SModPacket {

    final Action action;
    public C2SKeyActionPacket(FriendlyByteBuf buf) {
        action = Action.values()[buf.readInt()];
    }

    public C2SKeyActionPacket(Action action) {
        this.action = action;
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(action.ordinal());
    }

    @Override
    public void handleServer(ServerPlayer player) {
        switch (action) {
            case PING -> {

            }
            case CLONE -> {
                PlayerDuck playerDuck = PlayerDuck.of(player);
                int cloneCooldown = playerDuck.getCloneCooldown();
                if (cloneCooldown <=0) {
                    ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
                    if (stack.is(Items.PLAYER_HEAD)) {
                        CompoundTag tag = stack.getTag();
                        if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                            GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                            if (gameProfile != null) {
                                ServerPlayer cloneFrom = player.server.getPlayerList().getPlayer(gameProfile.getId());
                                Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = player.server.getSessionService()
                                        .getTextures(gameProfile, false);

                                EntityType<ClonePlayerEntity> clonePlayerEntityEntityType = ModEntities.CLONE_PLAYER;
                                for (int i = 0; i < 1; i++) {
                                    ClonePlayerEntity clonePlayerEntity = clonePlayerEntityEntityType.spawn(player.serverLevel(), player.blockPosition(), MobSpawnType.COMMAND);
                                    clonePlayerEntity.setClone(gameProfile);
                                    clonePlayerEntity.setOwnerUUID(player.getUUID());
                                    clonePlayerEntity.setCustomName(Component.literal(gameProfile.getName()));
                                    if (cloneFrom != null) {
                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.HEAD, cloneFrom.getItemBySlot(EquipmentSlot.HEAD));
                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.CHEST, cloneFrom.getItemBySlot(EquipmentSlot.CHEST));
                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.LEGS, cloneFrom.getItemBySlot(EquipmentSlot.LEGS));
                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.FEET, cloneFrom.getItemBySlot(EquipmentSlot.FEET));

                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.MAINHAND, cloneFrom.getItemBySlot(EquipmentSlot.MAINHAND));
                                        clonePlayerEntity.setFakeItemSlot(EquipmentSlot.OFFHAND, cloneFrom.getItemBySlot(EquipmentSlot.OFFHAND));
                                    }
                                }
                                playerDuck.setCloneCooldown(100);
                            }
                        }
                    }
                }
            }
            case LEVEL_UP -> {
                ItemStack stack = player.getMainHandItem();
                if (!stack.isEmpty()) {
                    int mine_anything = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.MINE_ANYTHING,stack);
                    if (mine_anything > 0) {
                        ListTag listTag = stack.getEnchantmentTags();
                        for (Tag tag : listTag) {
                            CompoundTag compoundTag = (CompoundTag) tag;
                            ResourceLocation location = BuiltInRegistries.ENCHANTMENT.getKey(ModEnchantments.MINE_ANYTHING);
                            if (compoundTag.getString("id").equals(location.toString())) {
                                EnchantmentHelper.setEnchantmentLevel(compoundTag,mine_anything+1);
                            }
                        }
                    }else {
                        stack.enchant(ModEnchantments.MINE_ANYTHING,1);
                    }
                }
            }
            case TOGGLE_FLIGHT -> {
                ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (stack.is(ModItems.DRAGON_ELYTRA)) {
                    Utils.toggleFlight(stack);
                }
            }
            case TOGGLE_HOVER -> {
                ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (stack.is(ModItems.DRAGON_ELYTRA)) {
                    Utils.toggleHover(stack);
                }
            }
        }
    }

    public enum Action{
        PING,LEVEL_UP,CLONE, TOGGLE_FLIGHT,TOGGLE_HOVER;
    }

}

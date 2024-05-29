package tfar.mineanything.network.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlayerHeadItem;
import tfar.mineanything.entity.ClonePlayerEntity;
import tfar.mineanything.init.ModEntities;

import java.util.Map;

public class C2SKeyActionPacket implements C2SModPacket<C2SKeyActionPacket> {

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
                ItemStack stack = player.getItemBySlot(EquipmentSlot.HEAD);
                if (stack.is(Items.PLAYER_HEAD)) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null && tag.contains(PlayerHeadItem.TAG_SKULL_OWNER)) {
                        GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound(PlayerHeadItem.TAG_SKULL_OWNER));
                        if (gameProfile != null) {
                            ServerPlayer cloneFrom = player.server.getPlayerList().getPlayer(gameProfile.getId());
                            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures = player.server.getSessionService()
                                    .getTextures(gameProfile, false);

                            if (textures.isEmpty()) {

                            }

                            EntityType<ClonePlayerEntity> clonePlayerEntityEntityType = ModEntities.CLONE_PLAYER;
                            for (int i = 0; i < 1;i++) {
                                ClonePlayerEntity clonePlayerEntity = clonePlayerEntityEntityType.spawn(player.serverLevel(),player.blockPosition(), MobSpawnType.COMMAND);
                                clonePlayerEntity.setClone(gameProfile.getId());
                                clonePlayerEntity.setOwnerUUID(player.getUUID());
                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.HEAD,cloneFrom.getItemBySlot(EquipmentSlot.HEAD));
                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.CHEST,cloneFrom.getItemBySlot(EquipmentSlot.CHEST));
                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.LEGS,cloneFrom.getItemBySlot(EquipmentSlot.LEGS));
                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.FEET,cloneFrom.getItemBySlot(EquipmentSlot.FEET));

                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.MAINHAND,cloneFrom.getItemBySlot(EquipmentSlot.MAINHAND));
                                clonePlayerEntity.setFakeItemSlot(EquipmentSlot.OFFHAND,cloneFrom.getItemBySlot(EquipmentSlot.OFFHAND));

                            }
                        }
                    }
                }
            }
        }
    }

    public enum Action{
        PING,LEVEL_UP,CLONE;
    }

}

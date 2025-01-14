package tfar.mineanything.blockentity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import tfar.mineanything.init.ModBlockEntities;

import java.util.UUID;

public class PlayerBodyBlockEntity extends BlockEntity {

    protected GameProfile gameProfile;
    public PlayerBodyBlockEntity(BlockEntityType<?> $$0, BlockPos $$1, BlockState $$2) {
        super($$0, $$1, $$2);
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
        setChanged();
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (gameProfile != null) {
            CompoundTag compoundTag = NbtUtils.writeGameProfile(new CompoundTag(),gameProfile);
            tag.put("GameProfile",compoundTag);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("GameProfile")) {
            gameProfile = NbtUtils.readGameProfile(tag.getCompound("GameProfile"));
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),3);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public PlayerBodyBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.PLAYER_BODY,pos,state);
    }

}

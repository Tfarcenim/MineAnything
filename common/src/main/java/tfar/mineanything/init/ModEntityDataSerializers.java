package tfar.mineanything.init;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

public class ModEntityDataSerializers {

    public static final EntityDataSerializer<GameProfile> GAME_PROFILE = EntityDataSerializer.simple(
            FriendlyByteBuf::writeGameProfile, FriendlyByteBuf::readGameProfile
    );

    static {
        EntityDataSerializers.registerSerializer(GAME_PROFILE);
    }
}

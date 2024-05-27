package tfar.mineanything.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

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
        }
    }

    public enum Action{
        PING;
    }

}

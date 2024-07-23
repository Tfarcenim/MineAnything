package tfar.mineanything.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.mineanything.world.InputHandler;

public class C2SInputPacket implements C2SModPacket{

    private final boolean up;
    private final boolean down;
    private final boolean forwards;
    private final boolean backwards;
    private final boolean left;
    private final boolean right;
    private final boolean sprint;

    public C2SInputPacket() {
        this.up = false;
        this.down = false;
        this.forwards = false;
        this.backwards = false;
        this.left = false;
        this.right = false;
        this.sprint = false;
    }

    public C2SInputPacket(FriendlyByteBuf buffer) {
        up = buffer.readBoolean();
        down = buffer.readBoolean();
        forwards = buffer.readBoolean();
        backwards = buffer.readBoolean();
        left = buffer.readBoolean();
        right = buffer.readBoolean();
        sprint = buffer.readBoolean();
    }

    public C2SInputPacket(boolean up, boolean down, boolean forwards, boolean backwards, boolean left, boolean right, boolean sprint) {
        this.up = up;
        this.down = down;
        this.forwards = forwards;
        this.backwards = backwards;
        this.left = left;
        this.right = right;
        this.sprint = sprint;
    }


    @Override
    public void handleServer(ServerPlayer player) {
        InputHandler.update(player, up, down, forwards, backwards, left, right, sprint);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(up);
        to.writeBoolean(down);
        to.writeBoolean(forwards);
        to.writeBoolean(backwards);
        to.writeBoolean(left);
        to.writeBoolean(right);
        to.writeBoolean(sprint);
    }
}

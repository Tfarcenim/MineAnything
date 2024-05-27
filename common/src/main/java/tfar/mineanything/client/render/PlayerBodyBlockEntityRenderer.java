package tfar.mineanything.client.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.Pose;
import tfar.mineanything.blockentity.PlayerBodyBlockEntity;

import java.util.UUID;

public class PlayerBodyBlockEntityRenderer implements BlockEntityRenderer<PlayerBodyBlockEntity> {
    public PlayerBodyBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(PlayerBodyBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light,int i1) {
        Minecraft client = Minecraft.getInstance();
        matrices.pushPose();
        matrices.translate(1.0, 0.0, 0.0);
        UUID playerUuid = entity.getUuid();
        if (playerUuid != null) {
            LocalPlayer copyPlayer = new LocalPlayer(client, client.level, new ClientPacketListener(null, null, new Connection(PacketFlow.CLIENTBOUND), null, new GameProfile(playerUuid, "null"), null), null, null, false, false) {
                @Override
                public boolean shouldShowName() {
                    return false;
                }
            };

            copyPlayer.setPose(Pose.SLEEPING);
            copyPlayer.yHeadRotO = 25;
            copyPlayer.setYHeadRot(25);
            copyPlayer.setUUID(playerUuid);

            client.getEntityRenderDispatcher().getRenderer(copyPlayer).render(copyPlayer, 0f, tickDelta, matrices, vertexConsumers, light);
            matrices.popPose();
            return;

        }

        /*Skeleton fakeSkeleton = getFakeSkeleton();
        for (DeadBodyData data :  savedData) {
            data.transferTo(fakeSkeleton);
        }

        client.getEntityRenderDispatcher().getRenderer(fakeSkeleton).render(fakeSkeleton, 0f, tickDelta, matrices, vertexConsumers, light);*/
        matrices.popPose();

    }
}

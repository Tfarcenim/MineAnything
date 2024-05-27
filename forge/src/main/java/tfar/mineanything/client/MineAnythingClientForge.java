package tfar.mineanything.client;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.mineanything.MineAnything;

public class MineAnythingClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(MineAnythingClientForge::clientSetup);
        bus.addListener(MineAnythingClientForge::registerRenderers);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientTick);
    }

    static void clientSetup(FMLClientSetupEvent event) {
        MineAnythingClient.clientSetup();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        MineAnythingClient.clientTick();
    }

    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        MineAnythingClient.registerRenderers();
    }

}

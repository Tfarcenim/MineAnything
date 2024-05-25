package tfar.mineanything.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class MineAnythingClientForge {

    public static void init(IEventBus bus) {
        bus.addListener(MineAnythingClientForge::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(MineAnythingClientForge::clientTick);
    }

    static void clientSetup(FMLClientSetupEvent event) {
        MineAnythingClient.clientSetup();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        MineAnythingClient.clientTick();
    }

}

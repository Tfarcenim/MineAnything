package tfar.mineanything;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tfar.mineanything.client.MineAnythingClientForge;
import tfar.mineanything.platform.Side;

@Mod(MineAnything.MOD_ID)
public class MineAnythingForge {
    
    public MineAnythingForge() {
        MineAnything.preInit();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        if (MineAnything.SIDE == Side.CLIENT) {
            MineAnythingClientForge.init(bus);
        }
    }

    private void setup(FMLCommonSetupEvent event) {

    }

}
package tfar.mineanything;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineAnything.MOD_ID)
public class MineAnythingForge {
    
    public MineAnythingForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MineAnything.init();
        
    }
}
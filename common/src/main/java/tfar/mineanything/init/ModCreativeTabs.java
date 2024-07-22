package tfar.mineanything.init;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import tfar.mineanything.MineAnything;

public class ModCreativeTabs {
    public static final CreativeModeTab TAB = CreativeModeTab.builder(null,-1)
            .title(Component.literal("MineAnything Items"))
            .icon(ModItems.PICKAXE::getDefaultInstance)
            .displayItems((itemDisplayParameters, output) -> MineAnything.getKnownItems().forEach(output::accept))
            .build();
}

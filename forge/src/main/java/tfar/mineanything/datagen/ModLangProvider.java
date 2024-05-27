package tfar.mineanything.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.mineanything.MineAnything;
import tfar.mineanything.init.ModBlocks;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, MineAnything.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.PLAYER_BODY,"Player Body");
    }
}

package tfar.mineanything.mixin;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mineanything.MineAnything;
import tfar.mineanything.client.MineAnythingClient;

@Mixin(ModelBakery.class)
public class ModelBakeryMixin {


    @Inject(method = "<clinit>",at = @At("RETURN"))
    private static void staticInit(CallbackInfo ci) {
        MineAnythingClient.REINFORCED_BASE = new Material(Sheets.SHIELD_SHEET, MineAnything.id("entity/shield/fortified_shield"));
    }
}

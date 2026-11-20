package com.example.hudspread.mixin;

import com.example.hudspread.HudCache;
import com.example.hudspread.HudSpreadClient;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    // 26.3: Gui.extractRenderState(DeltaTracker, boolean, boolean) only *extracts* draw data;
    // drawing is done later by a separate renderer. Skipping extraction on a cached frame may
    // leave an empty HUD, so this stays inert until HudCache is finished.
    // Parameter list omitted on purpose (Mixin allows dropping target args).
    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$render(CallbackInfo ci) {
        HudSpreadClient.once("GuiMixin active: Gui.extractRenderState hooked");
        if (HudCache.canUseCache()) {
            // TODO: draw cached HUD image here, then skip the real HUD.
            ci.cancel();
        }
    }
}

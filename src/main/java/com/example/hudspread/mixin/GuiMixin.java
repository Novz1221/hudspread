package com.example.hudspread.mixin;

import com.example.hudspread.HudCache;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    // VERIFY: method name/descriptor and GuiGraphics class name in 26.3
    // (Mojang renamed several GUI render methods in recent versions).
    @Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$render(GuiGraphics graphics, DeltaTracker delta, CallbackInfo ci) {
        if (HudCache.canUseCache()) {
            // TODO: draw cached HUD image here, then skip the real HUD.
            ci.cancel();
        }
    }
}

package com.example.hudspread.mixin;

import com.example.hudspread.HudCache;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    // Parameter list is omitted on purpose: Mixin allows dropping target args, so this
    // compiles even though GuiGraphics no longer exists in 26.3.
    // VERIFY: method name "render" in the decompiled 26.3 Gui class.
    @Inject(method = "render", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$render(CallbackInfo ci) {
        if (HudCache.canUseCache()) {
            // TODO: draw cached HUD image here, then skip the real HUD.
            ci.cancel();
        }
    }
}

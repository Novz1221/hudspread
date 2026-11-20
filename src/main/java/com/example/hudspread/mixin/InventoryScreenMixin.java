package com.example.hudspread.mixin;

import com.example.hudspread.HudSpreadClient;
import com.example.hudspread.HudSpreadConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Skips the live player model drawn in the survival inventory.
 * Targets are strings and require = 0 so a renamed class/method in 26.3
 * will NOT crash the game; the option just does nothing until fixed.
 * Method name confirmed against 26.3 (javap): static extractEntityInInventoryFollowsMouse(...).
 */
@Mixin(targets = "net.minecraft.client.gui.screens.inventory.InventoryScreen")
public class InventoryScreenMixin {
    @Inject(method = "extractEntityInInventoryFollowsMouse",
            at = @At("HEAD"), cancellable = true, require = 0)
    private static void hudspread$skipEntityPreview(CallbackInfo ci) {
        HudSpreadClient.once("InventoryScreenMixin active: extractEntityInInventoryFollowsMouse hooked");
        HudSpreadConfig cfg = HudSpreadConfig.get();
        if (cfg.disableInventoryAnimations && cfg.disableInventoryEntityPreview) {
            ci.cancel();
        }
    }
}

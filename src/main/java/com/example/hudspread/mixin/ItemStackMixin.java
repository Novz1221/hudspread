package com.example.hudspread.mixin;

import com.example.hudspread.HudSpreadClient;
import com.example.hudspread.HudSpreadConfig;
import com.example.hudspread.InventoryState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Hides the animated enchantment glint while an inventory screen is open. */
@Mixin(targets = "net.minecraft.world.item.ItemStack")
public class ItemStackMixin {
    @Inject(method = "hasFoil", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$noGlint(CallbackInfoReturnable<Boolean> cir) {
        HudSpreadClient.once("ItemStackMixin active: hasFoil hooked");
        if (HudSpreadConfig.get().disableInventoryGlint && InventoryState.open()) {
            HudSpreadClient.once("ItemStackMixin: glint hidden while a container screen is open");
            cir.setReturnValue(false);
        }
    }
}

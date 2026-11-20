package com.example.hudspread.mixin;

import com.example.hudspread.HudSpreadConfig;
import com.example.hudspread.InventoryState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Freezes animated textures while an inventory screen is open.
 * VERIFY class/method name in 26.3 sources (texture ticking was refactored
 * several times). require = 0 -> harmless if the name is wrong.
 */
@Mixin(targets = "net.minecraft.client.renderer.texture.TextureAtlasSprite")
public class SpriteMixin {
    @Inject(method = "tickAndUpload", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$freeze(CallbackInfo ci) {
        if (HudSpreadConfig.get().disableInventoryTextureAnimation && InventoryState.open()) {
            ci.cancel();
        }
    }
}

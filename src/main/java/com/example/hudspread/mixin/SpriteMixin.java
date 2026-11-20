package com.example.hudspread.mixin;

import com.example.hudspread.HudSpreadConfig;
import com.example.hudspread.InventoryState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Freezes animated textures while an inventory screen is open.
 * In 26.3 sprite animation is GPU driven: TextureAtlasSprite.uploadSpriteUbo(ByteBuffer, int x5)
 * pushes the current frame data (tickAndUpload no longer exists). Skipping it keeps the last frame.
 * require = 0 -> harmless if this guess is wrong.
 */
@Mixin(targets = "net.minecraft.client.renderer.texture.TextureAtlasSprite")
public class SpriteMixin {
    @Inject(method = "uploadSpriteUbo", at = @At("HEAD"), cancellable = true, require = 0)
    private void hudspread$freeze(CallbackInfo ci) {
        if (HudSpreadConfig.get().disableInventoryTextureAnimation && InventoryState.open()) {
            ci.cancel();
        }
    }
}

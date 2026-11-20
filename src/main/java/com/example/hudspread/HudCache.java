package com.example.hudspread;

/**
 * Decides, per frame, whether the real HUD should be re-rendered or the cached
 * image reused. The actual cache (an offscreen render target) is the TODO part.
 */
public final class HudCache {
    private static int frame = 0;
    private static boolean cacheValid = false;

    private HudCache() {}

    /** True when this frame must draw the real HUD (and refresh the cache). */
    public static boolean shouldRefresh() {
        return !cacheValid || (++frame % HudSpreadConfig.get().hudUpdateInterval == 0);
    }

    /** True when we can skip the real HUD and just draw the cached image. */
    public static boolean canUseCache() {
        return cacheValid && !shouldRefresh();
    }

    public static void invalidate() { cacheValid = false; }

    // TODO 1: create an offscreen render target sized to the window
    //         (recreate on resize -> invalidate()).
    // TODO 2: on refresh frames, redirect HUD drawing into that target.
    // TODO 3: on cached frames, draw the target's color texture fullscreen,
    //         then set cacheValid = true after the first successful refresh.
    // Until these are done cacheValid stays false, so the mod changes nothing.
}

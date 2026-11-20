package com.example.hudspread;

/**
 * Decides, per frame, whether the real HUD should be re-rendered or the cached
 * image reused, based on the configured HUD fps (1-60). The actual cache (an
 * offscreen render target) is still the TODO part.
 */
public final class HudCache {
    private static long lastRefreshNs = 0;
    private static boolean cacheValid = false;

    private HudCache() {}

    /** True when this frame must draw the real HUD (and refresh the cache). */
    public static boolean shouldRefresh() {
        int fps = HudSpreadConfig.get().hudFps;
        if (fps >= 60 || !cacheValid) return true;
        return System.nanoTime() - lastRefreshNs >= 1_000_000_000L / fps;
    }

    /** True when we can skip the real HUD and just draw the cached image. */
    public static boolean canUseCache() {
        return cacheValid && !shouldRefresh();
    }

    /** Call after the real HUD was drawn into the cache target. */
    public static void markRefreshed() {
        lastRefreshNs = System.nanoTime();
        cacheValid = true;
    }

    public static void invalidate() { cacheValid = false; }

    // TODO 1: create an offscreen render target sized to the window
    //         (recreate on resize -> invalidate()).
    // TODO 2: on refresh frames, redirect HUD drawing into that target, then markRefreshed().
    // TODO 3: on cached frames, draw the target's color texture fullscreen.
    // Until these are done cacheValid stays false, so the HUD is unchanged.
}

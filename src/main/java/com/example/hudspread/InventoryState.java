package com.example.hudspread;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/** True while any inventory-like screen (inventory, chest, crafting, creative...) is open. */
public final class InventoryState {
    private static volatile boolean containerOpen = false;
    private static Screen tracked = null;

    private InventoryState() {}

    /** Called once from the client initializer. Uses Fabric API events, so it does not
     *  depend on the (renamed) Minecraft#screen field. */
    public static void init() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof AbstractContainerScreen<?> && screen != tracked) {
                tracked = screen;
                containerOpen = true;
                ScreenEvents.remove(screen).register(s -> {
                    if (s == tracked) {
                        tracked = null;
                        containerOpen = false;
                    }
                });
            }
        });
    }

    public static boolean open() {
        return HudSpreadConfig.get().disableInventoryAnimations && containerOpen;
    }
}

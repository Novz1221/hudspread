package com.example.hudspread;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/** True while any inventory-like screen (inventory, chest, crafting, creative...) is open. */
public final class InventoryState {
    private InventoryState() {}

    public static boolean open() {
        return HudSpreadConfig.get().disableInventoryAnimations
                && Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>;
    }
}

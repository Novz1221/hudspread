package com.example.hudspread;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HudSpreadClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("hudspread");

    @Override
    public void onInitializeClient() {
        HudSpreadConfig.load();
        InventoryState.init();
        LOG.info("HudSpread loaded (HUD interval = {} frames, inventory animations off = {})",
                HudSpreadConfig.get().hudUpdateInterval,
                HudSpreadConfig.get().disableInventoryAnimations);
    }
}

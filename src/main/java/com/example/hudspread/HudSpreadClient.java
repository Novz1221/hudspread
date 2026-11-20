package com.example.hudspread;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HudSpreadClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("hudspread");

    @Override
    public void onInitializeClient() {
        HudSpreadConfig.load();
        InventoryState.init();
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("hudspread").executes(ctx -> {
                    Minecraft mc = Minecraft.getInstance();
                    // open on the next tick: the chat screen closes right after the command runs
                    mc.execute(() -> mc.setScreen(new HudSpreadScreen(null)));
                    return Command.SINGLE_SUCCESS;
                })));
        LOG.info("HudSpread loaded (HUD fps = {}, inventory animations off = {})",
                HudSpreadConfig.get().hudFps,
                HudSpreadConfig.get().disableInventoryAnimations);
    }
}

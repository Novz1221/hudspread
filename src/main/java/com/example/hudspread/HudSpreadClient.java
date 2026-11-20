package com.example.hudspread;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HudSpreadClient implements ClientModInitializer {
    public static final Logger LOG = LoggerFactory.getLogger("hudspread");
    private static final java.util.Set<String> SEEN = java.util.concurrent.ConcurrentHashMap.newKeySet();

    /** Logs a diagnostic line only the first time it is seen (cheap enough for per-frame hooks). */
    public static void once(String msg) {
        if (SEEN.add(msg)) LOG.info("[diag] {}", msg);
    }

    @Override
    public void onInitializeClient() {
        HudSpreadConfig.load();
        InventoryState.init();
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> registerCommand(dispatcher));
        LOG.info("HudSpread loaded (HUD fps = {}, inventory animations off = {})",
                HudSpreadConfig.get().hudFps,
                HudSpreadConfig.get().disableInventoryAnimations);
    }

    /** Generic on purpose: avoids naming ClientCommandManager / FabricClientCommandSource. */
    private static <S> void registerCommand(CommandDispatcher<S> dispatcher) {
        dispatcher.register(LiteralArgumentBuilder.<S>literal("hudspread").executes(ctx -> {
            // open on the next tick: the chat screen closes right after the command runs
            Minecraft.getInstance().execute(() -> ScreenOpener.open(new HudSpreadScreen(null)));
            return Command.SINGLE_SUCCESS;
        }));
    }
}

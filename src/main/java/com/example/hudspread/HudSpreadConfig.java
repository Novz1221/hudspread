package com.example.hudspread;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Simple JSON config at .minecraft/config/hudspread.json */
public final class HudSpreadConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE =
            FabricLoader.getInstance().getConfigDir().resolve("hudspread.json");

    /** Re-render the HUD once every N frames (1 = every frame, no caching). */
    public int hudUpdateInterval = 3;

    /** Master switch: turn off animations in the inventory screen to reduce lag. */
    public boolean disableInventoryAnimations = true;

    /** Sub-option: don't draw the live (mouse-following) player model in the inventory. */
    public boolean disableInventoryEntityPreview = true;

    /** Sub-option: no enchantment glint shimmer on items while a container screen is open. */
    public boolean disableInventoryGlint = true;

    /** Sub-option: freeze animated item/block textures (clock, compass, etc.) while a container screen is open. */
    public boolean disableInventoryTextureAnimation = true;

    private static HudSpreadConfig instance = new HudSpreadConfig();

    public static HudSpreadConfig get() { return instance; }

    public static void load() {
        try {
            if (Files.exists(FILE)) {
                HudSpreadConfig loaded = GSON.fromJson(Files.readString(FILE), HudSpreadConfig.class);
                if (loaded != null) instance = loaded;
            }
            instance.hudUpdateInterval = Math.max(1, instance.hudUpdateInterval);
            save(); // writes defaults / newly added options
        } catch (IOException | RuntimeException e) {
            HudSpreadClient.LOG.error("Could not read config, using defaults", e);
        }
    }

    public static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(instance));
        } catch (IOException e) {
            HudSpreadClient.LOG.error("Could not save config", e);
        }
    }
}

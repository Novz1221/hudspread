package com.example.hudspread;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** In-game settings screen. Open with the /hudspread client command. */
public class HudSpreadScreen extends Screen {
    private final Screen parent;

    public HudSpreadScreen(Screen parent) {
        super(Component.literal("HudSpread"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        HudSpreadConfig cfg = HudSpreadConfig.get();
        int w = 220, h = 20, x = this.width / 2 - w / 2;
        int y = this.height / 2 - 104;

        this.addRenderableWidget(new IntSlider(x, y, w, h, 1, 60, cfg.hudFps,
                v -> "HUD FPS: " + (v >= 60 ? "60 (no limit)" : v), v -> cfg.hudFps = v));
        y += 24;
        this.addRenderableWidget(new IntSlider(x, y, w, h, 25, 100, cfg.hudResolutionPercent,
                v -> "HUD Resolution: " + v + "%", v -> cfg.hudResolutionPercent = v));
        y += 28;
        this.addRenderableWidget(toggle(x, y, w, h, "No Inventory Animation",
                () -> cfg.disableInventoryAnimations, v -> cfg.disableInventoryAnimations = v));
        y += 24;
        this.addRenderableWidget(toggle(x, y, w, h, "Skip player model",
                () -> cfg.disableInventoryEntityPreview, v -> cfg.disableInventoryEntityPreview = v));
        y += 24;
        this.addRenderableWidget(toggle(x, y, w, h, "No enchant glint",
                () -> cfg.disableInventoryGlint, v -> cfg.disableInventoryGlint = v));
        y += 24;
        this.addRenderableWidget(toggle(x, y, w, h, "Freeze texture animation",
                () -> cfg.disableInventoryTextureAnimation, v -> cfg.disableInventoryTextureAnimation = v));
        y += 32;
        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
                .bounds(x, y, w, h).build());
    }

    private static Button toggle(int x, int y, int w, int h, String label,
                                 Supplier<Boolean> get, Consumer<Boolean> set) {
        return Button.builder(label(label, get.get()), b -> {
            boolean next = !get.get();
            set.accept(next);
            b.setMessage(label(label, next));
        }).bounds(x, y, w, h).build();
    }

    private static Component label(String name, boolean on) {
        return Component.literal(name + ": " + (on ? "ON" : "OFF"));
    }

    @Override
    public void onClose() {
        HudSpreadConfig.save();
        HudCache.invalidate();
        ScreenOpener.open(parent);
    }

    /** Integer slider between min and max. */
    private static class IntSlider extends AbstractSliderButton {
        private final int min, max;
        private final java.util.function.IntFunction<String> text;
        private final java.util.function.IntConsumer apply;

        IntSlider(int x, int y, int w, int h, int min, int max, int current,
                  java.util.function.IntFunction<String> text, java.util.function.IntConsumer apply) {
            super(x, y, w, h, Component.empty(), (current - min) / (double) (max - min));
            this.min = min;
            this.max = max;
            this.text = text;
            this.apply = apply;
            updateMessage();
        }

        private int val() { return min + (int) Math.round(this.value * (max - min)); }

        @Override
        protected void updateMessage() { setMessage(Component.literal(text.apply(val()))); }

        @Override
        protected void applyValue() { apply.accept(val()); }
    }
}

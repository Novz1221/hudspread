package com.example.hudspread;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Opens a screen without hard-coding where setScreen lives. In 26.3 it is no longer
 * a method of Minecraft, so we look for it on Minecraft and on its Gui field.
 */
public final class ScreenOpener {
    private static Method cached;
    private static Object target;

    private ScreenOpener() {}

    public static void open(Screen screen) {
        try {
            if (cached == null) find(Minecraft.getInstance());
            if (cached != null) {
                cached.invoke(target, screen);
                return;
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            HudSpreadClient.LOG.error("Could not open screen", e);
            return;
        }
        HudSpreadClient.LOG.error("No setScreen-like method found; cannot open screen");
    }

    private static void find(Minecraft mc) {
        List<Object> targets = new ArrayList<>();
        targets.add(mc);
        for (Field f : Minecraft.class.getDeclaredFields()) {
            if (f.getType() == Gui.class) {
                try {
                    f.setAccessible(true);
                    Object o = f.get(mc);
                    if (o != null) targets.add(o);
                } catch (ReflectiveOperationException | RuntimeException ignored) {}
            }
        }
        for (int pass = 0; pass < 2; pass++) {
            for (Object t : targets) {
                for (Class<?> c = t.getClass(); c != null; c = c.getSuperclass()) {
                    for (Method m : c.getDeclaredMethods()) {
                        if (m.getParameterCount() != 1 || m.getParameterTypes()[0] != Screen.class) continue;
                        if (m.getReturnType() != void.class) continue;
                        boolean ok = pass == 0
                                ? m.getName().equals("setScreen")
                                : m.getName().toLowerCase().contains("screen");
                        if (!ok) continue;
                        try {
                            m.setAccessible(true);
                        } catch (RuntimeException e) {
                            continue;
                        }
                        cached = m;
                        target = t;
                        HudSpreadClient.LOG.info("Using {}#{} to open screens", c.getName(), m.getName());
                        return;
                    }
                }
            }
        }
    }
}

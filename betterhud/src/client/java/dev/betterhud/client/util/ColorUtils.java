package dev.betterhud.client.util;

import java.awt.Color;

public class ColorUtils {

    public static int getChromaColor(long durationMs) {
        float hue = (System.currentTimeMillis() % durationMs) / (float) durationMs;
        return Color.HSBtoRGB(hue, 0.8f, 1.0f);
    }

    public static int interpolate(int color1, int color2, float factor) {
        factor = Math.max(0, Math.min(1, factor));
        int r1 = (color1 >> 16) & 0xFF;
        int g1 = (color1 >> 8) & 0xFF;
        int b1 = color1 & 0xFF;
        int a1 = (color1 >> 24) & 0xFF;

        int r2 = (color2 >> 16) & 0xFF;
        int g2 = (color2 >> 8) & 0xFF;
        int b2 = color2 & 0xFF;
        int a2 = (color2 >> 24) & 0xFF;

        int r = (int) (r1 + factor * (r2 - r1));
        int g = (int) (g1 + factor * (g2 - g1));
        int b = (int) (b1 + factor * (b2 - b1));
        int a = (int) (a1 + factor * (a2 - a1));

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int getDynamicColor(float value, float min, float max) {
        if (max <= min) {
            return 0xFFFFFFFF;
        }
        float factor = (value - min) / (max - min);
        return interpolate(0xFFFF0000, 0xFF00FF00, factor);
    }
}

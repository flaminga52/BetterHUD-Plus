package dev.betterhud.client;

public class HUDConfig {
    public enum ColorMode {
        COSMIC("Cosmic"),
        STATIC("Static"),
        DYNAMIC("Dynamic");

        private final String name;

        ColorMode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public ColorMode next() {
            return values()[(ordinal() + 1) % values().length];
        }
    }

    private static ColorMode currentColorMode = ColorMode.COSMIC;
    private static int staticColor = 0xFFFFFFFF;

    public static ColorMode getCurrentColorMode() {
        return currentColorMode;
    }

    public static void setCurrentColorMode(ColorMode mode) {
        currentColorMode = mode;
    }

    public static int getStaticColor() {
        return staticColor;
    }

    public static void setStaticColor(int color) {
        staticColor = color;
    }

    public static void cycleColorMode() {
        currentColorMode = currentColorMode.next();
    }
}

package dev.betterhud.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BetterHudConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betterhud.json");

    public static BetterHudConfig instance = new BetterHudConfig();

    public enum ArmorMode {
        PERCENTAGE("Percentage"),
        DURABILITY("Durability"),
        REMAINING("Remaining"),
        BARS("Bars"),
        DYNAMIC_COLOR("Dynamic Color");

        private final String name;
        ArmorMode(String name) { this.name = name; }
        public String getName() { return name; }
        public ArmorMode next() { return values()[(ordinal() + 1) % values().length]; }
    }

    public static class WidgetPos {
        public int x, y;
        public boolean visible = true;
        public float scale = 1.0f;
        public boolean shadow = true;

        public WidgetPos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public WidgetPos systemPos = new WidgetPos(10, 10);
    public WidgetPos armorPos = new WidgetPos(10, 70);
    public WidgetPos potionPos = new WidgetPos(10, 130);
    public WidgetPos coordsPos = new WidgetPos(10, 190);

    public ArmorMode armorMode = ArmorMode.PERCENTAGE;
    public int colorMode = 0;

    public static void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            return;
        }
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            BetterHudConfig loaded = GSON.fromJson(reader, BetterHudConfig.class);
            if (loaded != null) {
                instance = loaded;
            }
            sanitize();
        } catch (Exception e) {
            e.printStackTrace();
            instance = new BetterHudConfig();
            sanitize();
        }
    }

    private static void sanitize() {
        if (instance.systemPos == null) instance.systemPos = new WidgetPos(10, 10);
        if (instance.armorPos == null) instance.armorPos = new WidgetPos(10, 70);
        if (instance.potionPos == null) instance.potionPos = new WidgetPos(10, 130);
        if (instance.coordsPos == null) instance.coordsPos = new WidgetPos(10, 190);
        if (instance.armorMode == null) instance.armorMode = ArmorMode.PERCENTAGE;
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

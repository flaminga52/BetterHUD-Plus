package dev.betterhud.client.hud;

import dev.betterhud.client.config.BetterHudConfig;
import dev.betterhud.client.gui.HudConfigScreen;
import dev.betterhud.client.hud.widgets.*;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class AnarchyHUD {
    public static final List<HudWidget> widgets = new ArrayList<>();

    public static void register() {
        BetterHudConfig.load();

        widgets.add(new SystemWidget(BetterHudConfig.instance.systemPos));

        widgets.add(new ArmorWidget(BetterHudConfig.instance.armorPos));

        widgets.add(new PotionWidget(BetterHudConfig.instance.potionPos));
        widgets.add(new CoordsWidget(BetterHudConfig.instance.coordsPos));

        HudRenderCallback.EVENT.register(AnarchyHUD::render);
    }

    private static void render(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.player == null) return;
        if (client.currentScreen instanceof HudConfigScreen) return;

        for (HudWidget widget : widgets) {
            widget.render(drawContext, tickDelta);
        }
    }
}

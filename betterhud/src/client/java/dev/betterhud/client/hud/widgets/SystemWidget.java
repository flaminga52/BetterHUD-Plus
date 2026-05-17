package dev.betterhud.client.hud.widgets;

import dev.betterhud.client.config.BetterHudConfig;
import dev.betterhud.client.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class SystemWidget extends HudWidget {
    public SystemWidget(BetterHudConfig.WidgetPos pos) {
        super(pos);
        this.width = 80;
        this.height = 12;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        int fps = client.getCurrentFps();

        int color;
        if (BetterHudConfig.instance.colorMode == 0) {
            color = ColorUtils.getChromaColor(4000);
        } else if (BetterHudConfig.instance.colorMode == 2) {
            color = ColorUtils.getDynamicColor(fps, 30, 144);
        } else {
            color = 0xFFFFFFFF;
        }

        context.getMatrices().push();
        context.getMatrices().translate(getX(), getY(), 0);
        context.getMatrices().scale(pos.scale, pos.scale, 1.0f);

        String text = "FPS: " + fps;
        if (pos.shadow) {
            context.drawTextWithShadow(client.textRenderer, text, 0, 0, color);
        } else {
            context.drawText(client.textRenderer, text, 0, 0, color, false);
        }

        context.getMatrices().pop();
    }
}

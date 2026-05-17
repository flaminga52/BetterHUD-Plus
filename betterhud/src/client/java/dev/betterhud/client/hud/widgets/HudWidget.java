package dev.betterhud.client.hud.widgets;

import dev.betterhud.client.config.BetterHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudWidget {
    protected final BetterHudConfig.WidgetPos pos;
    protected int width = 100;
    protected int height = 20;

    public HudWidget(BetterHudConfig.WidgetPos pos) {
        this.pos = pos;
    }

    public abstract void render(DrawContext context, float tickDelta);

    public boolean isVisible() {
        return pos.visible;
    }

    public void setVisible(boolean visible) {
        pos.visible = visible;
    }

    public int getX() { return pos.x; }
    public int getY() { return pos.y; }
    public void setPos(int x, int y) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int widgetWidth = getWidth();
        int widgetHeight = getHeight();

        pos.x = Math.max(0, Math.min(x, screenWidth - widgetWidth));
        pos.y = Math.max(0, Math.min(y, screenHeight - widgetHeight));
    }

    public int getWidth() { return (int)(width * pos.scale); }
    public int getHeight() { return (int)(height * pos.scale); }
    public float getScale() { return pos.scale; }

    public void setScale(float scale) {
        pos.scale = scale;
    }

    public boolean hasShadow() {
        return pos.shadow;
    }

    public void setShadow(boolean shadow) {
        pos.shadow = shadow;
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() &&
               mouseY >= getY() && mouseY <= getY() + getHeight();
    }
}

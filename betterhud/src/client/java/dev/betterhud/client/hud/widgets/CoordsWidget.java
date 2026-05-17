package dev.betterhud.client.hud.widgets;

import dev.betterhud.client.config.BetterHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CoordsWidget extends HudWidget {
    public CoordsWidget(BetterHudConfig.WidgetPos pos) {
        super(pos);
        this.width = 120;
        this.height = 12;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        context.getMatrices().push();
        context.getMatrices().translate(getX(), getY(), 0);
        context.getMatrices().scale(pos.scale, pos.scale, 1.0f);

        int x = (int) client.player.getX();
        int y = (int) client.player.getY();
        int z = (int) client.player.getZ();

        String coords = String.format("XYZ: %d, %d, %d", x, y, z);
        int color = 0xFFFFFFFF;

        if (pos.shadow) {
            context.drawTextWithShadow(client.textRenderer, coords, 0, 0, color);
        } else {
            context.drawText(client.textRenderer, coords, 0, 0, color, false);
        }

        context.getMatrices().pop();
    }
}

package dev.betterhud.client.hud.widgets;

import dev.betterhud.client.config.BetterHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PotionWidget extends HudWidget {
    public PotionWidget(BetterHudConfig.WidgetPos pos) {
        super(pos);
        this.width = 60;
        this.height = 20;
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        if (effects.isEmpty()) return;

        List<StatusEffectInstance> sortedEffects = new ArrayList<>(effects);

        context.getMatrices().push();
        context.getMatrices().translate(getX(), getY(), 0);
        context.getMatrices().scale(pos.scale, pos.scale, 1.0f);

        StatusEffectSpriteManager spriteManager = client.getStatusEffectSpriteManager();
        int yOffset = 0;

        for (StatusEffectInstance effect : sortedEffects) {
            StatusEffect type = effect.getEffectType();

            context.drawSprite(0, yOffset, 0, 18, 18, spriteManager.getSprite(type));

            String duration;
            if (effect.getDuration() >= 32767) {
                duration = "**:**";
            } else {
                int totalSeconds = Math.max(0, effect.getDuration() / 20);
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                duration = String.format("%02d:%02d", minutes, seconds);
            }

            int textWidth = client.textRenderer.getWidth(duration);
            context.fill(20, yOffset + 4, 22 + textWidth + 2, yOffset + 14, 0x80000000);

            int color = 0xFFFFFFFF;
            if (pos.shadow) {
                context.drawTextWithShadow(client.textRenderer, duration, 22, yOffset + 5, color);
            } else {
                context.drawText(client.textRenderer, duration, 22, yOffset + 5, color, false);
            }

            yOffset += 20;
        }

        this.height = Math.max(20, yOffset);
        context.getMatrices().pop();
    }
}

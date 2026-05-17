package dev.betterhud.client.gui;

import dev.betterhud.client.config.BetterHudConfig;
import dev.betterhud.client.hud.AnarchyHUD;
import dev.betterhud.client.hud.widgets.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HudConfigScreen extends Screen {
    private HudWidget draggingWidget = null;
    private double dragOffsetX, dragOffsetY;

    public HudConfigScreen() {
        super(Text.literal("Better HUD Settings"));
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save & Close"), button -> {
            BetterHudConfig.save();
            this.close();
        }).dimensions(this.width / 2 - 50, this.height - 30, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Armor Mode: " + BetterHudConfig.instance.armorMode.getName()), button -> {
            BetterHudConfig.instance.armorMode = BetterHudConfig.instance.armorMode.next();
            button.setMessage(Text.literal("Armor Mode: " + BetterHudConfig.instance.armorMode.getName()));
        }).dimensions(10, 10, 120, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("RESET POSITIONS"), button -> {
            BetterHudConfig.instance.systemPos = new BetterHudConfig.WidgetPos(10, 10);
            BetterHudConfig.instance.armorPos = new BetterHudConfig.WidgetPos(0, 0);
            BetterHudConfig.instance.potionPos = new BetterHudConfig.WidgetPos(10, 130);
            BetterHudConfig.instance.coordsPos = new BetterHudConfig.WidgetPos(10, 190);

            AnarchyHUD.widgets.clear();
            AnarchyHUD.widgets.add(new SystemWidget(BetterHudConfig.instance.systemPos));

            ArmorWidget armorWidget = new ArmorWidget(BetterHudConfig.instance.armorPos);
            armorWidget.snapToBottomRight(MinecraftClient.getInstance());
            AnarchyHUD.widgets.add(armorWidget);

            AnarchyHUD.widgets.add(new PotionWidget(BetterHudConfig.instance.potionPos));
            AnarchyHUD.widgets.add(new CoordsWidget(BetterHudConfig.instance.coordsPos));

            BetterHudConfig.save();
        }).dimensions(this.width - 130, 10, 120, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "L-Click: Drag | R-Click: Scale | M-Click: Shadow | Shift+L-Click: Toggle", this.width / 2, 25, 0xFFAAAAAA);

        for (HudWidget widget : AnarchyHUD.widgets) {
            if (!widget.isVisible() && !Screen.hasControlDown()) continue;

            if (!(widget instanceof ArmorWidget)) {
                int color = widget.isHovered(mouseX, mouseY) ? 0x88FFFFFF : 0x44FFFFFF;
                if (!widget.isVisible()) color = 0x44FF0000;

                context.fill(widget.getX() - 2, widget.getY() - 2,
                             widget.getX() + widget.getWidth() + 2,
                             widget.getY() + widget.getHeight() + 2, color);
            }

            widget.render(context, delta);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (HudWidget widget : AnarchyHUD.widgets) {
            if (widget.isHovered(mouseX, mouseY)) {
                if (button == 0) {
                    if (Screen.hasShiftDown()) {
                        widget.setVisible(!widget.isVisible());
                    } else {
                        draggingWidget = widget;
                        dragOffsetX = mouseX - widget.getX();
                        dragOffsetY = mouseY - widget.getY();
                    }
                    return true;
                } else if (button == 1) {
                    float scale = widget.getScale() + 0.25f;
                    if (scale > 2.0f) scale = 0.5f;
                    widget.setScale(scale);
                    return true;
                } else if (button == 2) {
                    widget.setShadow(!widget.hasShadow());
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingWidget = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (draggingWidget != null) {
            draggingWidget.setPos((int) (mouseX - dragOffsetX), (int) (mouseY - dragOffsetY));
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}

package dev.betterhud.client.hud.widgets;

import dev.betterhud.client.config.BetterHudConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ArmorWidget extends HudWidget {
    private static final int MARGIN = 2;
    private static final int ICON_SIZE = 16;
    private static final int TEXT_OFFSET_X = 20;
    private static final int LEGACY_DEFAULT_X = 10;
    private static final int LEGACY_DEFAULT_Y = 70;

    private static final EquipmentSlot[] ARMOR_ORDER = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private boolean defaultPositionApplied = false;

    public ArmorWidget(BetterHudConfig.WidgetPos pos) {
        super(pos);
    }

    public void snapToBottomRight(MinecraftClient client) {
        if (client.getWindow() == null) {
            return;
        }
        updateLayout(client);
        if (width <= 0) {
            width = ICON_SIZE + 32;
            height = 64;
        }
        pos.x = client.getWindow().getScaledWidth() - getWidth() - MARGIN;
        pos.y = client.getWindow().getScaledHeight() - getHeight() - MARGIN;
    }

    private void ensureDefaultPosition(MinecraftClient client) {
        if (defaultPositionApplied || client.getWindow() == null) {
            return;
        }
        if (pos.x == LEGACY_DEFAULT_X && pos.y == LEGACY_DEFAULT_Y) {
            snapToBottomRight(client);
        }
        defaultPositionApplied = true;
    }

    private void updateLayout(MinecraftClient client) {
        if (client.player == null) {
            width = 0;
            height = 0;
            return;
        }

        boolean barsMode = BetterHudConfig.instance.armorMode == BetterHudConfig.ArmorMode.BARS;
        int rowHeight = barsMode ? 20 : 16;
        int itemCount = 0;
        int maxTextWidth = 0;

        for (EquipmentSlot slot : ARMOR_ORDER) {
            ItemStack stack = client.player.getEquippedStack(slot);
            if (stack.isEmpty()) {
                continue;
            }
            itemCount++;
            maxTextWidth = Math.max(maxTextWidth, client.textRenderer.getWidth(getArmorInfo(stack)));
        }

        if (itemCount == 0) {
            width = 0;
            height = 0;
            return;
        }

        width = Math.max(ICON_SIZE, TEXT_OFFSET_X + maxTextWidth);
        height = itemCount * rowHeight;
    }

    @Override
    public boolean isHovered(double mouseX, double mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        ensureDefaultPosition(client);
        updateLayout(client);
        if (width <= 0 || height <= 0) {
            return false;
        }
        return super.isHovered(mouseX, mouseY);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (!isVisible()) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        ensureDefaultPosition(client);
        updateLayout(client);
        if (width <= 0 || height <= 0) {
            return;
        }

        boolean barsMode = BetterHudConfig.instance.armorMode == BetterHudConfig.ArmorMode.BARS;
        int rowHeight = barsMode ? 20 : 16;

        context.getMatrices().push();
        context.getMatrices().translate(getX(), getY(), 0);
        context.getMatrices().scale(pos.scale, pos.scale, 1.0f);

        int yOffset = 0;
        for (EquipmentSlot slot : ARMOR_ORDER) {
            ItemStack stack = client.player.getEquippedStack(slot);
            if (stack.isEmpty()) {
                continue;
            }

            context.drawItemWithoutEntity(stack, 0, yOffset);

            String info = getArmorInfo(stack);
            int color = getArmorTextColor(stack);

            if (pos.shadow) {
                context.drawTextWithShadow(client.textRenderer, info, TEXT_OFFSET_X, yOffset + 4, color);
            } else {
                context.drawText(client.textRenderer, info, TEXT_OFFSET_X, yOffset + 4, color, false);
            }

            if (barsMode) {
                renderDurabilityBar(context, stack, 0, yOffset + 14);
            }
            yOffset += rowHeight;
        }

        context.getMatrices().pop();
    }

    private String getArmorInfo(ItemStack stack) {
        if (!stack.isDamageable()) {
            return "100%";
        }

        int max = stack.getMaxDamage();
        int current = max - stack.getDamage();
        int percent = max > 0 ? (current * 100) / max : 100;

        return switch (BetterHudConfig.instance.armorMode) {
            case PERCENTAGE -> percent + "%";
            case DURABILITY, DYNAMIC_COLOR -> current + "/" + max;
            case REMAINING -> String.valueOf(current);
            default -> percent + "%";
        };
    }

    private int getArmorTextColor(ItemStack stack) {
        BetterHudConfig.ArmorMode mode = BetterHudConfig.instance.armorMode;
        if (mode != BetterHudConfig.ArmorMode.DURABILITY
                && mode != BetterHudConfig.ArmorMode.DYNAMIC_COLOR
                && mode != BetterHudConfig.ArmorMode.PERCENTAGE) {
            return 0xFFFFFF;
        }
        if (!stack.isDamageable()) {
            return 0x55FF55;
        }

        float percent = ((float) (stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage()) * 100f;
        if (percent > 70) return 0x55FF55;
        if (percent > 40) return 0xFFFF55;
        if (percent > 15) return 0xFFAA00;
        return 0xFF5555;
    }

    private void renderDurabilityBar(DrawContext context, ItemStack stack, int x, int y) {
        if (!stack.isItemBarVisible()) return;
        int barWidth = stack.getItemBarStep();
        int barColor = stack.getItemBarColor() | 0xFF000000;
        context.fill(x, y, x + barWidth, y + 1, barColor);
    }
}

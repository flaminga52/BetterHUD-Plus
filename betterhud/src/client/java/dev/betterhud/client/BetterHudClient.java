package dev.betterhud.client;

import dev.betterhud.client.config.BetterHudConfig;
import dev.betterhud.client.gui.HudConfigScreen;
import dev.betterhud.client.hud.AnarchyHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class BetterHudClient implements ClientModInitializer {
    private static KeyBinding settingsKey;

    @Override
    public void onInitializeClient() {
        BetterHudConfig.load();
        AnarchyHUD.register();

        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.betterhud.settings",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.betterhud.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (settingsKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new HudConfigScreen());
                }
            }
        });
    }
}

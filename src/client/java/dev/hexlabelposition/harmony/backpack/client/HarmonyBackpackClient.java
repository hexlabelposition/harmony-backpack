package dev.hexlabelposition.harmony.backpack.client;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

import dev.hexlabelposition.harmony.backpack.menu.ModMenus;
import dev.hexlabelposition.harmony.backpack.network.OpenBackpackPayload;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;

public class HarmonyBackpackClient implements ClientModInitializer {
	private static KeyMapping openBackpackKey;

	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.BACKPACK, BackpackScreen::new);

		openBackpackKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.harmony-backpack.open_backpack",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				KeyMapping.Category.INVENTORY));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) {
				return;
			}
			while (openBackpackKey.consumeClick()) {
				ClientPlayNetworking.send(OpenBackpackPayload.INSTANCE);
			}
		});
	}
}

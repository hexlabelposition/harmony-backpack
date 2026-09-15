package dev.hexlabelposition.harmony.backpack.client;

import dev.hexlabelposition.harmony.backpack.menu.ModMenus;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.gui.screens.MenuScreens;

public class HarmonyBackpackClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		MenuScreens.register(ModMenus.BACKPACK, BackpackScreen::new);
	}
}

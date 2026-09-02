package dev.hexlabelposition.harmony.backpack;

import dev.hexlabelposition.harmony.backpack.item.ModItems;
import dev.hexlabelposition.harmony.backpack.network.ModNetworking;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HarmonyBackpack implements ModInitializer {
	public static final String MOD_ID = "harmony-backpack";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModNetworking.register();
		LOGGER.info("Initializing Harmony Backpack mod");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

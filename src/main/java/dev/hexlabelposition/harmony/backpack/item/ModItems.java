package dev.hexlabelposition.harmony.backpack.item;

import dev.hexlabelposition.harmony.backpack.core.RegistryHelper;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public final class ModItems {
	private ModItems() {
	}

	public static final Item BACKPACK = RegistryHelper.registerItem("backpack", Item::new,
			new Item.Properties().stacksTo(1));

	public static void initialize() {
		ResourceKey<CreativeModeTab> toolsAndUtilities = ResourceKey.create(
				Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace("tools_and_utilities"));
		ItemGroupEvents.modifyEntriesEvent(toolsAndUtilities).register(entries -> entries.accept(BACKPACK));
	}
}

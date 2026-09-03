package dev.hexlabelposition.harmony.backpack.menu;

import dev.hexlabelposition.harmony.backpack.HarmonyBackpack;
import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.SimpleContainer;

public final class ModMenus {
	private ModMenus() {
	}

	/**
	 * The backpack menu type. Registering our own type instead of borrowing
	 * {@link net.minecraft.world.inventory.MenuType#GENERIC_9x3} makes the client build the very same
	 * {@link BackpackMenu} the server runs, so its click prediction already applies the same slot rules
	 * and a refused placement never flickers.
	 *
	 * <p>The screen-opening data is the menu-slot index of the backpack that was opened, which the
	 * client needs in order to lock that slot too, or -1 when the backpack isn't shown in the menu
	 * (e.g. it is held in the off-hand).
	 *
	 * <p>The client has no backing backpack {@link net.minecraft.world.item.ItemStack}, so it gets a
	 * throwaway {@link SimpleContainer} of the right size; the server keeps the authoritative contents
	 * and syncs them like any other container.
	 */
	public static final ExtendedScreenHandlerType<BackpackMenu, Integer> BACKPACK = new ExtendedScreenHandlerType<>(
			(containerId, playerInventory, lockedSlot) -> new BackpackMenu(
					containerId, playerInventory, new SimpleContainer(BackpackInventory.SIZE), lockedSlot),
			ByteBufCodecs.VAR_INT);

	public static void initialize() {
		Registry.register(BuiltInRegistries.MENU, HarmonyBackpack.id("backpack"), BACKPACK);
	}
}

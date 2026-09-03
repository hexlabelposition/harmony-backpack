package dev.hexlabelposition.harmony.backpack.menu;

import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * A 3x9 chest menu whose one player-inventory slot holding the currently-open backpack is locked:
 * that stack can't be picked up, moved, dropped or swapped while the menu is open, which also keeps
 * the {@link BackpackInventory}'s reference to it valid. Other backpacks stay fully movable in the
 * player inventory but can't be placed into the backpack's own 27 slots (see {@link StorageSlot}).
 *
 * <p>Both sides run this class — see {@link ModMenus#BACKPACK} — so the client's click prediction
 * enforces the same rules and never has to be corrected by the server.
 */
public class BackpackMenu extends ChestMenu {
	/** The {@link ClickType#SWAP} button value vanilla reserves for the off-hand. */
	private static final int OFF_HAND_SWAP_BUTTON = 40;

	/** Menu-slot index of the locked backpack, or -1 when it isn't shown in this menu (e.g. off-hand). */
	private final int lockedSlot;
	/** Hotbar index (0-8) of the locked backpack, or -1 if it isn't in the hotbar. */
	private final int lockedHotbarIndex;

	public BackpackMenu(int containerId, Inventory playerInventory, Container container, int lockedSlot) {
		super(ModMenus.BACKPACK, containerId, playerInventory, container, BackpackInventory.ROWS);
		this.lockedSlot = lockedSlot;
		this.lockedHotbarIndex = (lockedSlot >= 54 && lockedSlot < 63) ? lockedSlot - 54 : -1;

		// ChestMenu fills slots 0..SIZE-1 with plain Slots, whose mayPlace unconditionally returns true
		// and never consults Container#canPlaceItem. Swap in slots that enforce what a backpack holds.
		for (int i = 0; i < BackpackInventory.SIZE; i++) {
			Slot original = this.slots.get(i);
			replaceSlot(i, new StorageSlot(container, original.getContainerSlot(), original.x, original.y));
		}

		if (lockedSlot >= 0 && lockedSlot < this.slots.size()) {
			Slot original = this.slots.get(lockedSlot);
			replaceSlot(lockedSlot,
					new LockedSlot(original.container, original.getContainerSlot(), original.x, original.y));
		}
	}

	/**
	 * A menu provider for the backpack in {@code container}, whose own stack sits in menu slot
	 * {@code lockedSlot} (or -1 if it isn't shown in the menu). Extended because the client needs
	 * {@code lockedSlot} to build a matching menu.
	 */
	public static ExtendedScreenHandlerFactory<Integer> provider(Container container, int lockedSlot, Component title) {
		return new BackpackMenuProvider(container, lockedSlot, title);
	}

	/** Menu-slot index of the locked backpack, or -1 when it isn't shown in this menu. */
	public int getLockedSlot() {
		return lockedSlot;
	}

	/** Swaps in {@code replacement} for the slot at {@code menuSlot}, keeping the menu-slot index. */
	private void replaceSlot(int menuSlot, Slot replacement) {
		replacement.index = this.slots.get(menuSlot).index;
		this.slots.set(menuSlot, replacement);
	}

	/** Converts a player-inventory slot index (0-35) to this menu's slot index, or -1 if out of range. */
	public static int playerInventoryToMenuSlot(int inventorySlot) {
		if (inventorySlot < 0 || inventorySlot > 35) {
			return -1;
		}
		// 0-26: backpack slots, 27-53: main inventory (inv 9-35), 54-62: hotbar (inv 0-8).
		return inventorySlot <= 8 ? 54 + inventorySlot : 27 + (inventorySlot - 9);
	}

	@Override
	public void clicked(int slotId, int button, ClickType clickType, Player player) {
		if (isLockedInteraction(slotId, button, clickType)) {
			return;
		}
		super.clicked(slotId, button, clickType, player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		if (index == lockedSlot) {
			return ItemStack.EMPTY;
		}
		return super.quickMoveStack(player, index);
	}

	@Override
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return !(slot instanceof LockedSlot) && super.canTakeItemForPickAll(stack, slot);
	}

	@Override
	public boolean canDragTo(Slot slot) {
		return !(slot instanceof LockedSlot) && super.canDragTo(slot);
	}

	private boolean isLockedInteraction(int slotId, int button, ClickType clickType) {
		if (lockedSlot >= 0 && slotId == lockedSlot) {
			return true;
		}
		if (clickType != ClickType.SWAP) {
			return false;
		}
		// A number-key / off-hand swap moves an item straight out of its hotbar or off-hand slot without
		// consulting any Slot, so block one whose source is the open backpack's own slot. A backpack the
		// menu doesn't show is one held in the off-hand.
		return lockedSlot < 0 ? button == OFF_HAND_SWAP_BUTTON : button == lockedHotbarIndex;
	}

	private record BackpackMenuProvider(Container container, int lockedSlot, Component title)
			implements ExtendedScreenHandlerFactory<Integer> {
		@Override
		public Integer getScreenOpeningData(ServerPlayer player) {
			return lockedSlot;
		}

		@Override
		public Component getDisplayName() {
			return title;
		}

		@Override
		public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
			return new BackpackMenu(containerId, playerInventory, container, lockedSlot);
		}
	}

	/**
	 * A backpack storage slot that enforces {@link BackpackInventory#mayStore} — so the rule governs
	 * clicks, shift-clicks, hotbar swaps and drags, not just hopper-style automation. Taking items out
	 * is unaffected.
	 */
	private static class StorageSlot extends Slot {
		StorageSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return BackpackInventory.mayStore(stack) && this.container.canPlaceItem(getContainerSlot(), stack);
		}
	}

	/** A slot that can neither give up nor receive its contents. */
	private static class LockedSlot extends Slot {
		LockedSlot(Container container, int index, int x, int y) {
			super(container, index, x, y);
		}

		@Override
		public boolean mayPlace(ItemStack stack) {
			return false;
		}

		@Override
		public boolean mayPickup(Player player) {
			return false;
		}
	}
}

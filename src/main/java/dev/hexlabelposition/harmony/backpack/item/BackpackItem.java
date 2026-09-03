package dev.hexlabelposition.harmony.backpack.item;

import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;
import dev.hexlabelposition.harmony.backpack.menu.BackpackMenu;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BackpackItem extends Item {
	public BackpackItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		int inventorySlot = hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : -1;
		open(player, player.getItemInHand(hand), inventorySlot);
		return InteractionResult.SUCCESS;
	}

	/**
	 * Opens the backpack menu backed by {@code backpackStack}. {@code lockedInventorySlot} is the
	 * player-inventory slot (0-35) that stack sits in and which should be locked while the menu is
	 * open, or a negative value if it isn't in a lockable slot (e.g. the off-hand). No-op on the
	 * client.
	 */
	public static void open(Player player, ItemStack backpackStack, int lockedInventorySlot) {
		if (!(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		int lockedMenuSlot = BackpackMenu.playerInventoryToMenuSlot(lockedInventorySlot);
		BackpackInventory inventory = new BackpackInventory(backpackStack);

		serverPlayer.openMenu(BackpackMenu.provider(inventory, lockedMenuSlot, backpackStack.getHoverName()));
	}

	/**
	 * Finds the backpack to open for the keybind: the held item first, then the first match scanning
	 * the main inventory from slot 0 (hotbar left-to-right, then the storage rows top-to-bottom), then
	 * the off-hand.
	 */
	public static FoundBackpack findBackpack(Player player) {
		Inventory inventory = player.getInventory();
		NonNullList<ItemStack> items = inventory.getNonEquipmentItems();

		int selected = inventory.getSelectedSlot();
		if (selected >= 0 && selected < items.size() && items.get(selected).getItem() instanceof BackpackItem) {
			return new FoundBackpack(items.get(selected), selected);
		}

		for (int slot = 0; slot < items.size(); slot++) {
			if (items.get(slot).getItem() instanceof BackpackItem) {
				return new FoundBackpack(items.get(slot), slot);
			}
		}

		ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
		if (offHand.getItem() instanceof BackpackItem) {
			return new FoundBackpack(offHand, -1);
		}

		return new FoundBackpack(ItemStack.EMPTY, -1);
	}

	/** Result of {@link #findBackpack}: the stack (empty if none) and its player-inventory slot. */
	public record FoundBackpack(ItemStack stack, int inventorySlot) {
		public boolean isEmpty() {
			return stack.isEmpty();
		}
	}
}

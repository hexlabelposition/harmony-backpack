package dev.hexlabelposition.harmony.backpack.item;

import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BackpackItem extends Item {
	public BackpackItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		open(player, player.getItemInHand(hand));
		return InteractionResult.SUCCESS;
	}

	/**
	 * Opens the backpack menu backed by {@code backpackStack}. No-op on the client — the container
	 * menu is opened server-side and synced to the client automatically.
	 */
	public static void open(Player player, ItemStack backpackStack) {
		if (player instanceof ServerPlayer serverPlayer) {
			BackpackInventory inventory = new BackpackInventory(backpackStack);
			serverPlayer.openMenu(new SimpleMenuProvider(
					(syncId, playerInventory, opener) -> ChestMenu.threeRows(syncId, playerInventory, inventory),
					backpackStack.getHoverName()));
		}
	}

	/**
	 * Finds the backpack to open when the player presses the keybind: the held item first, then the
	 * first match scanning the main inventory from slot 0 (hotbar left-to-right, then the storage rows
	 * top-to-bottom), then the off-hand. Returns {@link ItemStack#EMPTY} if the player has none.
	 */
	public static ItemStack findBackpack(Player player) {
		Inventory inventory = player.getInventory();

		ItemStack selected = inventory.getSelectedItem();
		if (selected.getItem() instanceof BackpackItem) {
			return selected;
		}

		for (ItemStack stack : inventory.getNonEquipmentItems()) {
			if (stack.getItem() instanceof BackpackItem) {
				return stack;
			}
		}

		ItemStack offHand = player.getItemInHand(InteractionHand.OFF_HAND);
		if (offHand.getItem() instanceof BackpackItem) {
			return offHand;
		}

		return ItemStack.EMPTY;
	}
}

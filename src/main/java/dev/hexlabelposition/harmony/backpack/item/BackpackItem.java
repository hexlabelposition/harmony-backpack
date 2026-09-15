package dev.hexlabelposition.harmony.backpack.item;

import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;
import dev.hexlabelposition.harmony.backpack.menu.BackpackMenu;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BackpackItem extends Item {
	public BackpackItem(Properties properties) {
		super(properties);
	}

	/**
	 * Opens the menu backed by the held backpack. The slot that backpack sits in is locked while the
	 * menu is open, so it can't be moved out from under the open menu; an off-hand backpack isn't
	 * shown in the menu and so has no slot to lock. No-op on the client — the menu is opened
	 * server-side and synced to the client automatically.
	 */
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		if (player instanceof ServerPlayer serverPlayer) {
			ItemStack backpackStack = player.getItemInHand(hand);
			int inventorySlot = hand == InteractionHand.MAIN_HAND ? player.getInventory().getSelectedSlot() : -1;
			int lockedMenuSlot = BackpackMenu.playerInventoryToMenuSlot(inventorySlot);

			serverPlayer.openMenu(BackpackMenu.provider(
					new BackpackInventory(backpackStack), lockedMenuSlot, backpackStack.getHoverName()));
		}

		return InteractionResult.SUCCESS;
	}
}

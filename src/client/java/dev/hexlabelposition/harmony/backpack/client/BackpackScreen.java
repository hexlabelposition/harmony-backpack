package dev.hexlabelposition.harmony.backpack.client;

import dev.hexlabelposition.harmony.backpack.menu.BackpackMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;

/**
 * The vanilla chest screen plus a shade over the slot holding the backpack this screen belongs to,
 * marking it as untouchable while the menu is open. Every other restriction lives in
 * {@link BackpackMenu} itself, which the client runs too, so nothing has to be intercepted here.
 */
public class BackpackScreen extends ContainerScreen {
	private static final int SLOT_SIZE = 16;
	private static final int SHADOW_COLOR = 0x80000000;

	public BackpackScreen(ChestMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.render(graphics, mouseX, mouseY, partialTick);

		if (!(getMenu() instanceof BackpackMenu menu)) {
			return;
		}
		int lockedSlot = menu.getLockedSlot();
		if (lockedSlot < 0 || lockedSlot >= menu.slots.size()) {
			return;
		}

		Slot slot = menu.slots.get(lockedSlot);
		int x = leftPos + slot.x;
		int y = topPos + slot.y;
		graphics.fill(x, y, x + SLOT_SIZE, y + SLOT_SIZE, SHADOW_COLOR);
	}
}

package dev.hexlabelposition.harmony.backpack.inventory;

import dev.hexlabelposition.harmony.backpack.item.ModItems;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * A 3x9 container whose contents are backed by the {@code minecraft:container} data component of the
 * backpack {@link ItemStack} it was opened from. Changes are written straight back into that stack,
 * so the same {@code ItemStack} instance must stay in the player's inventory while the menu is open.
 */
public class BackpackInventory extends SimpleContainer {
	public static final int ROWS = 3;
	public static final int COLUMNS = 9;
	public static final int SIZE = ROWS * COLUMNS;

	private final ItemStack backpackStack;

	public BackpackInventory(ItemStack backpackStack) {
		super(SIZE);
		this.backpackStack = backpackStack;
		backpackStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(getItems());
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		// Don't allow a backpack to be stored inside a backpack.
		return !stack.is(ModItems.BACKPACK);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		save();
	}

	@Override
	public void stopOpen(ContainerUser user) {
		super.stopOpen(user);
		save();
	}

	private void save() {
		backpackStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(getItems()));
	}
}

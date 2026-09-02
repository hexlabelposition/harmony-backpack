package dev.hexlabelposition.harmony.backpack.item;

import dev.hexlabelposition.harmony.backpack.inventory.BackpackInventory;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
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
		ItemStack stack = player.getItemInHand(hand);

		if (player instanceof ServerPlayer serverPlayer) {
			BackpackInventory inventory = new BackpackInventory(stack);
			serverPlayer.openMenu(new SimpleMenuProvider(
					(syncId, playerInventory, opener) -> ChestMenu.threeRows(syncId, playerInventory, inventory),
					stack.getHoverName()));
		}

		return InteractionResult.SUCCESS;
	}
}

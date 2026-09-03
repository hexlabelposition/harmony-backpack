package dev.hexlabelposition.harmony.backpack.network;

import dev.hexlabelposition.harmony.backpack.item.BackpackItem;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import net.minecraft.server.level.ServerPlayer;

public final class ModNetworking {
	private ModNetworking() {
	}

	public static void register() {
		PayloadTypeRegistry.playC2S().register(OpenBackpackPayload.TYPE, OpenBackpackPayload.CODEC);

		ServerPlayNetworking.registerGlobalReceiver(OpenBackpackPayload.TYPE, (payload, context) -> {
			ServerPlayer player = context.player();
			// Hop onto the server thread before touching the player's inventory / opening a menu.
			context.server().execute(() -> {
				BackpackItem.FoundBackpack found = BackpackItem.findBackpack(player);
				if (!found.isEmpty()) {
					BackpackItem.open(player, found.stack(), found.inventorySlot());
				}
			});
		});
	}
}

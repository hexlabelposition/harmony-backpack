package dev.hexlabelposition.harmony.backpack.network;

import dev.hexlabelposition.harmony.backpack.HarmonyBackpack;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Empty client-to-server packet: "the player pressed the open-backpack key, please open their
 * backpack". Carries no data — the server decides which backpack to open.
 */
public record OpenBackpackPayload() implements CustomPacketPayload {
	public static final OpenBackpackPayload INSTANCE = new OpenBackpackPayload();

	public static final Type<OpenBackpackPayload> TYPE = new Type<>(HarmonyBackpack.id("open_backpack"));

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenBackpackPayload> CODEC = StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}

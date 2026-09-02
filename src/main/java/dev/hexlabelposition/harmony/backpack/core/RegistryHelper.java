package dev.hexlabelposition.harmony.backpack.core;

import dev.hexlabelposition.harmony.backpack.HarmonyBackpack;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class RegistryHelper {
    public static <T extends Item> T registerItem(String name, Function<Item.Properties, T> factory,
            Item.Properties settings) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM,
                Identifier.fromNamespaceAndPath(HarmonyBackpack.MOD_ID, name));
        T item = factory.apply(settings.setId(key));
        Registry.register(BuiltInRegistries.ITEM, key, item);
        return item;
    }
}

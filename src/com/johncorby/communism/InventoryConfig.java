package com.johncorby.communism;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static com.johncorby.communism.InventoryHandler.inventories;
import static com.johncorby.communism.Main.PLUGIN;
import static com.johncorby.communism.Main.WORLDS;

public class InventoryConfig {
    public static void load() {
        var section = PLUGIN.getConfig().getConfigurationSection("inventories");
        for (var entry : section.getValues(false).entrySet())
            inventories.put(
                    entry.getKey(),
                    ((List<ItemStack>) entry.getValue()).toArray(ItemStack[]::new)
            );

        // add new blank world inventories from added WORLDS
        for (var world : WORLDS)
            if (!inventories.containsKey(world))
                inventories.put(world, new ItemStack[InventoryType.PLAYER.getDefaultSize()]);

        // remove inventories no longer in WORLDS
        for (var key : inventories.keySet())
            if (!WORLDS.contains(key))
                inventories.remove(key);
    }

    public static void save() {
        PLUGIN.getConfig().set("inventories", inventories);
        PLUGIN.saveConfig();
    }
}

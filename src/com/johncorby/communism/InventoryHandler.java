package com.johncorby.communism;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.johncorby.communism.Utils.print;
import static com.johncorby.communism.Utils.runTask;

/**
 * NOTES
 * getContents copies into new array and clones bukkit items but not nms items
 * setContents clones nms items
 * cloning bukkit item also clones nms item
 */
public class InventoryHandler {
    /**
     * stores state of inventory (1 per world)
     */
    public static final Map<String, ItemStack[]> inventories = new HashMap<>();

    /**
     * called right before player's inventory supposedly changes
     */
    public static void inventoryMightChange(Player changedPlayer) {
        var world = changedPlayer.getWorld();

        // check if player is in valid world
        var globalInventory = inventories.getOrDefault(changedPlayer.getWorld().getName(), null);
        if (globalInventory == null) return;

        // inventory getting runs on next tick, since that's when inventory actually changes
        runTask(() -> {
            var playerInventory = changedPlayer.getInventory().getContents();
            if (Arrays.equals(playerInventory, globalInventory)) return;

            // store in global inventory, copying into new array and cloning item (see notes)
            printChanges(changedPlayer);
            for (var i = 0; i < playerInventory.length; i++) {
                if (playerInventory[i] == null) continue;
                if (playerInventory[i].equals(globalInventory[i])) continue;

                globalInventory[i] = playerInventory[i].clone();
            }

            // update other player's inventories if needed
            for (var player : world.getPlayers()) {
                if (player.equals(changedPlayer)) continue;
                if (Arrays.equals(player.getInventory().getContents(), globalInventory)) continue;

                printChanges(player);
                player.getInventory().setContents(globalInventory);
            }
        });
    }

    /**
     * print changes between player and global inventory
     * slow cuz of unneeded cloning, map searching
     */
    private static void printChanges(Player player) {
        var name = player.getDisplayName();
        var playerInventory = player.getInventory().getContents();
        var globalInventory = inventories.get(player.getWorld().getName());

        if (Arrays.equals(playerInventory, globalInventory)) return;

        for (var i = 0; i < playerInventory.length; i++)
            if (!Objects.equals(playerInventory[i], globalInventory[i]))
                print(name, " - ", i, ": ", playerInventory[i], " vs ", globalInventory[i]);
    }
}

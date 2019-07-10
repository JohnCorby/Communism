package com.johncorby.communism;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.johncorby.communism.Utils.debug;

/**
 * NOTES
 * getContents copies into new array and clones bukkit items but not nms items
 * setContents clones nms items
 * cloning bukkit item also clones nms item
 */
public class SyncHandler {
    /**
     * stores state of inventory (1 per world)
     */
    public static final Map<String, ItemStack[]> inventories = new HashMap<>();

    public static ItemStack[] getGlobal(String world) {
        return inventories.getOrDefault(world, null);
    }

    /**
     * sync global inventory
     * update other players' inventories
     */
    public static void sync(Player causer) {
        // check if player is in valid world
        var globalInv = getGlobal(causer.getWorld().getName());
        if (globalInv == null) return;

        debug("syncing");

        // push to global
        // pull only if push succeeds
        if (push(causer.getInventory().getContents(), globalInv))
            for (var player : causer.getWorld().getPlayers()) {
                if (player.equals(causer)) continue;

                pull(globalInv, player.getInventory());
            }
    }

    /**
     * pushes src inventory to dest
     * clones as needed
     */
    private static boolean push(ItemStack[] src, ItemStack[] dest) {
        if (Arrays.equals(src, dest)) return false;

        debug("pushing");
        for (var i = 0; i < src.length; i++)
            if (!Objects.equals(src[i], dest[i]))
                dest[i] = src[i] == null ? null : src[i].clone();

        return true;
    }

    /**
     * pulls from src inventory to dest
     * cloning is implicit
     */
    public static boolean pull(ItemStack[] src, Inventory dest) {
        if (Arrays.equals(src, dest.getContents())) return false;

        debug("pulling");
        dest.setContents(src);

        return true;
    }
}

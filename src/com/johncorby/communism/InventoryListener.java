package com.johncorby.communism;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.johncorby.communism.InventoryConfig.inventories;
import static com.johncorby.communism.InventoryHandler.inventoryMightChange;

public class InventoryListener implements Listener {
    /**
     * make sure player's inventory is same as global inventory when teleporting into world
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        var from = event.getFrom().getWorld().getName();
        var to = event.getTo().getWorld().getName();

        if (from.equals(to)) return;
        if (!inventories.containsKey(to)) return;

        // item cloning happens implicitly
        event.getPlayer().getInventory().setContents(inventories.get(to));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        inventoryMightChange((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        inventoryMightChange((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        inventoryMightChange(event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        inventoryMightChange(event.getPlayer());
    }
}

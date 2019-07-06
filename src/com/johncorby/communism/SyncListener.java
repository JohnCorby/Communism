package com.johncorby.communism;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;

import java.util.function.Function;

import static com.johncorby.communism.Main.PLUGIN;
import static com.johncorby.communism.SyncHandler.*;
import static com.johncorby.communism.Utils.runNextTick;

public class SyncListener implements Listener {
    public SyncListener() {
        PLUGIN.getServer().getPluginManager().registerEvents(this, PLUGIN);

        addSyncEvent(InventoryClickEvent.class, e -> (Player) e.getWhoClicked());
        addSyncEvent(InventoryDragEvent.class, e -> (Player) e.getWhoClicked());
        addSyncEvent(PlayerPickupItemEvent.class, PlayerEvent::getPlayer);
        addSyncEvent(PlayerDropItemEvent.class, PlayerEvent::getPlayer);
        addSyncEvent(BlockPlaceEvent.class, BlockPlaceEvent::getPlayer);
        addSyncEvent(PlayerItemConsumeEvent.class, PlayerEvent::getPlayer);
        addSyncEvent(PlayerItemDamageEvent.class, PlayerEvent::getPlayer);
        addSyncEvent(PlayerSwapHandItemsEvent.class, PlayerEvent::getPlayer);
        addSyncEvent(PlayerItemMendEvent.class, PlayerEvent::getPlayer);
    }

    /**
     * add event that will trigger inventory sync
     */
    private <E extends Event> void addSyncEvent(Class<E> event, Function<E, Player> causerGetter) {
        Bukkit.getPluginManager().registerEvent(
                event,
                this,
                EventPriority.NORMAL,
                (l, e) -> runNextTick(() -> sync(causerGetter.apply((E) e))),
                PLUGIN
        );
    }

    /**
     * pull from global inventory on join
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var globalInv = getGlobal(event.getPlayer().getWorld().getName());
        if (globalInv == null) return;

        pull(globalInv, event.getPlayer().getInventory());
    }

    /**
     * pull from global inventory on tp to world
     */
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        var from = event.getFrom().getWorld().getName();
        var to = event.getTo().getWorld().getName();

        if (from.equals(to)) return;

        var globalInv = getGlobal(to);
        if (globalInv == null) return;

        // tp event is cancellable = pull after event occured
        runNextTick(() -> pull(globalInv, event.getPlayer().getInventory()));
    }
}

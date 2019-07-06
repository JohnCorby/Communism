package com.johncorby.communism;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * TODO
 * add more events that change player inventory
 * make it happen with nether and end
 * sync health, xp, effects
 * <p>
 * SYNC BUGS (not syncing but inventory changes)
 * clicking autocraft
 * pressing escape from crafting table (possibly other stuff)
 */
public class Main extends JavaPlugin {
    public static final boolean DEBUG = true;
    public static Main PLUGIN;

    public static List<String> WORLDS;

    @Override
    public void onEnable() {
        PLUGIN = this;

        saveDefaultConfig();
        WORLDS = getConfig().getStringList("worlds");
        InventoryConfig.load();

        new SyncListener();
    }

    @Override
    public void onDisable() {
        InventoryConfig.save();
    }
}

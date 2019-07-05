package com.johncorby.communism;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {
    public static Main PLUGIN;

    public static List<String> WORLDS;

    @Override
    public void onEnable() {
        PLUGIN = this;

        saveDefaultConfig();
        WORLDS = getConfig().getStringList("worlds");
        InventoryConfig.load();

        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getLogger().info("Communism Enabled");
    }

    @Override
    public void onDisable() {
        InventoryConfig.save();
    }
}

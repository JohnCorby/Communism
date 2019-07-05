package com.johncorby.communism;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

import static com.johncorby.communism.Main.PLUGIN;

public class Utils {
    public static void print(Object... args) {
        StringBuilder msg = new StringBuilder();
        for (var arg : args) {
            if (arg instanceof Object[])
                arg = Arrays.deepToString((Object[]) arg);
            msg.append(arg);
        }
        PLUGIN.getLogger().info(msg.toString());
    }

    public static void runTask(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(PLUGIN);
    }
}

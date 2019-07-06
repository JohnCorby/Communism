package com.johncorby.communism;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

import static com.johncorby.communism.Main.DEBUG;
import static com.johncorby.communism.Main.PLUGIN;

public class Utils {
    public static String stringify(Object obj) {
        if (obj instanceof Object[])
            return Arrays.deepToString((Object[]) obj);
        return String.valueOf(obj);
    }

    public static void debug(Object... args) {
        if (!DEBUG) return;

        StringBuilder msg = new StringBuilder();
        for (var arg : args)
            msg.append(stringify(arg)).append(" ");
        PLUGIN.getLogger().info(msg.toString());
    }

    public static void runNextTick(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(PLUGIN);
    }
}

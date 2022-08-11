package com.neomechanical.neoutils.messages;

import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.Bukkit;

public class Logger {
    static String prefix = "["+NeoUtils.getInstance().getName()+"] ";
    public static void setPrefix(String prefix) {
        Logger.prefix = prefix;
    }
    public static void warn(String message) {
        Bukkit.getLogger().warning(prefix + message);
    }

    public static void info(String message) {
        Bukkit.getLogger().info(prefix + message);
    }
    public static void severe(String message) {
        Bukkit.getLogger().severe(prefix + message);
    }
    public static void fatal(String message) {
        Bukkit.getLogger().severe(prefix + message);
        Bukkit.getPluginManager().disablePlugin(NeoUtils.getInstance());
    }
}

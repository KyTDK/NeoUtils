package com.neomechanical.neoutils.messages;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger {
    private final JavaPlugin plugin;

    public Logger(JavaPlugin plugin) {
        prefix = "["+plugin.getName()+"] ";
        this.plugin = plugin;
    }
    private String prefix;
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public void warn(String message) {
        Bukkit.getLogger().warning(prefix + message);
    }

    public void info(String message) {
        Bukkit.getLogger().info(prefix + message);
    }
    public void severe(String message) {
        Bukkit.getLogger().severe(prefix + message);
    }
    public void fatal(String message) {
        Bukkit.getLogger().severe(prefix + message);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}

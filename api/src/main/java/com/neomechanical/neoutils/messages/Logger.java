package com.neomechanical.neoutils.messages;


import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Logger {
    private final Plugin plugin;

    /**
     * Fancy logs with prefix and mild functionality
     *
     * @param plugin Plugin instance
     */
    public Logger(Plugin plugin) {
        prefix = "[" + plugin.getName() + "] ";
        this.plugin = plugin;
    }

    private String prefix;
    /**
     * Set the prefix for all the logs
     *
     * @param prefix The prefix to be used, default is plugin name
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    /**
     * Print a warning message,
     *
     * @param message The message that will be logged
     */
    public void warn(String message) {
        Bukkit.getLogger().warning(prefix + MessageUtil.parse(message));
    }
    /**
     * Print an info message,
     *
     * @param message The message that will be logged
     */
    public void info(String message) {
        Bukkit.getLogger().info(prefix + MessageUtil.parse(message));
    }
    /**
     * Print an severe message,
     *
     * @param message The message that will be logged
     */
    public void severe(String message) {
        Bukkit.getLogger().severe(prefix + MessageUtil.parse(message));
    }
    /**
     * Print a fatal message and disable the plugin.
     *
     * @param message The message that will be logged
     */
    public void fatal(String message) {
        Bukkit.getLogger().severe(prefix + MessageUtil.parse(message));
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}

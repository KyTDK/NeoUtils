package com.neomechanical.neoutils;

import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class NeoUtils extends JavaPlugin {
    private static BukkitAudiences adventure;
    private static JavaPlugin instance;
    public static @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }
    public static JavaPlugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Tried to access instance when the plugin was disabled, please make sure you call NeoUtils.init(this) in onEnable()!");
        }
        return instance;
    }
    public static void init(@NonNull JavaPlugin plugin) {
        instance = plugin;
        adventure = BukkitAudiences.create(plugin);
    }
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

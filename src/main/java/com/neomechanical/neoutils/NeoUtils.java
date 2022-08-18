package com.neomechanical.neoutils;

import com.neomechanical.neoutils.api.Api;
import com.neomechanical.neoutils.commandManager.CommandFunctionality;
import com.neomechanical.neoutils.commandManager.CommandManager;
import com.neomechanical.neoutils.config.ConfigManager;
import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.languages.LanguageManager;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public abstract class NeoUtils extends JavaPlugin implements Api {
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
    private static LanguageManager languageManager;
    public static LanguageManager getLanguageManager() {
        if (languageManager == null) {
            throw new IllegalStateException("Tried to access languageManager, but its not set!");
        }
        return languageManager;
    }
    private static final Map<String, ConfigManager> configManager = new HashMap<>();
    public static ConfigManager getConfigManager(String configName) {
        if (configManager.get(configName) == null) {
            throw new IllegalStateException("Tried to access " + configName +" in configManager, but its not set!");
        }
        return configManager.get(configName);
    }
    public static Map<String, ConfigManager> getConfigs() {
        return configManager;
    }
    public static void setLanguageManager(LanguageManager languageManager) {
        NeoUtils.languageManager = languageManager;
    }
    public static void setConfigManager(ConfigManager configManager, String configName) {
        NeoUtils.configManager.put(configName, configManager);
    }
    public static CommandManager commandManager;
    public static CommandManager getCommandManager() {
        return commandManager;
    }
    @Override
    public void onEnable() {
        dependencyInjection();
        getServer().getPluginManager().registerEvents(new InventoryFunctionality(), this);
        this.onPluginEnable();
    }

    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        this.onPluginDisable();
    }
    public void dependencyInjection() {
        instance = this;
        commandManager = new CommandManager();
        adventure = BukkitAudiences.create(this);
    }
}

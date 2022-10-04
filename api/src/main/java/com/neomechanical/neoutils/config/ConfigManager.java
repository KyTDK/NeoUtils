package com.neomechanical.neoutils.config;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.manager.ManagerHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    //File of the config
    private File configFile = null;
    //The actual config
    private FileConfiguration config;
    private final JavaPlugin plugin;
    private boolean keepDefaults = true;
    private static final ManagerHandler managers = NeoUtils.getManagers();

    String configFilePath;

    public ConfigManager(JavaPlugin plugin, String configFilePath) {
        this.plugin = plugin;
        this.configFilePath = configFilePath;
        reloadConfig();
        managers.setConfigManager(this, configFilePath);
    }

    public void setKeepDefaults(boolean keepDefaults) {
        this.keepDefaults = keepDefaults;
    }

    //Load the config file into memory
    public void reloadConfig() {
        saveDefaultConfig();
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configFilePath);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if (keepDefaults) {
            InputStream defaultStream = plugin.getResource(configFilePath);
            //If default stream is not null, then there is a config file in the plugin resources
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
                config.setDefaults(defaultConfig);
                config.options().copyDefaults(true);
                saveConfig();
            }
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public boolean saveConfig() {
        if (config == null || configFile == null) {
            return false;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            NeoUtils.getInstance().getFancyLogger().fatal("Could not save config to " + configFile.getPath() + " (3)");
            return false;
        }
        return true;
    }

    private void saveDefaultConfig() {
        //Save config file to directory
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configFilePath);
        }
        if (!configFile.exists()) {
            plugin.saveResource(configFilePath, false);
        }
    }

    public static void reloadAllConfigs() {
        managers.getConfigs().forEach((configFileName, configManager) -> configManager.reloadConfig());
    }

    public static ConfigManager getConfigManager(String config) {
        return NeoUtils.getManagers().getConfigManager(config);
    }
}

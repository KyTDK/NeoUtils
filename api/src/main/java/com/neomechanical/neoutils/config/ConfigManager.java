package com.neomechanical.neoutils.config;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.manager.ManagerHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigManager {
    //File of the config
    private File configFile = null;
    //The actual config
    private FileConfiguration config;
    private final Plugin plugin;
    private boolean keepDefaults = true;
    private static final ManagerHandler managers = NeoUtils.getNeoUtilities().getManagers();
    private final String configFilePath;

    public ConfigManager(Plugin plugin, String configFilePath) {
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
        configFile = new File(plugin.getDataFolder(), configFilePath);

        if (!configFile.exists()) {
            // Create the directory and the config file if they don't exist
            if (!configFile.getParentFile().exists()) {
                if (!configFile.getParentFile().mkdirs()) {
                    NeoUtils.getNeoUtilities().getFancyLogger().fatal("Error creating directory");
                    return;
                }
            }

            try {
                if (!configFile.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException("Error creating the config file: " + e.getMessage(), e);
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        if (keepDefaults) {
            InputStream defaultStream = plugin.getResource(configFilePath);
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

    public static ConfigManager getConfigManager(String config) {
        return NeoUtils.getNeoUtilities().getManagers().getConfigManager(config);
    }

    public static void reloadAllConfigs() {
        managers.getConfigs().forEach((configFileName, configManager) -> configManager.reloadConfig());
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            NeoUtils.getNeoUtilities().getFancyLogger().fatal("Could not save config to " + configFile.getPath() + " (3)");
        }
    }

    public ConfigManager setConfig(FileConfiguration config) {
        this.config = config;
        return this;
    }

    public File getConfigFile() {
        return configFile;
    }

    public boolean saveConfig(FileConfiguration config) {
        if (config == null || configFile == null) {
            return false;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            NeoUtils.getNeoUtilities().getFancyLogger().fatal("Could not save config to " + configFile.getPath() + " (3)");
            return false;
        }
        return true;
    }
}

package com.neomechanical.neoutils.config;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.messages.Logger;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.List;

public class ConfigManager {

    private File configFile = null;
    private FileConfiguration config;
    private final JavaPlugin plugin = NeoUtils.getInstance();

    public void reloadConfig() {
        loadConfiguration();
    }

    String configFilePath;

    public ConfigManager(String configFilePath) {
        this.configFilePath = configFilePath;
        loadConfiguration();
        NeoUtils.setConfigManager(this, configFilePath);
    }

    private void loadConfiguration() {
        saveDefaultConfig();
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), configFilePath);
        } else {
            config = YamlConfiguration.loadConfiguration(configFile);
            return;
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            //Does not work yet, since comments are overridden if something is saved
            //saveDefaultConfig();
            try {
                //Try to create the language.yml config file, and throw an error if it fails.

                if (!configFile.createNewFile()) {
                    Logger.warn("There was an error creating the " + configFilePath + " config file.");
                    return;

                }
            } catch (IOException ioException) {
                Logger.warn("There was an error creating the " + configFilePath + " config file.");
                return;
            }
        }
        config = new YamlConfiguration();
        try {

            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
    public boolean saveConfig(FileConfiguration config) {
        if (config == null || configFile == null) {
            return false;
        }
        try {
            config.save(configFile);
        } catch (IOException e) {
            Logger.fatal("Could not save config to " + configFile.getPath());
            return false;
        }
        return true;
    }
    private void saveDefaultConfig() {
        try {
            if (configFile == null) {
                configFile = new File(plugin.getDataFolder(), configFilePath);
            }
            if (!configFile.exists()) {
                plugin.saveResource(configFilePath, false);
                InputStream inputStream = plugin.getResource(configFilePath);
                //Instead of creating a new language file, we will copy the one from inside the plugin jar into the plugin folder:
                if (inputStream != null) {
                    try (OutputStream outputStream = new FileOutputStream(configFile)) {
                        IOUtils.copy(inputStream, outputStream);
                    } catch (Exception e) {
                        Logger.fatal("There was an error creating the " + configFilePath + " config file. (4)");
                    }
                }
            }
            ConfigUpdater.update(plugin, configFilePath, configFile, List.of(""));
        } catch (IOException ioException) {
            Logger.fatal("There was an error creating the " + configFilePath + " config file. (3)");
        }
    }
    public static void reloadAllConfigs() {
        NeoUtils.getConfigs().forEach((configFileName, configManager) -> configManager.reloadConfig());
    }
}

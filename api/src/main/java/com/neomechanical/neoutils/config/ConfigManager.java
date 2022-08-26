package com.neomechanical.neoutils.config;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.manager.ManagerHandler;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collections;

public class ConfigManager {

    private File configFile = null;
    private FileConfiguration config;
    private final NeoUtils plugin;
    private static final ManagerHandler managers = NeoUtils.getManagers();

    public void reloadConfig() {
        loadConfiguration();
    }

    String configFilePath;

    public ConfigManager(NeoUtils plugin, String configFilePath) {
        this.plugin = plugin;
        this.configFilePath = configFilePath;
        loadConfiguration();
        managers.setConfigManager(this, configFilePath);
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
            // Does not work yet, since comments are overridden if something is saved
            // saveDefaultConfig();
            try {
                // Try to create the language.yml config file, and throw an error if it fails.

                if (!configFile.createNewFile()) {
                    plugin.getFancyLogger().warn("There was an error creating the " + configFilePath + " config file.");
                    return;
                }
            } catch (IOException ioException) {
                plugin.getFancyLogger().warn("There was an error creating the " + configFilePath + " config file.");
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
            plugin.getFancyLogger().fatal("Could not save config to " + configFile.getPath());
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
                // Instead of creating a new language file, we will copy the one from inside the plugin jar into the
                // plugin folder:
                if (inputStream != null) {
                    try (OutputStream outputStream = Files.newOutputStream(configFile.toPath())) {
                        IOUtils.copy(inputStream, outputStream);
                    } catch (Exception e) {
                        plugin.getFancyLogger()
                                .fatal("There was an error creating the " + configFilePath + " config file. (4)");
                    }
                }
            }
            ConfigUpdater.update(plugin, configFilePath, configFile, Collections.singletonList(""));
        } catch (IOException ioException) {
            plugin.getFancyLogger().fatal("There was an error creating the " + configFilePath + " config file. (3)");
        }
    }

    public static void reloadAllConfigs() {
        managers.getConfigs().forEach((configFileName, configManager) -> configManager.reloadConfig());
    }
}

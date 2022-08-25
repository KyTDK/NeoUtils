package com.neomechanical.neoutils.manager;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.commands.CommandManager;
import com.neomechanical.neoutils.config.ConfigManager;
import com.neomechanical.neoutils.inventory.managers.InventoryManager;
import com.neomechanical.neoutils.languages.LanguageManager;
import com.neomechanical.neoutils.versions.VersionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Get and set managers, such as languageManager, configManager
 */
public class ManagerHandler  {

    private final NeoUtils plugin;
    private final VersionManager versionManager;
    private final Map<String, ConfigManager> configManager = new HashMap<>();
    private final CommandManager commandManager = new CommandManager();
    private LanguageManager languageManager;
    private final InventoryManager inventoryManager = new InventoryManager();

    public LanguageManager getLanguageManager() {
        if (languageManager == null) {
            throw new IllegalStateException("Tried to access languageManager, but its not set!");
        }
        return languageManager;
    }
    public ConfigManager getConfigManager(String configName) {
        return configManager.get(configName);
    }
    /**
     * Simple method to create a manager for a specific config file
     *
     * @param configName The config name, must have yml appended
     */
    public ConfigManager createNewConfigManager(String configName) {
        return new ConfigManager(plugin, configName);
    }
    public Map<String, ConfigManager> getConfigs() {
        return configManager;
    }
    public void setLanguageManager(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    public void setConfigManager(ConfigManager configManager, String configName) {
        this.configManager.put(configName, configManager);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ManagerHandler(NeoUtils plugin) {
        this.plugin = plugin;
        this.languageManager = new LanguageManager(plugin);
        this.versionManager = new VersionManager();
    }

    public VersionManager getVersionManager() {
        return versionManager;
    }
}

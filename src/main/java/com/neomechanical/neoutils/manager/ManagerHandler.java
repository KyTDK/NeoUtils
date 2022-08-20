package com.neomechanical.neoutils.manager;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.managers.InventoryManager;

import java.util.HashMap;
import java.util.Map;

public class ManagerHandler  {

    private final NeoUtils plugin;
    public ManagerHandler(NeoUtils plugin) {
        this.plugin = plugin;
        this.languageManager = new LanguageManager(plugin);
    }
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
}

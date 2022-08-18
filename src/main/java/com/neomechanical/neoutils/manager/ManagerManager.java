package com.neomechanical.neoutils.manager;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.commands.CommandManager;
import com.neomechanical.neoutils.config.ConfigManager;
import com.neomechanical.neoutils.languages.LanguageManager;

import java.util.HashMap;
import java.util.Map;

public class ManagerManager {
    private final Map<String, ConfigManager> configManager = new HashMap<>();
    private final CommandManager commandManager = new CommandManager();
    private LanguageManager languageManager = new LanguageManager(NeoUtils.getInstance());

    public LanguageManager getLanguageManager() {
        if (languageManager == null) {
            throw new IllegalStateException("Tried to access languageManager, but its not set!");
        }
        return languageManager;
    }
    public ConfigManager getConfigManager(String configName) {
        if (configManager.get(configName) == null) {
            throw new IllegalStateException("Tried to access " + configName +" in configManager, but its not set!");
        }
        return configManager.get(configName);
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
}

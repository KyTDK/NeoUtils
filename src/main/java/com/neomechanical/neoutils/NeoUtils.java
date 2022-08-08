package com.neomechanical.neoutils;

import com.neomechanical.neoutils.api.Api;
import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.languages.LanguageManager;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

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
    public static void setLanguageManager(LanguageManager languageManager) {
        NeoUtils.languageManager = languageManager;
    }
    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);
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
}

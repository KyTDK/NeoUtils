package com.neomechanical.neoutils;

import com.neomechanical.neoutils.api.Api;
import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.manager.ManagerHandler;
import com.neomechanical.neoutils.messages.Logger;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class NeoUtils extends JavaPlugin implements Api {
    private static BukkitAudiences adventure;
    private static JavaPlugin instance;
    private static ManagerHandler managerHandler;

    public static @NonNull BukkitAudiences adventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }
    @Deprecated
    public static JavaPlugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Tried to access instance when the plugin was disabled, please make sure you call NeoUtils.init(this) in onEnable()!");
        }
        return instance;
    }
    public static ManagerHandler getManagers() {
        if (managerHandler == null) {
            throw new IllegalStateException("Tried to access managerManager when the plugin was disabled!");
        }
        return managerHandler;
    }
    Logger logger;
    public Logger getFancyLogger() {
        return logger;
    }
    @Override
    public void onEnable() {
        instance = this;
        logger = new Logger(this);
        adventure = BukkitAudiences.create(this);
        managerHandler = new ManagerHandler(this);
        getServer().getPluginManager().registerEvents(new InventoryFunctionality(this), this);
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

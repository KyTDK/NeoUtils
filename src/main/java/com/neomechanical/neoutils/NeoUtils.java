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
    private static ManagerHandler managerHandler;
    private static NeoUtils plugin;

    public static @NonNull BukkitAudiences getAdventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    private Logger logger;

    public static ManagerHandler getManagers() {
        if (managerHandler == null) {
            throw new IllegalStateException("Tried to access the manager handler when the plugin was disabled!");
        }
        return managerHandler;
    }

    public static NeoUtils getInstance() {
        return plugin;
    }

    public Logger getFancyLogger() {
        return logger;
    }

    @Override
    public void onEnable() {
        plugin = this;
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

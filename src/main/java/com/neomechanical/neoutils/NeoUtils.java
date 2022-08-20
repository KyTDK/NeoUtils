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

    public static @NonNull BukkitAudiences getAdventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
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

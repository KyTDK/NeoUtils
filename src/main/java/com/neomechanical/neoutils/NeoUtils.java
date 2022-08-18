package com.neomechanical.neoutils;

import com.neomechanical.neoutils.api.Api;
import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.manager.ManagerManager;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class NeoUtils extends JavaPlugin implements Api {
    private static BukkitAudiences adventure;
    private static JavaPlugin instance;
    private static ManagerManager managerManager;

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
    public static ManagerManager getManagers() {
        if (managerManager == null) {
            throw new IllegalStateException("Tried to access managerManager when the plugin was disabled!");
        }
        return managerManager;
    }
    @Override
    public void onEnable() {
        instance = this;
        adventure = BukkitAudiences.create(this);
        managerManager = new ManagerManager();
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

package com.neomechanical.neoutils;

import com.neomechanical.neoutils.api.Api;
import com.neomechanical.neoutils.bungeecord.BungeeCord;
import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.items.ItemInteractionListener;
import com.neomechanical.neoutils.manager.DataHandler;
import com.neomechanical.neoutils.manager.ManagerHandler;
import com.neomechanical.neoutils.messages.Logger;
import com.neomechanical.neoutils.version.VersionMatcher;
import com.neomechanical.neoutils.version.VersionWrapper;
import com.neomechanical.neoutils.version.Versioning;
import com.neomechanical.neoutils.version.items.WrapperLEGACY;
import com.neomechanical.neoutils.version.items.WrapperNONLEGACY;
import com.neomechanical.neoutils.version.v1_10_R1.worlds.WorldWrapper1_10_R1;
import com.neomechanical.neoutils.version.v1_11_R1.worlds.WorldWrapper1_11_R1;
import com.neomechanical.neoutils.version.v1_12_R1.worlds.WorldWrapper1_12_R1;
import com.neomechanical.neoutils.version.v1_13_R1.worlds.WorldWrapper1_13_R1;
import com.neomechanical.neoutils.version.v1_14_4_R1.worlds.WorldWrapper1_14_4_R1;
import com.neomechanical.neoutils.version.v1_14_R1.worlds.WorldWrapper1_14_R1;
import com.neomechanical.neoutils.version.v1_15_R1.worlds.WorldWrapper1_15_R1;
import com.neomechanical.neoutils.version.v1_16_R1.worlds.WorldWrapper1_16_R1;
import com.neomechanical.neoutils.version.v1_16_R2.worlds.WorldWrapper1_16_R2;
import com.neomechanical.neoutils.version.v1_16_R3.worlds.WorldWrapper1_16_R3;
import com.neomechanical.neoutils.version.v1_17_1_R1.worlds.WorldWrapper1_17_1_R1;
import com.neomechanical.neoutils.version.v1_17_R1.worlds.WorldWrapper1_17_R1;
import com.neomechanical.neoutils.version.v1_18_R1.worlds.WorldWrapper1_18_R1;
import com.neomechanical.neoutils.version.v1_18_R2.worlds.WorldWrapper1_18_R2;
import com.neomechanical.neoutils.version.v1_19_1_R1.worlds.WorldWrapper1_19_1_R1;
import com.neomechanical.neoutils.version.v1_19_R1.worlds.WorldWrapper1_19_2_R1;
import com.neomechanical.neoutils.version.v1_19_R1.worlds.WorldWrapper1_19_R1;
import com.neomechanical.neoutils.version.v1_8_R1.worlds.WorldWrapper1_8_R1;
import com.neomechanical.neoutils.version.v1_8_R2.worlds.WorldWrapper1_8_R2;
import com.neomechanical.neoutils.version.v1_8_R3.worlds.WorldWrapper1_8_R3;
import com.neomechanical.neoutils.version.v1_9_R1.worlds.WorldWrapper1_9_R1;
import com.neomechanical.neoutils.version.v1_9_R2.worlds.WorldWrapper1_9_R2;
import com.neomechanical.neoutils.version.versions.Versions;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static com.neomechanical.neoutils.updates.IsUpToDate.isUpToDate;

public abstract class NeoUtils extends JavaPlugin implements Api {
    private static BukkitAudiences adventure;
    private static ManagerHandler managerHandler;
    private static NeoUtils plugin;
    public static DataHandler dataHandler;
    private static BungeeCord bungeeCord;

    public static @NonNull BukkitAudiences getAdventure() {
        if (adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return adventure;
    }

    private static Logger logger;

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

    static Map<String, VersionWrapper> internalVersions;

    public static Map<String, VersionWrapper> getInternalVersions() {
        return internalVersions;
    }

    public static DataHandler getDataHandler() {
        return dataHandler;
    }

    public static void initializeAll(JavaPlugin plugin) {
        logger = new Logger(plugin);
        adventure = BukkitAudiences.create(plugin);
        managerHandler = new ManagerHandler(plugin);
        dataHandler = new DataHandler(plugin);
        //Register plugin channels
        bungeeCord = new BungeeCord(plugin);
        bungeeCord.initialize();
        //Versions
        new Versioning.VersioningBuilder("items")
                .addClass(Versions.vLEGACY.toString(), WrapperLEGACY.class)
                .addClass(Versions.vNONLEGACY.toString(), WrapperNONLEGACY.class)
                .setLegacyFunction((ver) -> !isUpToDate(ver, "1_13_R1"))
                .build()
                .register();
        new Versioning.VersioningBuilder("worlds")
                .addClass(Versions.v1_8_R1.toString(), WorldWrapper1_8_R1.class)
                .addClass(Versions.v1_8_R2.toString(), WorldWrapper1_8_R2.class)
                .addClass(Versions.v1_8_R3.toString(), WorldWrapper1_8_R3.class)
                .addClass(Versions.v1_9_R1.toString(), WorldWrapper1_9_R1.class)
                .addClass(Versions.v1_9_R2.toString(), WorldWrapper1_9_R2.class)
                .addClass(Versions.v1_10_R1.toString(), WorldWrapper1_10_R1.class)
                .addClass(Versions.v1_11_R1.toString(), WorldWrapper1_11_R1.class)
                .addClass(Versions.v1_12_R1.toString(), WorldWrapper1_12_R1.class)
                .addClass(Versions.v1_13_R1.toString(), WorldWrapper1_13_R1.class)
                .addClass(Versions.v1_14_R1.toString(), WorldWrapper1_14_R1.class)
                .addClass(Versions.v1_14_4_R1.toString(), WorldWrapper1_14_4_R1.class)
                .addClass(Versions.v1_15_R1.toString(), WorldWrapper1_15_R1.class)
                .addClass(Versions.v1_16_R1.toString(), WorldWrapper1_16_R1.class)
                .addClass(Versions.v1_16_R2.toString(), WorldWrapper1_16_R2.class)
                .addClass(Versions.v1_16_R3.toString(), WorldWrapper1_16_R3.class)
                .addClass(Versions.v1_17_R1.toString(), WorldWrapper1_17_R1.class)
                .addClass(Versions.v1_17_1_R1.toString(), WorldWrapper1_17_1_R1.class)
                .addClass(Versions.v1_18_R1.toString(), WorldWrapper1_18_R1.class)
                .addClass(Versions.v1_18_R2.toString(), WorldWrapper1_18_R2.class)
                .addClass(Versions.v1_19_R1.toString(), WorldWrapper1_19_R1.class)
                .addClass(Versions.v1_19_1_R1.toString(), WorldWrapper1_19_1_R1.class)
                .addClass(Versions.v1_19_2_R1.toString(), WorldWrapper1_19_2_R1.class)
                .build()
                .register();
        internalVersions = new VersionMatcher(getManagers().getVersionManager()).matchAll();
        plugin.getServer().getPluginManager().registerEvents(new InventoryFunctionality(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemInteractionListener(), plugin);
    }

    public BungeeCord getBungeeCord() {
        return bungeeCord;
    }

    @Override
    public void onEnable() {
        plugin = this;
        initializeAll(this);
        this.onPluginEnable();
    }

    @Override
    public void onDisable() {
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        //make sure to unregister the registered channels in case of a reload
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);

        this.onPluginDisable();
    }
}

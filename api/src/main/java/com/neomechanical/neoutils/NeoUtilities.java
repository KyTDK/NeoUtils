package com.neomechanical.neoutils;

import com.neomechanical.neoutils.bungeecord.BungeeCord;
import com.neomechanical.neoutils.manager.DataHandler;
import com.neomechanical.neoutils.manager.ManagerHandler;
import com.neomechanical.neoutils.messages.Logger;
import com.neomechanical.neoutils.version.VersionMatcher;
import com.neomechanical.neoutils.version.VersionWrapper;
import com.neomechanical.neoutils.version.Versioning;
import com.neomechanical.neoutils.version.items.ItemEventHandlerLEGACY;
import com.neomechanical.neoutils.version.items.ItemEventHandlerNONLEGACY;
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
import org.bukkit.plugin.Plugin;

import java.util.Map;

import static com.neomechanical.neoutils.updates.IsUpToDate.isUpToDate;

public class NeoUtilities {
    private static DataHandler dataHandler;
    private static BungeeCord bungeeCord;
    private static BukkitAudiences adventure;
    private static ManagerHandler managerHandler;
    private static Map<String, VersionWrapper> internalVersions;
    private static Logger logger;
    private static ServerMetrics serverMetrics;
    private final Plugin plugin;
    public NeoUtilities(Plugin plugin) {
        this.plugin = plugin;
        logger = new Logger(plugin);
        adventure = BukkitAudiences.create(plugin);
        managerHandler = new ManagerHandler(plugin);
        dataHandler = new DataHandler();
        //Register plugin channels
        bungeeCord = new BungeeCord(plugin);
        bungeeCord.initialize();
        //Set metrics
        serverMetrics = new ServerMetrics();
        //Versions
    }

    //Run init after this is set so Versioning doesn't throw an error
    public void init() {
        new Versioning.VersioningBuilder("items")
                .addClass(Versions.vLEGACY.toString(), WrapperLEGACY.class)
                .addClass(Versions.vNONLEGACY.toString(), WrapperNONLEGACY.class)
                .setLegacyFunction((ver) -> !isUpToDate(ver, Versions.v1_13_R1.toString()))
                .build()
                .register();
//        new Versioning.VersioningBuilder("worlds")
//                .addClass(Versions.v1_8_R1.toString(), WorldWrapper1_8_R1.class)
//                .addClass(Versions.v1_8_R2.toString(), WorldWrapper1_8_R2.class)
//                .addClass(Versions.v1_8_R3.toString(), WorldWrapper1_8_R3.class)
//                .addClass(Versions.v1_9_R1.toString(), WorldWrapper1_9_R1.class)
//                .addClass(Versions.v1_9_R2.toString(), WorldWrapper1_9_R2.class)
//                .addClass(Versions.v1_10_R1.toString(), WorldWrapper1_10_R1.class)
//                .addClass(Versions.v1_11_R1.toString(), WorldWrapper1_11_R1.class)
//                .addClass(Versions.v1_12_R1.toString(), WorldWrapper1_12_R1.class)
//                .addClass(Versions.v1_13_R1.toString(), WorldWrapper1_13_R1.class)
//                .addClass(Versions.v1_14_R1.toString(), WorldWrapper1_14_R1.class)
//                .addClass(Versions.v1_14_4_R1.toString(), WorldWrapper1_14_4_R1.class)
//                .addClass(Versions.v1_15_R1.toString(), WorldWrapper1_15_R1.class)
//                .addClass(Versions.v1_16_R1.toString(), WorldWrapper1_16_R1.class)
//                .addClass(Versions.v1_16_R2.toString(), WorldWrapper1_16_R2.class)
//                .addClass(Versions.v1_16_R3.toString(), WorldWrapper1_16_R3.class)
//                .addClass(Versions.v1_17_R1.toString(), WorldWrapper1_17_R1.class)
//                .addClass(Versions.v1_17_1_R1.toString(), WorldWrapper1_17_1_R1.class)
//                .addClass(Versions.v1_18_R1.toString(), WorldWrapper1_18_R1.class)
//                .addClass(Versions.v1_18_R2.toString(), WorldWrapper1_18_R2.class)
//                .addClass(Versions.v1_19_R1.toString(), WorldWrapper1_19_R1.class)
//                .addClass(Versions.v1_19_1_R1.toString(), WorldWrapper1_19_1_R1.class)
//                .addClass(Versions.v1_19_2_R1.toString(), WorldWrapper1_19_2_R1.class)
//                .build()
//                .register();
        new Versioning.VersioningBuilder("specialItemInteractions")
                .addClass(Versions.vLEGACY.toString(), ItemEventHandlerLEGACY.class)
                .addClass(Versions.vNONLEGACY.toString(), ItemEventHandlerNONLEGACY.class)
                .setLegacyFunction((ver) -> !isUpToDate(ver, Versions.v1_9_R1.toString()))
                .build()
                .register();
        internalVersions = new VersionMatcher(managerHandler.getVersionManager()).matchAll();
    }

    public @NonNull BukkitAudiences getAdventure() {
        if (adventure == null) {
            return BukkitAudiences.create(plugin);
        }
        return adventure;
    }

    public ServerMetrics getServerMetrics() {
        return serverMetrics;
    }

    public ManagerHandler getManagers() {
        if (managerHandler == null) {
            return new ManagerHandler(plugin);
        }
        return managerHandler;
    }

    public Logger getFancyLogger() {
        return logger;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public BungeeCord getBungeeCord() {
        return bungeeCord;
    }

    public Map<String, VersionWrapper> getInternalVersions() {
        return internalVersions;
    }
}

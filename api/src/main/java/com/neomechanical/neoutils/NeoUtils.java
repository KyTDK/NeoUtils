package com.neomechanical.neoutils;

import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.items.ItemInteractionListener;
import com.neomechanical.neoutils.version.items.IItemEventHandlerWrapper;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class NeoUtils extends JavaPlugin {
    private static Plugin plugin;
    private static NeoUtilities neoUtilities;

    public static Plugin getInstance() {
        if (plugin == null) {
            try {
                plugin = JavaPlugin.getProvidingPlugin(Class.forName(Thread.currentThread().getStackTrace()[3].getClassName()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (plugin == null) {
                throw new IllegalStateException("Cannot find plugin instance");
            }
            init();
        }

        return plugin;
    }

    public static NeoUtilities getNeoUtilities() {
        if (neoUtilities == null) {
            init();
        }
        return neoUtilities;
    }

    public static void init() {
        neoUtilities = new NeoUtilities(getInstance());
        plugin.getServer().getPluginManager().registerEvents(new InventoryFunctionality(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemInteractionListener((IItemEventHandlerWrapper) getNeoUtilities().getInternalVersions().get("specialItemInteractions")), plugin);
    }

    @Override
    public void onEnable() {
        plugin = this;
        init();
    }

    @Override
    public void onDisable() {
        BukkitAudiences adventure = getNeoUtilities().getAdventure();
        adventure.close();
        //make sure to unregister the registered channels in case of a reload
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}

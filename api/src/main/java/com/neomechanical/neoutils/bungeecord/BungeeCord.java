package com.neomechanical.neoutils.bungeecord;

import org.bukkit.plugin.Plugin;

public class BungeeCord {
    private final Plugin plugin;
    private PluginMessageReceiver pluginMessageReceiver;
    private PluginMessageBroker broker;

    public BungeeCord(Plugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        broker = new PluginMessageBroker(plugin);
        pluginMessageReceiver = new PluginMessageReceiver(broker);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", pluginMessageReceiver);
    }

    public PluginMessageReceiver getPluginMessageReceiver() {
        return pluginMessageReceiver;
    }

    public PluginMessageBroker getPluginMessageBroker() {
        return broker;
    }
}

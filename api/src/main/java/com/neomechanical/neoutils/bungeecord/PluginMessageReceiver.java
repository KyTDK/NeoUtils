package com.neomechanical.neoutils.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PluginMessageReceiver implements PluginMessageListener {
    private final CompletableFuture<ByteArrayDataInput> result = new CompletableFuture<>();
    private final PluginMessageBroker broker;

    public CompletableFuture<ByteArrayDataInput> getResult() {
        return result;
    }

    public PluginMessageReceiver(PluginMessageBroker broker) {
        this.broker = broker;
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        broker.consume(subChannel, player, message);
    }
}

package com.neomechanical.neoutils.bungeecord;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class SendAndReceive {
    public static CompletableFuture<ByteArrayDataInput> send(String channel, String message) {
        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(message);
        player.sendPluginMessage(NeoUtils.getInstance(), "BungeeCord", out.toByteArray());
        return NeoUtils.getInstance().getPluginMessageReceiver().getResult();
    }
}

package com.neomechanical.neoutils.bungeecord;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class SendAndReceive {
    public static CompletableFuture<byte[]> send(@Nullable Player player, String channel, String message) {
        //If the player is null then just pick a random one
        if (player == null) {
            player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(channel);
        out.writeUTF(message);
        player.sendPluginMessage(NeoUtils.getInstance(), "BungeeCord", out.toByteArray());
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        NeoUtils.getInstance().getBungeeCord().getPluginMessageBroker().request(player, channel, (rec, msg) ->
                future.complete(msg));
        return future;
    }
}

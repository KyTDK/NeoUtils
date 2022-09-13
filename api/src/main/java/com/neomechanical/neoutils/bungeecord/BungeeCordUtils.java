package com.neomechanical.neoutils.bungeecord;

import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class BungeeCordUtils {
    private BungeeCordUtils() {
    }

    public static void connect(Player player, String server) {
        SendAndReceive.send(player, "Connect", server);
    }

    public static CompletableFuture<byte[]> playerCount(String server) {
        return SendAndReceive.send("PlayerCount", server);
    }
}

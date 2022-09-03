package com.neomechanical.neoutils.bungeecord;

import org.bukkit.entity.Player;

import java.util.concurrent.Future;

public class BungeeCordUtils {
    private BungeeCordUtils() {
    }

    public static void connect(Player player, String server) {
        SendAndReceive.send(player, "Connect", server);
    }

    public static Future<byte[]> playerCount(String server) {
        return SendAndReceive.send(null, "PlayerCount", server);
    }
}

package com.neomechanical.neoutils.bungeecord;

import com.google.common.io.ByteArrayDataInput;
import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.concurrent.CompletableFuture;

public class BungeeCordUtils {
    private BungeeCordUtils() {
    }

    public static void connect(Player player, String server) {
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(NeoUtils.getInstance(), "BungeeCord");

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(NeoUtils.getInstance(), "BungeeCord", b.toByteArray());
    }

    public static CompletableFuture<ByteArrayDataInput> playerCount(String server) {
        CompletableFuture<String> playerCount = new CompletableFuture<>();
        return SendAndReceive.send("PlayerCount", server);
    }
}

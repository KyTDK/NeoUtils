package com.neomechanical.neoutils.bungeecord;

import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

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
}

package com.neomechanical.neoutils.messages;

import com.neomechanical.neoutils.NeoUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

public final class MessageUtil {

    private static final BukkitAudiences adventure = NeoUtils.getAdventure();

    /**
     * A utility class for handling Bukkit messages.
     */
    public MessageUtil() {}

        /**
         * Translate an uncolored message.
         *
         * @param msg the msg
         * @return the colored message
         */
        @Contract("null -> null")
        public static String color(String msg) {
            return msg == null ? null : ChatColor.translateAlternateColorCodes('&', msg);
        }

    static List<Component> neoComponentArray = new ArrayList<>();

    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }

    public static void sendMM(CommandSender sendTo, Component parsed) {
        Audience player = adventure.sender(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMM(Player sendTo, Component parsed) {
        Audience player = adventure.player(sendTo);
        player.sendMessage(parsed);
    }
    public static void sendMM(Player sendTo, TextComponent parsed) {
        Audience player = adventure.player(sendTo);
        player.sendMessage(parsed);
    }
    public static void sendMM(CommandSender sendTo, TextComponent parsed) {
        Audience player = adventure.sender(sendTo);
        player.sendMessage(parsed);
    }
    public static void sendMM(CommandSender sendTo, String msg) {
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(msg);
        Audience player = adventure.sender(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMMAll(String string) {
        Audience player = NeoUtils.getAdventure().all();
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(string);
        player.sendMessage(parsed);
    }

    public static void sendMMAdmins(String string) {
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(string);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                Audience audience = adventure.player(player);
                audience.sendMessage(parsed);
            }
        }
    }

    public MessageUtil addComponent(String msg) {
        MiniMessage mm = MiniMessage.miniMessage();
        Component parsed = mm.deserialize(msg);
        neoComponentArray.add(parsed);
        return this;
    }
    public MessageUtil addComponent(TextComponent msg) {
        neoComponentArray.add(msg);
        return this;
    }

    public MessageUtil neoComponentMessage() {
        return this;
    }
    
    public void sendNeoComponentMessage(CommandSender player, String prefix, String suffix) {
        player.sendMessage(color(prefix));
        for (Component msg : neoComponentArray) {
            sendMM(player, msg);
        }
        player.sendMessage(color(suffix));
        neoComponentArray.clear();
    }
    public void sendNeoComponentMessage(Player player, String prefix, String suffix) {
        player.sendMessage(color(prefix));
        for (Component msg : neoComponentArray) {
            sendMM(player, msg);
        }
        player.sendMessage(color(suffix));
        neoComponentArray.clear();
    }
    public void sendNeoComponentMessage(Player player) {
        for (Component msg : neoComponentArray) {
            sendMM(player, msg);
        }
        neoComponentArray.clear();
    }
    public void sendNeoComponentMessage(CommandSender player) {
        for (Component msg : neoComponentArray) {
            sendMM(player, msg);
        }
        neoComponentArray.clear();
    }
}

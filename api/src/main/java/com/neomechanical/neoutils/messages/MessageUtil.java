package com.neomechanical.neoutils.messages;

import com.neomechanical.neoutils.NeoUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class MessageUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Audience ALL_PLAYERS = NeoUtils.getNeoUtilities().getAdventure().all();

    private final List<Component> neoComponentArray = new ArrayList<>();

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
    public static String color(String msg) {
        return msg == null ? null : ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String parse(String msg) {
        return MINI_MESSAGE.deserialize(msg).toString();
    }

    public static Component parseComponent(String msg) {
        return MINI_MESSAGE.deserialize(msg);
    }

    public static void send(Player player, String message) {
        player.sendMessage(color(message));
    }

    public static void sendMM(CommandSender sendTo, Component parsed) {
        Audience player = NeoUtils.getNeoUtilities().getAdventure().sender(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMM(Player sendTo, Component parsed) {
        Audience player = NeoUtils.getNeoUtilities().getAdventure().player(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMM(CommandSender sendTo, TextComponent parsed) {
        Audience player = NeoUtils.getNeoUtilities().getAdventure().sender(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMM(Player sendTo, TextComponent parsed) {
        Audience player = NeoUtils.getNeoUtilities().getAdventure().player(sendTo);
        player.sendMessage(parsed);
    }

    public static void sendMM(CommandSender sendTo, String msg) {
        sendMM(sendTo, parseComponent(msg));
    }

    public static void sendMMAll(String string) {
        ALL_PLAYERS.sendMessage(parseComponent(string));
    }

    public static void sendMMAdmins(String string) {
        Component parsed = parseComponent(string);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                NeoUtils.getNeoUtilities().getAdventure().player(player).sendMessage(parsed);
            }
        }
    }

    public MessageUtil addComponent(String msg) {
        neoComponentArray.add(parseComponent(msg));
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
        neoComponentArray.forEach(msg -> sendMM(player, msg));
        player.sendMessage(color(suffix));
        neoComponentArray.clear();
    }

    public void sendNeoComponentMessage(Player player, String prefix, String suffix) {
        player.sendMessage(color(prefix));
        neoComponentArray.forEach(msg -> sendMM(player, msg));
        player.sendMessage(color(suffix));
        neoComponentArray.clear();
    }

    public void sendNeoComponentMessage(Player player) {
        neoComponentArray.forEach(msg -> sendMM(player, msg));
        neoComponentArray.clear();
    }

    public void sendNeoComponentMessage(CommandSender player) {
        neoComponentArray.forEach(msg -> sendMM(player, msg));
        neoComponentArray.clear();
    }

    public static void sendConsole(String message) {
        Audience console = NeoUtils.getNeoUtilities().getAdventure().console();
        console.sendMessage(parseComponent(message));
    }
}

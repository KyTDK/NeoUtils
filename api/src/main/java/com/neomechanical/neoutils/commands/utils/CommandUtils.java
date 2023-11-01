package com.neomechanical.neoutils.commands.utils;

import com.neomechanical.neoutils.commands.Command;
import com.neomechanical.neoutils.commands.CommandBuilder;
import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandUtils {
    private CommandUtils() {
    }
    public static Command getSubcommand(Command root, String[] args) {
        if (args.length > 1) {
            ArrayDeque<String> argStack = new ArrayDeque<>(Arrays.asList(args));
            Iterator<String> value = argStack.iterator();
            Command previousCommand = root;
            while (value.hasNext()) {
                String currentArg = value.next();
                List<Command> subcommands = previousCommand.getSubcommands();
                if (subcommands != null) {
                    for (Command subcommand : subcommands) {
                        if (subcommand.getName().equalsIgnoreCase(currentArg)) {
                            previousCommand = subcommand;
                        }
                    }
                }
            }
            return previousCommand;
        }
        return null;
    }

    public static boolean hasPermission(CommandSender sender, Command command) {
        if (command.getPermission() == null) {
            return true;
        } else return sender.hasPermission(command.getPermission());
    }

    public static boolean commandCanRun(CommandSender sender, Command command, CommandBuilder commandBuilder) {
        if (command.playerOnly() && !(sender instanceof Player)) {
            MessageUtil.sendMM(sender, commandBuilder.errorNotPlayer.get());
            return false;
        }
        if (hasPermission(sender, command)) {
            return true;
        } else {
            MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
            return false;
        }
    }
}
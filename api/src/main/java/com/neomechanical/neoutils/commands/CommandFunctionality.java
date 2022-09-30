package com.neomechanical.neoutils.commands;


import com.neomechanical.neoutils.commands.utils.CommandUtils;
import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandFunctionality implements CommandExecutor, TabCompleter {
    private final CommandBuilder commandBuilder;

    public CommandFunctionality(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull org.bukkit.command.Command command,
            @NotNull String label,
            @NotNull String[] args) {
        if (args.length > 0) {
            ArrayList<Command> commands = commandBuilder.getCommands();
            for (Command neoCommand : commands) {
                if (args[0].equalsIgnoreCase(
                        neoCommand.getName())) {
                    if (neoCommand.playerOnly() && !(sender instanceof Player)) {
                        MessageUtil.sendMM(sender, commandBuilder.errorNotPlayer.get());
                        return true;
                    }
                    if (sender.hasPermission(
                            neoCommand.getPermission())) {
                        if (args.length > 1) {
                            Command subcommand = CommandUtils.getSubcommand(neoCommand, args);
                            if (subcommand != null && sender.hasPermission(subcommand.getPermission())) {
                                subcommand.perform(sender, args);
                                return true;
                            }
                        }
                        neoCommand.perform(sender, args);
                    } else {
                        MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
                    }
                    return true;
                }
            }
            // If the command is not found, send a message to the player
            MessageUtil.sendMM(sender, commandBuilder.errorCommandNotFound.get());
            return true;
        } else {
            if (commandBuilder.mainCommand.playerOnly() && !(sender instanceof Player)) {
                MessageUtil.sendMM(sender, commandBuilder.errorNotPlayer.get());
                return true;
            }
            if (sender.hasPermission(commandBuilder.mainCommand.getPermission())) {
                commandBuilder.mainCommand.perform(sender, args);
            } else {
                MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
            }
        }

        return true;
    }

    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull org.bukkit.command.Command command,
            @NotNull String alias,
            @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < commandBuilder.getCommands().size(); i++) {
            Command neoCommand = commandBuilder.getCommands().get(i);
            if (!sender.hasPermission(neoCommand.getPermission())) {
                return null;
            }
            if (args.length == 1) {
                list.add(neoCommand.getName());
            } else if (args.length >= 2) {
                if (args[0].equalsIgnoreCase(
                        commandBuilder.getCommands().get(i).getName())) {
                    List<String> suggestions =
                            commandBuilder.getCommands().get(i).tabSuggestions();
                    Map<String, List<String>> mapSuggestions =
                            commandBuilder.getCommands().get(i).mapSuggestions();
                    List<String> listArgs = new ArrayList<>(Arrays.asList(args));
                    String currentArg = listArgs.get(listArgs.size() - 2);
                    List<Command> annotatedSubcommands = CommandUtils.getSubcommands(neoCommand.getClass());
                    if (annotatedSubcommands != null) {
                        for (Command subcommand : annotatedSubcommands) {
                            list.add(subcommand.getName());
                        }
                    }
                    if (mapSuggestions != null && mapSuggestions.containsKey(currentArg)) {
                        list.addAll(mapSuggestions.get(currentArg));
                    } else if (suggestions != null) {
                        list.addAll(suggestions);
                    } else {
                        return null;
                    }
                }
            }
        }
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], list, completions);
        // sort the list
        Collections.sort(completions);
        return completions;
    }
}

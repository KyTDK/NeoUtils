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
                if (args[0].equalsIgnoreCase(neoCommand.getName())) {
                    if (commandCanRun(sender, neoCommand)) {
                        if (args.length > 1) {
                            Command subcommand = CommandUtils.getSubcommand(neoCommand, args);
                            if (subcommand != null && commandCanRun(sender, subcommand)) {
                                subcommand.perform(sender, args);
                            }
                        } else {
                            neoCommand.perform(sender, args);
                        }
                    }
                    return true;
                }
            }
            // If the command is not found, send a message to the player
            MessageUtil.sendMM(sender, commandBuilder.errorCommandNotFound.get());
            return true;
        } else if (commandCanRun(sender, commandBuilder.mainCommand)) {
            MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
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
            if (!hasPermission(sender, neoCommand)) {
                return null;
            }
            if (args.length == 1) {
                list.add(neoCommand.getName());
            } else if (args.length >= 2) {
                if (args[0].equalsIgnoreCase(neoCommand.getName())) {
                    List<String> listArgs = new ArrayList<>(Arrays.asList(args));
                    String currentArg = listArgs.get(listArgs.size() - 2);
                    //Add subcommands to tab completion list
                    Command subcommand = CommandUtils.getSubcommand(neoCommand, args);
                    if (subcommand != null) {
                        List<Command> subcommands = subcommand.getSubcommands();
                        if (subcommands != null) {
                            for (Command subSubcommand : subcommands) {
                                if (subSubcommand.getName() != null && subSubcommand.tabComplete) {
                                    list.add(subSubcommand.getName());
                                }
                            }
                        }
                    }
                    Map<String, List<String>> mapSuggestions = neoCommand.mapSuggestions();
                    if (mapSuggestions != null && mapSuggestions.containsKey(currentArg)) {
                        list.addAll(mapSuggestions.get(currentArg));
                    }
                    List<String> suggestions = neoCommand.tabSuggestions();
                    if (suggestions != null) {
                        list.addAll(suggestions);
                    }
                }
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], list, completions);
        // sort the list
        Collections.sort(completions);
        return completions;
    }

    private boolean hasPermission(CommandSender sender, Command command) {
        if (command.getPermission() == null) {
            return true;
        } else return sender.hasPermission(command.getPermission());
    }

    private boolean commandCanRun(CommandSender sender, Command command) {
        if (command.playerOnly() && !(sender instanceof Player)) {
            MessageUtil.sendMM(sender, commandBuilder.errorNotPlayer.get());
            return true;
        }
        if (hasPermission(sender, command)) {
            return true;
        } else {
            MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
            return false;
        }
    }
}

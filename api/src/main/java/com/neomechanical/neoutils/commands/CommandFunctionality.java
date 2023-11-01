package com.neomechanical.neoutils.commands;


import com.neomechanical.neoutils.commands.utils.CommandUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.neomechanical.neoutils.commands.utils.CommandUtils.commandCanRun;
import static com.neomechanical.neoutils.commands.utils.CommandUtils.hasPermission;

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
        ArrayList<Command> commands = commandBuilder.getCommands();
        if (args.length > 0) {
            for (Command neoCommand : commands) {
                if (args[0].equalsIgnoreCase(neoCommand.getName())) {
                    if (commandCanRun(sender, neoCommand, commandBuilder)) {
                        if (args.length > 1) {
                            Command subcommand = CommandUtils.getSubcommand(neoCommand, args);
                            if (subcommand != null && commandCanRun(sender, subcommand, commandBuilder)) {
                                subcommand.perform(sender, args);
                            } else {
                                neoCommand.perform(sender, args);
                            }
                        } else {
                            neoCommand.perform(sender, args);
                        }
                    }
                    return true;
                }
            }
        }
        if (commandCanRun(sender, commandBuilder.mainCommand, commandBuilder)) {
            commandBuilder.mainCommand.perform(sender, args);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        List<String> listArgs = new ArrayList<>(Arrays.asList(args));
        String currentArg = args.length > 1 ?
                listArgs.get(listArgs.size() - 2) : alias;
        if (args.length == 1) {
            Command neoCommand = commandBuilder.mainCommand;
            list.addAll(getCompletions(neoCommand, sender, currentArg, args));
        }
        if (args.length >= 2) {
            Command subcommand = CommandUtils.getSubcommand(commandBuilder.mainCommand, args);
            if (subcommand != null) {
                list.addAll(getCompletions(subcommand, sender, currentArg, args));
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

    public List<String> getCompletions(Command command, CommandSender sender, String currentArg, String[] args) {
        List<String> list = new ArrayList<>();
        if (!hasPermission(sender, command)) {
            return null;
        }
        //Map suggestions isolated all other suggestions
        Map<String, List<String>> mapSuggestions = command.mapSuggestions();
        if (mapSuggestions != null && mapSuggestions.containsKey(currentArg)) {
            return mapSuggestions.get(currentArg);
        }
        if (mapSuggestions != null) {
            list.addAll(mapSuggestions.keySet());
        }
        List<String> suggestions = command.tabSuggestions();
        if (suggestions != null) {
            list.addAll(suggestions);
        }
        //Add immediate subcommands
        {
            List<Command> subcommands = command.getSubcommands();
            if (subcommands != null) {
                for (Command commandFromBuilder : subcommands) {
                    list.add(commandFromBuilder.getName());
                }
            }
        }
        //Add subcommands to tab completion list
        Command subcommand = CommandUtils.getSubcommand(command, args);
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
        return list;
    }
}

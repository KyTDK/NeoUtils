package com.neomechanical.neoutils.commandManager;

import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommandFunctionality implements CommandExecutor, TabCompleter{
    private final CommandBuilder commandBuilder;

    public CommandBuilder createCommand(String parentCommand) {
        return new CommandBuilder(parentCommand);
    }
    public CommandFunctionality(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }
    @SuppressWarnings("unused")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
                    for (int i = 0; i < commandBuilder.getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(commandBuilder.getSubcommands().get(i).getName())) {
                            if (commandBuilder.getSubcommands().get(i).playerOnly() && !(sender instanceof Player)) {
                                MessageUtil.sendMM(sender, commandBuilder.errorNotPlayer.get());
                                return true;
                            }
                            if (sender.hasPermission(commandBuilder.getSubcommands().get(i).getPermission())) {
                                commandBuilder.getSubcommands().get(i).perform(sender, args);
                            } else {
                                MessageUtil.sendMM(sender, commandBuilder.errorNoPermission.get());
                            }
                            return true;
                        }
                    }
                    //If the command is not found, send a message to the player
            MessageUtil.sendMM(sender, commandBuilder.errorCommandNotFound.get());
                    return true;
                }
                else {
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
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
            for (int i = 0; i < commandBuilder.getSubcommands().size(); i++) {
                Command neoCommand = commandBuilder.getSubcommands().get(i);
                if (!sender.hasPermission(neoCommand.getPermission())) {
                    return null;
                }
                if (args.length == 1) {
                    list.add(neoCommand.getName());
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase(commandBuilder.getSubcommands().get(i).getName())) {
                        List<String> suggestions = commandBuilder.getSubcommands().get(i).tabSuggestions();
                        Map<String, List<String>> mapSuggestions = commandBuilder.getSubcommands().get(i).mapSuggestions();
                        List<String> listArgs = List.of(args);
                        String currentArg = listArgs.get(listArgs.size() - 2);
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
        StringUtil.copyPartialMatches(args[args.length-1], list, completions);
        //sort the list
        Collections.sort(completions);
        return completions;
    }

}

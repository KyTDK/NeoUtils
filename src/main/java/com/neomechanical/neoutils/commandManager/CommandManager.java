package com.neomechanical.neoutils.commandManager;

import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

public class CommandManager implements CommandExecutor, TabCompleter{
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();
    private Command mainCommand;
    private Supplier<String> errorNotPlayer = () -> "You must be a player to use this command";
    private Supplier<String> errorNoPermission = () -> "You do not have permission to use this command!";
    private Supplier<String> errorCommandNotFound = () -> "Command not found!";
    @SuppressWarnings("unused")
    public CommandManager(JavaPlugin plugin, String parentCommand, SubCommand... subcommandsPass) {
        Collections.addAll(this.subcommands, subcommandsPass);
        Objects.requireNonNull(plugin.getCommand(parentCommand)).setExecutor(this);
    }
    @SuppressWarnings("unused")
    public void registerMainCommand(Command command) {
        mainCommand = command;
    }
    @SuppressWarnings("unused")
    public void setErrorNotPlayer(Supplier<String> messageSupplier) {
        this.errorNotPlayer = messageSupplier;
    }
    @SuppressWarnings("unused")
    public void setErrorNoPermission(Supplier<String> messageSupplier) {
        this.errorNoPermission = messageSupplier;
    }
    @SuppressWarnings("unused")
    public void setErrorCommandNotFound(Supplier<String> messageSupplier) {
        this.errorCommandNotFound = messageSupplier;
    }
    @SuppressWarnings("unused")
    public void registerSubCommand(SubCommand subcommand) {
        subcommands.add(subcommand);
    }
    @SuppressWarnings("unused")
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                            if (getSubcommands().get(i).playerOnly() && !(sender instanceof Player)) {
                                MessageUtil.sendMM(sender, errorNotPlayer.get());
                                return true;
                            }
                            if (sender.hasPermission(getSubcommands().get(i).getPermission())) {
                                getSubcommands().get(i).perform(sender, args);
                            } else {
                                MessageUtil.sendMM(sender, errorNoPermission.get());
                            }
                            return true;
                        }
                    }
                    //If the command is not found, send a message to the player
            MessageUtil.sendMM(sender, errorCommandNotFound.get());
                    return true;
                }
                else {
                if (mainCommand.playerOnly() && !(sender instanceof Player)) {
                    MessageUtil.sendMM(sender, errorNotPlayer.get());
                    return true;
                }
                if (sender.hasPermission(mainCommand.getPermission())) {
                    mainCommand.perform(sender, args);
                } else {
                    MessageUtil.sendMM(sender, errorNoPermission.get());
                }
            }


        return true;
    }
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
            for (int i = 0; i < getSubcommands().size(); i++) {
                SubCommand subCommand = getSubcommands().get(i);
                if (!sender.hasPermission(subCommand.getPermission())) {
                    return null;
                }
                if (args.length == 1) {
                    list.add(subCommand.getName());
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        List<String> suggestions = getSubcommands().get(i).tabSuggestions();
                        Map<String, List<String>> mapSuggestions = getSubcommands().get(i).mapSuggestions();
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
    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

}

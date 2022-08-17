package com.neomechanical.neoutils.commandManager;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public abstract class Command {
    public abstract List<Command> getSubcommands();

    public Command addSubcommand(Command... command) {
        getSubcommands().addAll(List.of(command));
        return this;
    }
    public Command removeSubcommand(Command... command) {
        getSubcommands().removeAll(List.of(command));
        return this;
    }
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract String getPermission();

    public abstract boolean playerOnly();

    //code for the subcommand
    public abstract void perform(CommandSender player, String[] args);

    public abstract List<String> tabSuggestions();

    public abstract Map<String, List<String>> mapSuggestions();
}

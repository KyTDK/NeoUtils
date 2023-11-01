package com.neomechanical.neoutils.commands;


import org.bukkit.command.CommandSender;

import java.util.*;

public abstract class Command {
    private final ArrayList<Command> subcommands = new ArrayList<>();
    boolean tabComplete = true;

    public Command tabCompleteName(boolean value) {
        tabComplete = value;
        return this;
    }

    public List<Command> getSubcommands() {
        return subcommands;
    }

    public Command addSubcommand(Command... command) {
        getSubcommands().addAll(new ArrayList<>(Arrays.asList(command)));
        return this;
    }

    public Command removeSubcommand(Command... command) {
        getSubcommands().removeAll((new ArrayList<>(Arrays.asList(command))));
        return this;
    }

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public String getPermission() {
        return null;
    }

    public abstract boolean playerOnly();

    // code for the subcommand
    public abstract void perform(CommandSender player, String[] args);

    public List<String> tabSuggestions() {
        return Collections.emptyList();
    }

    public Map<String, List<String>> mapSuggestions() {
        return Collections.emptyMap();
    }
}

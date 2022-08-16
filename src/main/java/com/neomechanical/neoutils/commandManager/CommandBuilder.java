package com.neomechanical.neoutils.commandManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.function.Supplier;

public class CommandBuilder {
    String parentCommand;
    public CommandBuilder(String parentCommand) {
        this.parentCommand = parentCommand;
    }
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();
    public Command mainCommand;
    public Supplier<String> errorNotPlayer = () -> "You must be a player to use this command";
    public Supplier<String> errorNoPermission = () -> "You do not have permission to use this command!";
    public Supplier<String> errorCommandNotFound = () -> "Command not found!";
    CommandFunctionality functionality = new CommandFunctionality(this);
    @SuppressWarnings("unused")
    public CommandBuilder(JavaPlugin plugin, String parentCommand, SubCommand... subcommandsPass) {
        Collections.addAll(this.subcommands, subcommandsPass);
        Objects.requireNonNull(plugin.getCommand(parentCommand)).setExecutor(functionality);
    }
    @SuppressWarnings("unused")
    public void registerMainCommand(Command command) {
        mainCommand = command;
    }
    @SuppressWarnings("unused")
    public CommandBuilder setErrorNotPlayer(Supplier<String> messageSupplier) {
        this.errorNotPlayer = messageSupplier;
        return this;
    }
    @SuppressWarnings("unused")
    public CommandBuilder setErrorNoPermission(Supplier<String> messageSupplier) {
        this.errorNoPermission = messageSupplier;
        return this;
    }
    @SuppressWarnings("unused")
    public CommandBuilder setErrorCommandNotFound(Supplier<String> messageSupplier) {
        this.errorCommandNotFound = messageSupplier;
        return this;
    }
    @SuppressWarnings("unused")
    public CommandBuilder register(SubCommand subcommand) {
        subcommands.add(subcommand);
        return this;
    }
    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }
}

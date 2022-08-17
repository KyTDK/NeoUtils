package com.neomechanical.neoutils.commandManager;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Supplier;

public class CommandBuilder {
    String parentCommand;
    public CommandBuilder(String parentCommand) {
        this.parentCommand = parentCommand;
    }
    public Command mainCommand;
    public Supplier<String> errorNotPlayer = () -> "You must be a player to use this command";
    public Supplier<String> errorNoPermission = () -> "You do not have permission to use this command!";
    public Supplier<String> errorCommandNotFound = () -> "Command not found!";
    CommandFunctionality functionality = new CommandFunctionality(this);
    @SuppressWarnings("unused")
    public CommandBuilder(JavaPlugin plugin, Command mainCommand, Command... subcommandsPass) {
        this.mainCommand = mainCommand;
        mainCommand.addSubcommand(subcommandsPass);
        Objects.requireNonNull(plugin.getCommand(mainCommand.getName())).setExecutor(functionality);
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
    public CommandBuilder addSubcommand(Command command) {
        mainCommand.addSubcommand(command);
        return this;
    }
    public void register() {

    }
    public ArrayList<Command> getSubcommands(){
        return new ArrayList<>(mainCommand.getSubcommands());
    }
}

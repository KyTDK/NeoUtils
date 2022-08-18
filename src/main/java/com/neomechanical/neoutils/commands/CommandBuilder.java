package com.neomechanical.neoutils.commands;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.commands.utils.CommandUtils;
import com.neomechanical.neoutils.manager.ManagerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Supplier;

public class CommandBuilder {
    String parentCommand;
    public Command mainCommand;
    public JavaPlugin plugin = NeoUtils.getInstance();
    private final ManagerManager manager = NeoUtils.getManagers();
    public Supplier<String> errorNotPlayer = () -> "You must be a player to use this command";
    public Supplier<String> errorNoPermission = () -> "You do not have permission to use this command!";
    public Supplier<String> errorCommandNotFound = () -> "Command not found!";
    public String[] aliases;
    CommandFunctionality functionality = new CommandFunctionality(this);
    @SuppressWarnings("unused")
    public CommandBuilder(Command mainCommand, Command... subcommandsPass) {
        this.mainCommand = mainCommand;
        this.parentCommand = mainCommand.getName();
        mainCommand.addSubcommand(subcommandsPass);
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
    public CommandBuilder setAliases(String... aliases) {
         this.aliases = aliases;
         return this;
    }
    public void register() {
        manager.getCommandManager().addCommand(mainCommand);
        CommandUtils.registerCommand(mainCommand.getPermission(), mainCommand.getName(), aliases);
        Objects.requireNonNull(plugin.getCommand(mainCommand.getName())).setExecutor(functionality);
    }
    public void unregister() {
        manager.getCommandManager().removeCommand(parentCommand);
    }
    public ArrayList<Command> getSubcommands(){
        return new ArrayList<>(mainCommand.getSubcommands());
    }
}
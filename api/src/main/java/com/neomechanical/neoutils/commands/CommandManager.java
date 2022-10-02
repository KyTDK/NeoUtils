package com.neomechanical.neoutils.commands;


import com.neomechanical.neoutils.NeoUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    public CommandManager() {}

    private final Map<String, Command> stringCommandBuilderMap = new HashMap<>();

    public Command getCommand(String command) {
        return stringCommandBuilderMap.get(command);
    }

    public void addCommand(Command command) {
        stringCommandBuilderMap.put(command.getName(), command);
    }

    public void removeCommand(String command) {
        stringCommandBuilderMap.remove(command);
    }

    public List<Command> getSubcommands(String commandName) {
        Command command = stringCommandBuilderMap.get(commandName);
        if (command == null) {
            NeoUtils.getInstance().getFancyLogger().warn("Command " + commandName + " does not exist");
            return Collections.emptyList();
        } else {
            return command.getSubcommands();
        }
    }
}

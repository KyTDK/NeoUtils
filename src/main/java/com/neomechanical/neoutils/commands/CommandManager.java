package com.neomechanical.neoutils.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
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
    public List<Command> getSubcommands(String command) {
        return stringCommandBuilderMap.get(command).getSubcommands();
    }
}

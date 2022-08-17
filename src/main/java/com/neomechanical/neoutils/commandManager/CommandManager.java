package com.neomechanical.neoutils.commandManager;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, Command> stringCommandBuilderMap = new HashMap<>();
    public Command getCommandBuilder(String command) {
        return stringCommandBuilderMap.get(command);
    }
    public void addCommandBuilder(Command command) {
        stringCommandBuilderMap.put(command.getName(), command);
    }
}

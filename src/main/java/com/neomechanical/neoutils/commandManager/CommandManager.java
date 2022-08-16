package com.neomechanical.neoutils.commandManager;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private Map<String, CommandBuilder> stringCommandBuilderMap = new HashMap<>();
    public CommandBuilder getCommandBuilder(String command) {
        return stringCommandBuilderMap.get(command);
    }
    public void addCommandBuilder(CommandBuilder commandBuilder) {
        stringCommandBuilderMap.put(commandBuilder, commandBuilder);
    }
}

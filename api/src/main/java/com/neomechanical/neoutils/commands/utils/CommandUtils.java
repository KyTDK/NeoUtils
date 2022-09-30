package com.neomechanical.neoutils.commands.utils;

import com.neomechanical.neoutils.commands.Command;
import com.neomechanical.neoutils.commands.annotations.SubCommands;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class CommandUtils {
    private CommandUtils() {
    }

    public static List<Command> getSubcommands(Class<? extends Command> command) {
        if (command.isAnnotationPresent(SubCommands.class)) {
            SubCommands subCommands = command.getAnnotation(SubCommands.class);
            List<Command> commandList = new ArrayList<>();
            for (Class<? extends Command> commandClass : subCommands.subcommands()) {
                try {
                    commandList.add(commandClass.getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            return commandList;
        }
        return null;
    }

    public static Command getSubcommand(Command root, String[] args) {
        if (args.length > 1) {
            ArrayDeque<String> argStack = new ArrayDeque<>(Arrays.asList(args));
            Iterator<String> value = argStack.iterator();
            Command previousCommand = root;
            while (value.hasNext()) {
                String currentArg = value.next();
                List<Command> annotatedSubcommands = CommandUtils.getSubcommands(previousCommand.getClass());
                if (annotatedSubcommands != null) {
                    for (Command subcommand : annotatedSubcommands) {
                        if (subcommand.getName().equalsIgnoreCase(currentArg)) {
                            previousCommand = subcommand;
                        }
                    }
                }
            }
            return previousCommand;
        }
        return null;
    }
}
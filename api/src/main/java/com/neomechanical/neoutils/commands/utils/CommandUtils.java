package com.neomechanical.neoutils.commands.utils;

import com.neomechanical.neoutils.commands.Command;
import com.neomechanical.neoutils.commands.annotations.SubCommands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;

public class CommandUtils {
    private CommandUtils() {
    }

    public static Class<? extends Command>[] getSubcommands(Class<? extends Command> command) {
        if (command.isAnnotationPresent(SubCommands.class)) {
            SubCommands subCommands = command.getAnnotation(SubCommands.class);
            return subCommands.subcommands();
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
                Class<? extends Command>[] annotatedSubcommands = CommandUtils.getSubcommands(previousCommand.getClass());
                if (annotatedSubcommands != null) {
                    for (Class<? extends Command> subcommand : annotatedSubcommands) {
                        if (subcommand.getName().equalsIgnoreCase(currentArg)) {
                            try {
                                previousCommand = subcommand.getDeclaredConstructor().newInstance();
                            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                                     NoSuchMethodException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            return previousCommand;
        }
        return null;
    }
}
package com.neomechanical.neoutils.commands.utils;

import com.neomechanical.neoutils.commands.Command;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommandUtils {
    private CommandUtils() {
    }
    public static Command getSubcommand(Command root, String[] args) {
        if (args.length > 1) {
            ArrayDeque<String> argStack = new ArrayDeque<>(Arrays.asList(args));
            Iterator<String> value = argStack.iterator();
            Command previousCommand = root;
            while (value.hasNext()) {
                String currentArg = value.next();
                List<Command> subcommands = previousCommand.getSubcommands();
                if (subcommands != null) {
                    for (Command subcommand : subcommands) {
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
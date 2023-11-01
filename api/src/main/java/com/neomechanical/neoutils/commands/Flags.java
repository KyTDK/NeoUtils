package com.neomechanical.neoutils.commands;

import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.function.BiConsumer;

public class Flags {
    private static final Map<String, BiConsumer<CommandSender, List<String>>> PROCESSORS = new HashMap<>();

    /**
     * @param flag     The flag the triggers the function.
     * @param function The function to be executed when the flag is detected. List<String> is a list of strings after the flag.
     */
    public Flags addFlag(String flag, BiConsumer<CommandSender, List<String>> function) {
        PROCESSORS.put(flag, function);
        return this;
    }

    /**
     * @param sender The command sender
     * @param args   The command args
     * @return The accumulated flag map
     */
    public Map<String, List<String>> parseFlags(CommandSender sender, String[] args) {
        Stack<String> commandStack = new Stack<>();
        commandStack.addAll(Arrays.asList(args));
        List<String> flagArgs = new ArrayList<>();
        Map<String, List<String>> accumulatedFlagArgs = new HashMap<>();
        while (!commandStack.isEmpty()) {
            String element = commandStack.pop();
            if (PROCESSORS.containsKey(element)) {
                accumulatedFlagArgs.put(element, new ArrayList<>(flagArgs));
                PROCESSORS.get(element)
                        //.andThen((acc, list) -> list.clear())
                        .accept(sender, flagArgs);
                flagArgs.clear();
            } else {
                flagArgs.add(element);
            }
        }
        return accumulatedFlagArgs;
    }

    /**
     * @return The keySet of PROCESSORS, in other words, all the flags
     */
    public List<String> tabSuggestions() {
        return new ArrayList<>(PROCESSORS.keySet());
    }
}

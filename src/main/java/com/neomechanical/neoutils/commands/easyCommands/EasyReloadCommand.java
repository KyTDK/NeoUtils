package com.neomechanical.neoutils.commands.easyCommands;

import com.neomechanical.neoutils.commands.Command;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class EasyReloadCommand extends Command {
    private final boolean playerOnly;
    private final String permission;
    private final String description;
    private final String syntax;
    private final BiConsumer<CommandSender, String[]> reloadFunction;

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getSyntax() {
        return syntax;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public boolean playerOnly() {
        return playerOnly;
    }
    public EasyReloadCommand(String syntax, String description, String permission, boolean playerOnly,
                             BiConsumer<CommandSender, String[]> reloadFunction) {
        this.syntax = syntax;
        this.description = description;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.reloadFunction = reloadFunction;
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        reloadFunction.accept(commandSender, strings);
    }

    @Override
    public List<String> tabSuggestions() {
        return null;
    }

    @Override
    public Map<String, List<String>> mapSuggestions() {
        return null;
    }
}

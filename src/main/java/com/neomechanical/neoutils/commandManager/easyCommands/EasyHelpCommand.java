package com.neomechanical.neoutils.commandManager.easyCommands;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.commandManager.Command;
import com.neomechanical.neoutils.commandManager.CommandFunctionality;
import com.neomechanical.neoutils.messages.MessageUtil;
import com.neomechanical.neoutils.pages.Pagination;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

public class EasyHelpCommand extends Command {
    private final String syntax;
    private final String description;
    private final String permission;
    private final boolean playerOnly;
    private final String suffix;
    private final String prefix;
    private final CommandFunctionality commandFunctionality;

    public EasyHelpCommand(CommandFunctionality commandFunctionality, String syntax, String description,
                           String permission, boolean playerOnly , String prefix, String suffix) {
        this.commandFunctionality = commandFunctionality;
        this.syntax = syntax;
        this.description = description;
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override
    public List<Command> getSubcommands() {
        return null;
    }

    @Override
    public String getName() {
        return "help";
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

    @Override
    public void perform(CommandSender player, String[] args) {
        MessageUtil messageUtil = new MessageUtil();
        messageUtil.neoComponentMessage();
        int page = 1;
        if (args.length == 2) {
            if (Integer.getInteger(args[1]) == null) {
                MessageUtil.sendMM(player, NeoUtils.getLanguageManager().getString("commandGeneric.errorInvalidSyntax", null));
                return;
            }
            page = Integer.getInteger(args[1]);
        }
        List<Command> pageList = Pagination.getPage(commandFunctionality.getSubcommands(), page, 10);
        if (pageList == null) {
            MessageUtil.sendMM(player, NeoUtils.getLanguageManager().getString("commandGeneric.errorInvalidSyntax", null));
            return;
        }
        for (Command subCommand : pageList) {
            messageUtil.addComponent("  <gray><bold>" + subCommand.getSyntax() + "</bold> - " + subCommand.getDescription());
        }
        messageUtil.sendNeoComponentMessage(player, prefix, suffix);
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

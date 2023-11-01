package com.neomechanical.neoutils.commands.easyCommands;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.commands.Command;
import com.neomechanical.neoutils.manager.ManagerHandler;
import com.neomechanical.neoutils.messages.MessageUtil;
import com.neomechanical.neoutils.pages.Pagination;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
public class EasyHelpCommand extends Command {
    private final String parentCommandName;
    private final String syntax;
    private final String description;
    private final String permission;
    private final boolean playerOnly;
    private final String suffix;
    private final String prefix;

    public EasyHelpCommand(
            String parentCommandName,
            String syntax,
            String description,
            String permission,
            boolean playerOnly,
            @Nullable String prefix,
            @Nullable String suffix) {
        this.parentCommandName = parentCommandName;
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

    private final ManagerHandler managers = NeoUtils.getNeoUtilities().getManagers();

    @Override
    public void perform(CommandSender player, String[] args) {
        MessageUtil messageUtil = new MessageUtil();
        messageUtil.neoComponentMessage();
        int page = 1;
        if (args.length == 2) {
            if (Integer.getInteger(args[1]) == null) {
                MessageUtil.sendMM(
                        player, managers.getLanguageManager().getString("commandGeneric.errorInvalidSyntax", null));
                return;
            }
            page = Integer.getInteger(args[1]);
        }
        List<Command> pageList =
                Pagination.getPage(managers.getCommandManager().getSubcommands(parentCommandName), page, 10);
        if (pageList == null) {
            MessageUtil.sendMM(
                    player, managers.getLanguageManager().getString("commandGeneric.errorInvalidSyntax", null));
            return;
        }
        for (Command subCommand : pageList) {
            messageUtil.addComponent(
                    "  <gray><bold>" + subCommand.getSyntax() + "</bold> - " + subCommand.getDescription());
        }
        if (prefix != null && suffix != null) {
            messageUtil.sendNeoComponentMessage(player, prefix, suffix);
        } else {
            messageUtil.sendNeoComponentMessage(player);
        }
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

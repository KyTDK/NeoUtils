package com.neomechanical.neoutils.commands.utils.permissions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;

public class PermissionCheck {
    public static String getPermission(Command command) {
        return command.getPermission();
    }
}

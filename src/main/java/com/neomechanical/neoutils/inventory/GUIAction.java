package com.neomechanical.neoutils.inventory;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class GUIAction {
    public abstract void action(InventoryClickEvent event);
}

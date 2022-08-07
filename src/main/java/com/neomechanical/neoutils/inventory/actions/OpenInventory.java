package com.neomechanical.neoutils.inventory.actions;

import com.neomechanical.neoutils.inventory.GUIAction;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OpenInventory extends GUIAction {
    public final InventoryGUI inventoryGUI;

    public OpenInventory(InventoryGUI inventoryGUI) {
        this.inventoryGUI = inventoryGUI;
    }
    @Override
    public void action(InventoryClickEvent event) {
        if (inventoryGUI != null) {
            Player player = (Player) event.getWhoClicked();
            player.openInventory(inventoryGUI.getInventory());
            return;
        }
        throw new IllegalArgumentException("Inventory GUI does not exist.");
    }
}

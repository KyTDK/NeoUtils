package com.neomechanical.neoutils.inventory.actions;

import com.neomechanical.neoutils.inventory.GUIAction;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.entity.Player;

public class OpenInventory extends GUIAction {
    private final String inventoryId;

    public OpenInventory(String inventoryId) {
        this.inventoryId = inventoryId;
    }
    @Override
    public void action(Player player) {
        InventoryGUI inventoryGUI = InventoryUtil.getInventoryManager().getInventoryGUI(inventoryId);
        if (inventoryGUI != null) {
            player.openInventory(inventoryGUI.getInventory());
            return;
        }
        throw new IllegalArgumentException("Inventory GUI with id " + inventoryId + " does not exist.");
    }
}

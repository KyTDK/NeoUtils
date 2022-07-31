package com.neomechanical.neoutils.inventory.actions;

import com.neomechanical.neoutils.inventory.GUIAction;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.entity.Player;

public class OpenInventory extends GUIAction {
    private final InventoryGUI inventoryGUI;

    public OpenInventory(InventoryGUI inventoryGUI) {
        this.inventoryGUI = inventoryGUI;
    }
    @Override
    public void action(Player player) {
        if (inventoryGUI != null) {
            InventoryUtil.openInventory(player, inventoryGUI);
            return;
        }
        throw new IllegalArgumentException("Inventory GUI does not exist.");
    }
}

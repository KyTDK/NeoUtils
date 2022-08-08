package com.neomechanical.neoutils.inventory;

import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.inventory.managers.InventoryManager;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

public class InventoryUtil {
    private static final InventoryManager inventoryManager = new InventoryManager();
    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    public static void registerGUI(InventoryGUI inventoryGUI) {
        inventoryManager.put(inventoryGUI);
    }
    public static void unregisterGUI(InventoryGUI inventoryGUI) {
        inventoryManager.remove(inventoryGUI);
        for (InventoryItem inventoryItem : inventoryGUI.getInventoryItems()) {
            if (inventoryItem.getAction()!=null && inventoryItem.getAction() instanceof OpenInventory) {
                unregisterGUI(((OpenInventory) inventoryItem.getAction()).inventoryGUI);
            }
        }
        if (!inventoryGUI.getPages().isEmpty()) {
            for (InventoryGUI page : inventoryGUI.getPages()) {
                unregisterGUI(page);
            }
        }
    }
    public static InventoryGUI createInventoryGUI(@Nullable InventoryHolder owner, int rows, String title) {
        return new InventoryGUI(Bukkit.createInventory(owner, rows, title), title);
    }
    public static void openInventory(Player player, InventoryGUI inventoryGUI) {
        player.openInventory(inventoryGUI.getInventory());
    }
}

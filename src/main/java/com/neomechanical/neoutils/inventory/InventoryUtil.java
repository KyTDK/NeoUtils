package com.neomechanical.neoutils.inventory;

import com.neomechanical.neoutils.inventory.managers.InventoryManager;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
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
        if (!isRegistered(inventoryGUI)) {
            inventoryManager.put(inventoryGUI);
        }
    }
    private static boolean isRegistered(InventoryGUI inventoryGUI) {
        return inventoryManager.contains(inventoryGUI);
    }
    public static void unregisterGUI(InventoryGUI inventoryGUI) {
        if (isRegistered(inventoryGUI)) {
            inventoryManager.remove(inventoryGUI);
        }
    }
    public static InventoryGUI createInventoryGUI(@Nullable InventoryHolder owner, int rows, String title) {
        return new InventoryGUI(Bukkit.createInventory(owner, rows, title), title);
    }
    public static void openInventory(Player player, InventoryGUI inventoryGUI) {
        registerGUI(inventoryGUI);
        player.openInventory(inventoryGUI.getInventory());
    }
}

package com.neomechanical.neoutils.inventory;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nullable;

/**
 * Basic utils for InventoryGUIs
 */
public class InventoryUtil {
    private InventoryUtil() {}

    public static void registerGUI(InventoryGUI inventoryGUI) {
        if (!isRegistered(inventoryGUI)) {
            NeoUtils.getManagers().getInventoryManager().put(inventoryGUI);
        }
    }

    private static boolean isRegistered(InventoryGUI inventoryGUI) {
        return NeoUtils.getManagers().getInventoryManager().contains(inventoryGUI);
    }

    public static void unregisterGUI(InventoryGUI inventoryGUI) {
        if (isRegistered(inventoryGUI)) {
            NeoUtils.getManagers().getInventoryManager().remove(inventoryGUI);
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

package com.neomechanical.neoutils.inventory.utils;

import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryOperations {
    public static int addItem(Inventory inventory, ItemStack itemStack) {
        int inventoryOccupied = Size.amountOfFilledSlots(inventory);
        inventory.setItem(inventoryOccupied, itemStack);
        return inventoryOccupied;
    }
    public static int addItem(InventoryGUI inventoryGUI, ItemStack itemStack) {
        Inventory inventory = inventoryGUI.getInventory();
        int inventoryOccupied = Size.amountOfFilledSlots(inventory);
        inventory.setItem(inventoryOccupied, itemStack);
        return inventoryOccupied;
    }
}

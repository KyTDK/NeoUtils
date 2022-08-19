package com.neomechanical.neoutils.inventory.utils;

import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryOperations {
    public static int addItem(Inventory inventory, ItemStack itemStack) {
        int inventoryOccupied = Size.amountOfFilledSlots(inventory);
        int index = inventoryOccupied != 0 ? inventoryOccupied : inventory.getSize() - 1;
        inventory.setItem(index, itemStack);
        return index;
    }
    public static int addItem(InventoryGUI inventoryGUI, ItemStack itemStack) {
        Inventory inventory = inventoryGUI.getInventory();
        int inventoryOccupied = Size.amountOfFilledSlots(inventory);
        int index = inventoryOccupied != 0 ? inventoryOccupied : inventory.getSize() - 1;
        inventory.setItem(index, itemStack);
        return index;
    }
}

package com.neomechanical.neoutils.inventory.utils;

import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryOperations {
    public static int addItem(Inventory inventory, ItemStack itemStack) {
        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, itemStack);
                return i;
            }
        }
        return -1;
    }
    public static int addItem(InventoryGUI inventoryGUI, ItemStack itemStack) {
        Inventory inventory = inventoryGUI.getInventory();
        for(int i = 0; i < inventory.getSize(); i++){
            if(inventory.getItem(i) == null){
                inventory.setItem(i, itemStack);
                return i;
            }
        }
        return -1;
    }
}

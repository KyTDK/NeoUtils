package com.neomechanical.neoutils.inventory.managers;

import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private final Map<Inventory, InventoryGUI> inventoryGuiMap = new HashMap<>();

    @Nullable
    public InventoryItem getMenuItem(InventoryGUI inventoryGUI, @NotNull ItemStack item) {
        for (InventoryItem menuItem : inventoryGUI.getInventoryItems()) {
            if (menuItem.getItem().equals(item)) {
                return menuItem;
            }
        }
        return null;
    }
    public boolean isGUI(Inventory inventory) {
        return inventoryGuiMap.containsKey(inventory);
    }
    /**
     * Get the inventory GUI from an id.
     *
     * @param inventory the inventory
     * @return the shop GUI
     */

    @Nullable
    public InventoryGUI getInventoryGUI(@NotNull Inventory inventory) {
        return inventoryGuiMap.get(inventory);
    }

    public void put(@NotNull InventoryGUI gui) {
        inventoryGuiMap.put(gui.getInventory(), gui);
    }
    public void remove(@NotNull InventoryGUI gui) {
        inventoryGuiMap.remove(gui.getInventory(), gui);
    }
    public boolean contains(@NotNull InventoryGUI gui) {
        return inventoryGuiMap.containsKey(gui.getInventory());
    }
}

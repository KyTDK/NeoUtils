package com.neomechanical.neoutils.inventory.managers;

import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private final Map<String, InventoryGUI> stringGuiMap = new HashMap<>();
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

    @Nullable
    public String getKey(@Nullable Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryGUI gui = this.getInventoryGUI(inventory);
        return gui == null ? null : gui.getKey();
    }
    /**
     * Get the inventory GUI from an id.
     *
     * @param inventoryId the inventory id
     * @return the shop GUI
     */
    @Nullable
    public InventoryGUI getInventoryGUI(@NotNull String inventoryId) {
        return stringGuiMap.get(inventoryId);
    }

    @Nullable
    public InventoryGUI getInventoryGUI(@NotNull Inventory inventory) {
        return inventoryGuiMap.get(inventory);
    }

    public void put(@NotNull String menuId, @NotNull InventoryGUI gui) {
        stringGuiMap.put(menuId, gui);
        inventoryGuiMap.put(gui.getInventory(), gui);
    }
}

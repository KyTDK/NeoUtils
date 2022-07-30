package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.NInventory;
import lombok.Data;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

@Data
public class InventoryGUI implements NInventory {
    private @NotNull final Inventory inventory;
    private @NotNull final String key;
    private @NotNull final List<InventoryItem> inventoryItems;

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Nullable
    @Override
    public InventoryItem getItem(int index) {
        return inventoryItems.get(index);
    }

    @Override
    public void setItem(int index, @Nullable InventoryItem item) {
        inventoryItems.add(item);
        inventory.setItem(index, item == null ? null : item.getItem());
    }

    @Override
    public void addItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.add(item);
            inventory.addItem(item.getItem());
        }
    }

    @Override
    public void removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.remove(item);
            inventory.removeItem(item.getItem());
        }
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        return new ItemStack[0];
    }

    @Override
    public void setContents(@NotNull InventoryItem[] items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.add(item);
            inventory.addItem(item.getItem());
        }
    }
}
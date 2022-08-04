package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.items.ItemUtil;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Data
public class InventoryGUI implements NInventory {
    private @NotNull final Inventory inventory;
    private final List<InventoryItem> inventoryItems = new ArrayList<>();
    private @NotNull final String title;

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
            Bukkit.broadcastMessage(getContents().length +" "+getSize());
            if (getContents().length+1 > getSize()) {
                inventory.setItem(getSize()-10, ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
                inventory.setItem(getSize()-1, ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));
                List<InventoryItem> carryOver = new ArrayList<>();
                for (int i = 0; i < getSize()-8; i++) {
                    ItemStack itemCO = inventory.getItem(i);
                    if (itemCO != null) {
                        carryOver.add(InventoryUtil.getInventoryManager().getMenuItem(this, itemCO));
                    }
                }
                InventoryGUI newPage = InventoryUtil.createInventoryGUI(null, getSize(), title);
                for (int i = 0; i < carryOver.size(); i++) {
                    newPage.setItem(i, carryOver.get(i));
                }
            }
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
        return inventory.getContents();
    }

    @Override
    public void setContents(@NotNull InventoryItem[] items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.add(item);
            inventory.addItem(item.getItem());
        }
    }
}
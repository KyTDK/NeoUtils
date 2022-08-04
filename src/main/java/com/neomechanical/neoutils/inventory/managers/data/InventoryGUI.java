package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.items.ItemUtil;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
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
    private @NotNull final List<InventoryGUI> pages = new ArrayList<>();
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
            if (getContents().length+1 > getSize()) {
                List<InventoryItem> carryOver = new ArrayList<>();
                for (int i = getSize()-8; i < getSize(); i++) {
                    ItemStack itemCO = inventory.getItem(i);
                    if (itemCO != null) {
                        carryOver.add(InventoryUtil.getInventoryManager().getMenuItem(this, itemCO));
                        inventory.remove(itemCO);
                    }
                }
                InventoryGUI newPage = InventoryUtil.createInventoryGUI(null, getSize(), title);
                newPage.setItem(getSize()-9, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                        new OpenInventory(this)));
                pages.add(newPage);
                for (InventoryItem itemCO : carryOver) {
                    if (itemCO != null) {
                        newPage.addItem(itemCO);
                    }
                }
                InventoryUtil.registerGUI(newPage);
                if (pages.size() > 1) {
                    InventoryItem navItem = new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                            new OpenInventory(pages.get(pages.size()-2)));
                    setItem(getSize()-9, navItem);
                }
                InventoryItem navItem = new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"),
                        new OpenInventory(newPage));
                setItem(getSize()-1, navItem);
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
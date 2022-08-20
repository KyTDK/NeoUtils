package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import com.neomechanical.neoutils.inventory.utils.InventoryOperations;
import com.neomechanical.neoutils.inventory.utils.Size;
import com.neomechanical.neoutils.items.ItemUtil;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Auto-pagination, feature rich inventory GUI.
 * extends NInventory
 */
@Data
public class InventoryGUI implements NInventory {
    private @NotNull final Inventory inventory;
    private @NotNull final List<InventoryGUI> pages = new ArrayList<>();
    private final Map<Integer, InventoryItem> inventoryItems = new HashMap<>();
    private @NotNull final String title;
    private @Nullable InventoryGUI openOnClose = null;
    private boolean unregisterOnClose = true;
    private boolean pagesInheritParentSettings = true;

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
        if (item !=null) {
            if (inventory.getItem(index) != null) {
                removeItem(item);
            }
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem());
        }
    }


    @Override
    public void addItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            if (!pages.isEmpty()) {
                pages.get(pages.size() - 1).addItem(item);
                continue;
            }
            //If overflow occurs
            if (Size.amountOfFilledSlots(inventory)+1 > getSize()) {
                List<InventoryItem> carryOver = new ArrayList<>();
                carryOver.add(item);
                for (int i = getSize()-9; i < getSize(); i++) {
                    ItemStack itemCO = inventory.getItem(i);
                    if (itemCO != null) {
                        InventoryItem itemCOI = NeoUtils.getManagers().getInventoryManager().getMenuItem(this, i);
                        if (itemCOI!=null&&itemCOI.getType()!=null&&itemCOI.getType().equals(InventoryItemType.NAVIGATION)) {
                            continue;
                        }
                        carryOver.add(itemCOI);
                        inventory.remove(itemCO);
                        inventoryItems.remove(i);
                    }
                }
                InventoryGUI newPage = InventoryUtil.createInventoryGUI(null, getSize(), title);
                if (pagesInheritParentSettings) {
                    newPage.setPagesInheritParentSettings(true);
                    if (openOnClose != null) {
                        newPage.setOpenOnClose(openOnClose);
                    }
                    if (unregisterOnClose) {
                        newPage.setUnregisterOnClose(true);
                    }
                }
                //Add buttons
                newPage.setItem(getSize()-9, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                        (event) -> new OpenInventory(this).action(event), InventoryItemType.NAVIGATION));
                if (pages.contains(this))  {
                    setItem(getSize()-9, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                            (event) -> new OpenInventory(pages.get(pages.size()-2)).action(event), InventoryItemType.NAVIGATION));
                }
                setItem(getSize()-1, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"),
                        (event) -> new OpenInventory(newPage).action(event), InventoryItemType.NAVIGATION));
                //Add overflown items to new page
                for (InventoryItem itemCO : carryOver) {
                    if (itemCO != null) {
                        newPage.addItem(itemCO);
                    }
                }
                pages.add(newPage);
                continue;
            }
            inventoryItems.put(InventoryOperations.addItem(inventory, item.getItem()), item);
        }
    }

    @Override
    public void removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.values().remove(item);
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
            int index = InventoryOperations.addItem(inventory, item.getItem());
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem());
            //get item index
        }
    }

    @Override
    public void setOpenOnClose(@NotNull InventoryGUI inventory) {
        this.openOnClose = inventory;
    }

    @Override
    public void setUnregisterOnClose(boolean unregister) {
        this.unregisterOnClose = unregister;
    }

    @Override
    public void setPagesInheritParentSettings(boolean inherit) {
        this.pagesInheritParentSettings = inherit;
    }
    public List<InventoryGUI> getPages() {
        return pages;
    }
}
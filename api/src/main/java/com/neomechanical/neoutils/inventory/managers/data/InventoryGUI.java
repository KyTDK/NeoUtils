package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import com.neomechanical.neoutils.inventory.utils.InventoryOperations;
import com.neomechanical.neoutils.inventory.utils.Size;
import com.neomechanical.neoutils.items.ItemUtil;
import com.neomechanical.neoutils.versions.VersionManager;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
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
    private @NotNull
    final Inventory inventory;
    private @NotNull
    final List<InventoryGUI> pages = new ArrayList<>();
    private final Map<Integer, InventoryItem> inventoryItems = new HashMap<>();
    private @NotNull
    final String title;
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
    public InventoryGUI setItem(int index, @Nullable InventoryItem item) {
        if (item != null) {
            if (inventory.getItem(index) != null) {
                removeItem(item);
            }
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem());
        }
        return this;
    }

    @Override
    public InventoryGUI addItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
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
                VersionManager versionManager = NeoUtils.getManagers().getVersionManager();
                newPage.setItem(getSize() - 9, new InventoryItem(ItemUtil.createItem(versionManager.matchItems().oakButton(), ChatColor.GREEN + "Left"),
                        (event) -> new OpenInventory(this).action(event), InventoryItemType.NAVIGATION));
                if (pages.contains(this)) {
                    setItem(getSize() - 9, new InventoryItem(ItemUtil.createItem(versionManager.matchItems().oakButton(), ChatColor.GREEN + "Left"),
                            (event) -> new OpenInventory(pages.get(pages.size() - 2)).action(event), InventoryItemType.NAVIGATION));
                }
                setItem(getSize() - 1, new InventoryItem(ItemUtil.createItem(versionManager.matchItems().oakButton(), ChatColor.GREEN + "Right"),
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
        return this;
    }

    @Override
    public InventoryGUI removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.values().remove(item);
            inventory.removeItem(item.getItem());
        }
        return this;
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        return inventory.getContents();
    }

    @Override
    public InventoryGUI setContents(@NotNull InventoryItem[] items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            int index = InventoryOperations.addItem(inventory, item.getItem());
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem());
            //get item index
        }
        return this;
    }

    @Override
    public InventoryGUI setOpenOnClose(@NotNull InventoryGUI inventory) {
        this.openOnClose = inventory;
        return this;
    }

    @Override
    public InventoryGUI setUnregisterOnClose(boolean unregister) {
        this.unregisterOnClose = unregister;
        return this;
    }

    @Override
    public InventoryGUI setPagesInheritParentSettings(boolean inherit) {
        this.pagesInheritParentSettings = inherit;
        return this;
    }
    public void open(Player player) {
        InventoryUtil.openInventory(player, this);
    }
    public List<InventoryGUI> getPages() {
        return pages;
    }
}
package com.neomechanical.neoutils.inventory.managers.data;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import com.neomechanical.neoutils.inventory.utils.InventoryOperations;
import com.neomechanical.neoutils.inventory.utils.Size;
import com.neomechanical.neoutils.items.ItemUtil;
import com.neomechanical.neoutils.version.items.ItemVersionWrapper;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
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
    public InventoryGUI setItem(int index, @Nullable InventoryItem item) {
        if (item != null) {
            if (inventory.getItem(index) != null) {
                removeItem(item);
            }
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem().get());
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
            // If overflow occurs
            if (Size.amountOfFilledSlots(inventory) + 1 > getSize()) {
                List<InventoryItem> carryOver = new ArrayList<>();
                carryOver.add(item);
                for (int i = getSize() - 9; i < getSize(); i++) {
                    ItemStack itemCO = inventory.getItem(i);
                    if (itemCO != null) {
                        InventoryItem itemCOI =
                                NeoUtils.getManagers().getInventoryManager().getMenuItem(this, i);
                        if (itemCOI != null
                                && itemCOI.getType() != null
                                && itemCOI.getType().equals(InventoryItemType.NAVIGATION)) {
                            continue;
                        }
                        carryOver.add(itemCOI);
                        inventory.remove(itemCO);
                        inventoryItems.remove(i);
                    }
                }
                InventoryGUI newPage = InventoryUtil.createInventoryGUI(null, getSize(), title);
                applyParentTraits(newPage);
                // Add buttons
                Material button = ((ItemVersionWrapper) NeoUtils.getInternalVersions().get("items")).oakButton();
                newPage.setItem(
                        getSize() - 9,
                        new InventoryItem.InventoryItemBuilder(
                                () -> ItemUtil.createItem(button, ChatColor.GREEN + "Left"))
                                .setAction((event) -> new OpenInventory(pages.get(pages.size() - 2)).action(event))
                                .setType(InventoryItemType.NAVIGATION)
                                .build());
                if (pages.contains(this)) {
                    setItem(
                            getSize() - 9,
                            new InventoryItem.InventoryItemBuilder(
                                    () -> ItemUtil.createItem(button, ChatColor.GREEN + "Left"))
                                    .setAction((event) -> new OpenInventory(pages.get(pages.size() - 2)).action(event))
                                    .setType(InventoryItemType.NAVIGATION)
                                    .build());
                }
                setItem(
                        getSize() - 1,
                        new InventoryItem.InventoryItemBuilder(
                                () -> ItemUtil.createItem(button, ChatColor.GREEN + "Right"))
                                .setAction((event) -> new OpenInventory(newPage).action(event))
                                .setType(InventoryItemType.NAVIGATION)
                                .build());
                // Add overflown items to new page
                for (InventoryItem itemCO : carryOver) {
                    if (itemCO != null) {
                        newPage.addItem(itemCO);
                    }
                }
                pages.add(newPage);
                continue;
            }
            inventoryItems.put(InventoryOperations.addItem(inventory, item.getItem().get()), item);
        }
        return this;
    }

    @Override
    public InventoryGUI removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.values().remove(item);
            inventory.removeItem(item.getItem().get());
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
            int index = InventoryOperations.addItem(inventory, item.getItem().get());
            inventoryItems.put(index, item);
            inventory.setItem(index, item.getItem().get());
            // get item index
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

    @NotNull
    public InventoryGUI getPage(InventoryGUI inventoryGUI) {
        return pages.get(pages.indexOf(inventoryGUI));
    }
    @NotNull
    public InventoryGUI addPage(InventoryGUI newPage) {
        applyParentTraits(newPage);
        pages.add(newPage);
        return this;
    }
    @NotNull
    public InventoryGUI removePage(InventoryGUI inventoryGUI) {
        pages.remove(inventoryGUI);
        return this;
    }

    public InventoryGUI update() {
        for (InventoryGUI gui : pages) {
            gui.update();
        }
        for (int slot : inventoryItems.keySet()) {
            InventoryItem item = inventoryItems.get(slot);
            inventory.setItem(slot, item.getItem().get());
        }
        return this;
    }
    private void applyParentTraits(InventoryGUI newPage) {
        if (pagesInheritParentSettings) {
            newPage.setPagesInheritParentSettings(true);
            if (openOnClose != null) {
                newPage.setOpenOnClose(openOnClose);
            }
            if (unregisterOnClose) {
                newPage.setUnregisterOnClose(true);
            }
        }
    }
}

package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import com.neomechanical.neoutils.inventory.utils.InventoryOperations;
import com.neomechanical.neoutils.inventory.utils.Size;
import com.neomechanical.neoutils.items.ItemUtil;
import com.neomechanical.neoutils.version.items.ItemVersionWrapper;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
    private Consumer<InventoryCloseEvent> closeEventConsumer;

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
    public InventoryGUI addItem(@NotNull InventoryItem... items) {
        for (InventoryItem item : items) {
            InventoryGUI currentPage = this;
            if (!pages.isEmpty()) {
                currentPage = pages.get(pages.size() - 1);
            }
            // If overflow occurs and its not a page
            if (Size.amountOfFilledSlots(currentPage.inventory) + 1 > currentPage.getSize()) {
                overflowGUI(currentPage, item);
            } else {
                currentPage.inventoryItems.put(InventoryOperations.addItem(currentPage, item.getItem().get()), item);
            }
        }
        return this;
    }

    private void overflowGUI(InventoryGUI currentPage, InventoryItem item) {
        List<InventoryItem> carryOvers = new ArrayList<>();
        for (int i = currentPage.getSize() - 9; i < currentPage.getSize(); i++) {
            ItemStack itemCO = currentPage.inventory.getItem(i);
            if (itemCO != null) {
                InventoryItem itemCOI = NeoUtils.getManagers().getInventoryManager().getMenuItem(currentPage, i);
                if (itemCOI != null && itemCOI.getType() != null && itemCOI.getType().equals(InventoryItemType.NAVIGATION)) {
                    continue;
                }
                carryOvers.add(itemCOI);
                currentPage.inventory.remove(itemCO);
                currentPage.inventoryItems.remove(i);
            }
        }
        carryOvers.add(item);
        InventoryGUI newPage = InventoryUtil.createInventoryGUI(null, currentPage.getSize(), currentPage.title);
        applyParentTraits(newPage);
        setNavigationButtons(currentPage, newPage);
        // Add overflown items to new page
        for (InventoryItem itemCarryOver : carryOvers) {
            if (itemCarryOver != null) {
                newPage.getInventoryItems().put(InventoryOperations.addItem(newPage, itemCarryOver.getItem().get()), itemCarryOver);
            }
        }
        pages.add(newPage);
    }

    private void setNavigationButtons(InventoryGUI currentPage, InventoryGUI newPage) {
        // Add buttons
        Material button = ((ItemVersionWrapper) NeoUtils.getInternalVersions().get("items")).oakButton();
        InventoryItem leftButton = new InventoryItem.InventoryItemBuilder(
                () -> ItemUtil.createItem(button, ChatColor.GREEN + "Left"))
                .setAction(event -> currentPage.open((Player) event.getWhoClicked()))
                .setType(InventoryItemType.NAVIGATION)
                .build();
        InventoryItem rightButton = new InventoryItem.InventoryItemBuilder(
                () -> ItemUtil.createItem(button, ChatColor.GREEN + "Right"))
                .setAction(event -> newPage.open((Player) event.getWhoClicked()))
                .setType(InventoryItemType.NAVIGATION)
                .build();
        newPage.setItem(newPage.getSize() - 9, leftButton);
        currentPage.setItem(currentPage.getSize() - 1, rightButton);
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

    public InventoryGUI setRunOnClose(@NotNull Consumer<InventoryCloseEvent> closeEventConsumer) {
        this.closeEventConsumer = closeEventConsumer;
        return this;
    }

    public Consumer<InventoryCloseEvent> getCloseEventConsumer() {
        return closeEventConsumer;
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

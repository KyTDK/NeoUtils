package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.NInventory;
import com.neomechanical.neoutils.inventory.actions.OpenInventory;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import com.neomechanical.neoutils.inventory.utils.Size;
import com.neomechanical.neoutils.items.ItemUtil;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto-pagination, feature rich inventory GUI.
 * extends NInventory
 */
@Data
public class InventoryGUI implements NInventory {
    private @NotNull final Inventory inventory;
    private @NotNull final List<InventoryGUI> pages = new ArrayList<>();
    private final List<InventoryItem> inventoryItems = new ArrayList<>();
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
        inventoryItems.add(item);
        inventory.setItem(index, item == null ? null : item.getItem());
        return this;
    }

    @Override
    public InventoryGUI addItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            if (!pages.isEmpty()) {
                pages.get(pages.size() - 1).addItem(item);
                continue;
            }
            if (Size.amountOfFiledSlots(inventory)+1 > getSize()) {
                List<InventoryItem> carryOver = new ArrayList<>();
                carryOver.add(item);
                for (int i = getSize()-9; i < getSize(); i++) {
                    ItemStack itemCO = inventory.getItem(i);
                    if (itemCO != null) {
                        InventoryItem itemCOI = InventoryUtil.getInventoryManager().getMenuItem(this, itemCO);
                        if (itemCOI!=null&&itemCOI.getType()!=null&&itemCOI.getType().equals(InventoryItemType.NAVIGATION)) {
                            continue;
                        }
                        carryOver.add(itemCOI);
                        inventory.remove(itemCO);
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
                newPage.setItem(getSize()-9, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                        new OpenInventory(this), InventoryItemType.NAVIGATION));
                pages.add(newPage);
                for (InventoryItem itemCO : carryOver) {
                    if (itemCO != null) {
                        newPage.addItem(itemCO);
                    }
                }
                if (pages.contains(this))  {
                    setItem(getSize()-9, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"),
                            new OpenInventory(pages.get(pages.size()-2)), InventoryItemType.NAVIGATION));
                }
                setItem(getSize()-1, new InventoryItem(ItemUtil.createItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"),
                        new OpenInventory(newPage), InventoryItemType.NAVIGATION));
                continue;
            }
            inventoryItems.add(item);
            inventory.addItem(item.getItem());
        }
        return this;
    }

    @Override
    public InventoryGUI removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException {
        for (InventoryItem item : items) {
            inventoryItems.remove(item);
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
            inventoryItems.add(item);
            inventory.addItem(item.getItem());
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
}
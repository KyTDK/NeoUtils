package com.neomechanical.neoutils.inventory.managers;

import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
public class InventoryFunctionality implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        InventoryManager inventoryManager = InventoryUtil.getInventoryManager();
        if (!inventoryManager.isGUI(inventory)) {
            return;
        }
        event.setCancelled(true);
        InventoryGUI gui = inventoryManager.getInventoryGUI(inventory);
        if (gui == null) {
            return;
        }
        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        InventoryItem menuItem = inventoryManager.getMenuItem(gui, item);
        if (menuItem == null) {
            return;
        }
        if (menuItem.getAction() != null) {
            menuItem.getAction().action((Player) event.getWhoClicked());
        }
        if (menuItem.getOpens() != null) {
            InventoryGUI inventoryToOpen = inventoryManager.getInventoryGUI(menuItem.getOpens());
            if (inventoryToOpen != null) {
                player.openInventory(inventoryToOpen.getInventory());
            }
        }
    }
}

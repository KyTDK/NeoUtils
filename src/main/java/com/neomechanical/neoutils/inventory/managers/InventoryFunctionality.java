package com.neomechanical.neoutils.inventory.managers;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.GUIAction;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryFunctionality implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryManager inventoryManager = InventoryUtil.getInventoryManager();
        if (!inventoryManager.isGUI(inventory)) {
            return;
        }
        InventoryGUI gui = inventoryManager.getInventoryGUI(inventory);
        if (gui == null) {
            return;
        }
        Player player = (Player) event.getPlayer();
        if (gui.getOpenOnClose()!=null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.openInventory(gui.getOpenOnClose().getInventory());
                }
            }.runTaskLater(NeoUtils.getInstance(), 1L);
        }
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
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
        menuItem.getAction().action(event);
    }
}

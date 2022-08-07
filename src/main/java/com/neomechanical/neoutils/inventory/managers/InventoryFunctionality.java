package com.neomechanical.neoutils.inventory.managers;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.inventory.InventoryUtil;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
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
            new BukkitRunnable() {
                @Override
                public void run() {
                    //Make sure it wasn't just another inventory being opened.
                    if (!InventoryUtil.getInventoryManager().isGUI(player.getOpenInventory().getTopInventory())
                    && player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
                        if (gui.getOpenOnClose() != null) {
                            player.openInventory(gui.getOpenOnClose().getInventory());
                            return;
                        }
                        if (gui.isUnregisterOnClose()) {
                            InventoryUtil.unregisterGUI(gui);
                        }
                    }
                }
            }.runTaskLater(NeoUtils.getInstance(), 1L);
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
        if (menuItem.getAction() != null) {
            menuItem.getAction().action(event);
        }
    }
}

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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.function.Consumer;

public class InventoryFunctionality implements Listener {
    JavaPlugin plugin;

    public InventoryFunctionality(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final InventoryManager inventoryManager = NeoUtils.getManagers().getInventoryManager();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
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
                // Make sure it wasn't just another inventory being opened.
                if (!inventoryManager.isGUI(player.getOpenInventory().getTopInventory())
                        && player.getOpenInventory().getTopInventory().getType() == InventoryType.CRAFTING) {
                    if (gui.getOpenOnClose() != null) {
                        gui.update();
                        InventoryUtil.openInventory(player, gui.getOpenOnClose());
                    }
                    if (gui.getCloseEventConsumer() != null) {
                        gui.getCloseEventConsumer().accept(event);
                    }
                }
                if (gui.isUnregisterOnClose()) {
                    InventoryUtil.unregisterGUI(gui);
                }
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
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
        InventoryItem menuItem = inventoryManager.getMenuItem(gui, event.getSlot());
        if (menuItem == null) {
            return;
        }
        for (Consumer<InventoryClickEvent> action : Objects.requireNonNull(menuItem.getAction(event.getClick()))) {
            action.accept(event);
        }
    }
}
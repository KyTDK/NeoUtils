package com.neomechanical.neoutils.inventory;

import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.inventory.managers.InventoryManager;
import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class InventoryUtil {
    private static InventoryManager inventoryManager;
    public static InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    public static void setInventoryManager(InventoryManager inventoryManager) {
        InventoryUtil.inventoryManager = inventoryManager;
    }
    public static void registerGUI(String inventoryId, InventoryGUI inventoryGUI) {
        inventoryManager.put(inventoryId, inventoryGUI);
    }
    public static void init(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new InventoryFunctionality(), plugin);
        setInventoryManager(new InventoryManager());
    }
    public static InventoryGUI createInventoryGUI(@Nullable InventoryHolder owner, String inventoryId, int rows) {
        return new InventoryGUI(Bukkit.createInventory(owner, rows), inventoryId);
    }
    public static void openInventory(Player player, InventoryGUI inventoryGUI) {
        player.openInventory(inventoryGUI.getInventory());
    }
}

package com.neomechanical.neoutils.manager;

import com.neomechanical.neoutils.items.ItemData;
import org.bukkit.plugin.java.JavaPlugin;

public class DataHandler {
    private final JavaPlugin plugin;
    private final ItemData itemData = new ItemData();

    public DataHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public ItemData getItemData() {
        return itemData;
    }
}

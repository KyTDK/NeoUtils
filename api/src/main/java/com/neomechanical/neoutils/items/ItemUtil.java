package com.neomechanical.neoutils.items;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemUtil {
    /**
     * Set ItemStack's durability
     *
     * @param material    the item
     * @param displayname the name
     */
    public static ItemStack createItem(Material material, String displayname) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Set ItemStack's durability
     *
     * @param material the item
     * @param amount the amount
     * @param displayname the name
     * @param lore the lore
     *
     */
    public static ItemStack createItem(Material material, int amount, String displayname, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayname));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static short getMaxDurability(ItemStack itemStack) {
        return itemStack.getType().getMaxDurability();
    }
}

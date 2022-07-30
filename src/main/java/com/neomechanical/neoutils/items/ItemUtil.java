package com.neomechanical.neoutils.items;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Random;


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
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayname));
    meta.setLore(lore);
    item.setItemMeta(meta);
    return item;
}
    /**
     * Set ItemStack's durability
     *
     * @param itemStack the item in which durability will be altered
     * @param damage by how much will the durability change
     *
     */
public static void setDurability(ItemStack itemStack, int damage) {
    org.bukkit.inventory.meta.Damageable damageableMeta = (org.bukkit.inventory.meta.Damageable) itemStack.getItemMeta();
    assert damageableMeta != null;
    int durability = damageableMeta.getDamage(); // returns the damage the item has received **this is a positive integer**
    damageableMeta.setDamage(durability + damage);
    itemStack.setItemMeta(damageableMeta);
}
    /**
     * Set ItemStack's durability, taking unbreaking into account
     *
     * @param itemStack the item in which durability will be altered
     * @param damage by how much will the durability change
     *
     */
    public static void setRealisticDurability(ItemStack itemStack, int damage) {
        org.bukkit.inventory.meta.Damageable damageableMeta = (org.bukkit.inventory.meta.Damageable) itemStack.getItemMeta();
        assert damageableMeta != null;
        int durability = damageableMeta.getDamage(); // returns the damage the item has received **this is a positive integer**
        if(itemStack.getEnchantments().containsKey(Enchantment.DURABILITY)) {
            int percentage = 100/itemStack.getEnchantmentLevel(Enchantment.DURABILITY)+1;
            Random rand = new Random();
            int  n = rand.nextInt(100) + 1;
            if(n<=percentage) {
                damageableMeta.setDamage(durability + damage);
                itemStack.setItemMeta(damageableMeta);
            }
        }
    }
    public static short getDurability(ItemStack itemStack) {
        org.bukkit.inventory.meta.Damageable  damageableMeta = (org.bukkit.inventory.meta.Damageable) itemStack.getItemMeta();
        assert damageableMeta != null;
        return (short) damageableMeta.getDamage(); // returns the damage the item has received **this is a positive integer**
    }
public static short getMaxDurability (ItemStack itemStack) {
    return itemStack.getType().getMaxDurability();
}
}
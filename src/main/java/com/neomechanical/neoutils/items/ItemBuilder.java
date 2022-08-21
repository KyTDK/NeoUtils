package com.neomechanical.neoutils.items;

import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple ItemBuilder class.
 */
public class ItemBuilder {
    private final ItemStack item;
    private final ItemMeta meta;

    private final List<String> lore = new ArrayList<>();

    public ItemBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    /**
     * Set the item's name.
     *
     * @param string the name
     * @return the builder instance
     */
    public ItemBuilder name(String string) {
        meta.setDisplayName(MessageUtil.color(string));
        return this;
    }

    /**
     * Add a string to the lore.
     *
     * @param string the string
     * @return the builder instance
     */
    public ItemBuilder lore(String string) {
        lore.add(MessageUtil.color(string));
        return this;
    }

    /**
     * Set lore.
     *
     * @param lore the lore
     * @return the builder instance
     */
    public ItemBuilder lore(List<String> lore) {
        this.lore.addAll(lore.stream()
                .map(MessageUtil::color).collect(Collectors.toList()));
        return this;
    }

    /**
     * Apply an enchantment to the item.
     *
     * @param enchantment the enchantment
     * @param level       the level
     */
    public void enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
    }

    /**
     * Equivalent to ItemBuilder#enchant(Enchantment.DURABILITY, 1);
     *
     * @param glow to glow
     * @return the builder instance
     */
    public ItemBuilder glow(boolean glow) {
        if (glow) {
            this.enchant(Enchantment.DURABILITY, 1);
        }
        return this;
    }

    /**
     * Set the item meta.
     *
     * @return the builder instance
     */
    public ItemBuilder set() {
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Set the item meta and get the item.
     * Equivalent to ItemBuilder#set(), ItemBuilder#get().
     *
     * @return the item
     */
    @NotNull
    public ItemStack getAndSet() {
        set();
        return item;
    }

    /**
     * Get the item.
     *
     * @return the item
     */
    @NotNull
    public ItemStack get() {
        return item;
    }
}
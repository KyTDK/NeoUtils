package com.neomechanical.neoutils.items;


import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.messages.MessageUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A simple ItemBuilder class.
 */
public class SpecialItem {
    private final ItemStack item;
    private final ItemMeta meta;
    private List<String> lore = new ArrayList<>();
    private final List<Supplier<String>> animatedLore = new ArrayList<>();
    private final Map<Consumer<PlayerInteractEvent>, Action[]> actionMap = new HashMap<>();
    private int animatedLoreUpdateInterval = 5;

    public SpecialItem(@NotNull Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    @Nullable
    public List<Consumer<PlayerInteractEvent>> getAction(Action action) {
        List<Consumer<PlayerInteractEvent>> actionConsumers = new ArrayList<>();
        for (Consumer<PlayerInteractEvent> actionConsumer : actionMap.keySet()) {
            Action[] actions = actionMap.get(actionConsumer);
            if (actions == null || actions.length < 1 || Arrays.stream(actions).anyMatch((actionTrigger -> actionTrigger == action))) {
                actionConsumers.add(actionConsumer);
            }
        }
        return actionConsumers;
    }

    /**
     * Set the item's name.
     *
     * @param string the name
     * @return the builder instance
     */
    public SpecialItem setName(String string) {
        if (meta != null) {
            meta.setDisplayName(MessageUtil.color(string));
        }
        return this;
    }

    /**
     * Add a string to the lore.
     *
     * @param string the string
     * @return the builder instance
     */
    public SpecialItem setLore(String string) {
        lore.clear();
        lore.add(MessageUtil.color(string));
        return this;
    }

    public SpecialItem addLore(String string) {
        lore.add(MessageUtil.color(string));
        return this;
    }

    /**
     * Set lore.
     *
     * @param lore the lore
     * @return the builder instance
     */
    public SpecialItem addLore(List<String> lore) {
        this.lore.addAll(lore.stream().map(MessageUtil::color).collect(Collectors.toList()));
        return this;
    }

    public SpecialItem setLore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public List<Supplier<String>> getAnimatedLore() {
        return animatedLore;
    }

    /**
     * Add a string to the animated law (updates every said amount of ticks)
     *
     * @param string the string
     * @return the builder instance
     */
    public SpecialItem setAnimatedLore(Supplier<String> string) {
        this.animatedLore.clear();
        animatedLore.add(string);
        return this;
    }

    /**
     * Set lore. (updates every said amount of ticks)
     *
     * @param lore the lore
     * @return the builder instance
     */
    public SpecialItem setAnimatedLore(List<Supplier<String>> lore) {
        this.animatedLore.clear();
        this.animatedLore.addAll(lore);
        return this;
    }

    public SpecialItem addAnimatedLore(Supplier<String> string) {
        animatedLore.add(string);
        return this;
    }

    /**
     * Set lore. (updates every said amount of ticks)
     *
     * @param lore the lore
     * @return the builder instance
     */
    public SpecialItem addAnimatedLore(List<Supplier<String>> lore) {
        this.animatedLore.addAll(lore);
        return this;
    }

    public int getAnimatedLoreUpdateInterval() {
        return animatedLoreUpdateInterval;
    }

    /**
     * Set the animated lore update interval, default is 5 ticks
     *
     * @param animatedLoreUpdateInterval the lore update interval in ticks
     */
    public void setAnimatedLoreUpdate(int animatedLoreUpdateInterval) {
        this.animatedLoreUpdateInterval = animatedLoreUpdateInterval;
    }

    /**
     * Apply an enchantment to the item.
     *
     * @param enchantment the enchantment
     * @param level       the level
     */
    public void setEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
    }

    /**
     * Equivalent to ItemBuilder#enchant(Enchantment.DURABILITY, 1);
     *
     * @param glow to glow
     * @return the builder instance
     */
    public SpecialItem glow(boolean glow) {
        if (glow) {
            this.setEnchantment(Enchantment.DURABILITY, 1);
        }
        return this;
    }

    /**
     * Set the item meta.
     *
     * @return the builder instance
     */
    public SpecialItem set() {
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
        if (!animatedLore.isEmpty()) {
            new UpdateAnimatedLore(NeoUtils.getInstance(), this).run();
        }
        if (!NeoUtils.getNeoUtilities().getDataHandler().getItemData().getSpecialItems().containsKey(item)) {
            NeoUtils.getNeoUtilities().getDataHandler().getItemData().getSpecialItems().put(item, this);
        }
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

    public SpecialItem setAction(Consumer<PlayerInteractEvent> playerInteractEventConsumer, Action... action) {
        actionMap.put(playerInteractEventConsumer, action);
        return this;
    }
}

package com.neomechanical.neoutils.items;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UpdateAnimatedLore {
    private final int interval;
    private final JavaPlugin plugin;
    private final SpecialItem specialItem;

    public UpdateAnimatedLore(JavaPlugin plugin, SpecialItem specialItem) {
        this.interval = specialItem.getAnimatedLoreUpdateInterval();
        this.specialItem = specialItem;
        this.plugin = plugin;
    }

    public void run() {
        List<Supplier<String>> animatedLore = specialItem.getAnimatedLore();
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> toSetNewMeta = new ArrayList<>();
                for (Supplier<String> stringSupplier : animatedLore) {
                    toSetNewMeta.add(stringSupplier.get());
                }
                ItemMeta itemMeta = specialItem.get().getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setLore(toSetNewMeta);
                }
            }
        }.runTaskTimer(plugin, 0, interval);
    }
}

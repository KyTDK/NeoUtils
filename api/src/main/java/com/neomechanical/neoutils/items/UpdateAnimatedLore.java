package com.neomechanical.neoutils.items;

import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.function.Supplier;

public class UpdateAnimatedLore {
    private final int interval;
    private final JavaPlugin plugin;
    private final SpecialItem specialItem;

    public UpdateAnimatedLore(NeoUtils plugin, SpecialItem specialItem) {
        this.interval = specialItem.getAnimatedLoreUpdateInterval();
        this.specialItem = specialItem;
        this.plugin = plugin;
    }

    public void run() {
        List<Supplier<String>> animatedLore = specialItem.getAnimatedLore();
        new BukkitRunnable() {
            @Override
            public void run() {
                specialItem.clearItemLore();
                for (Supplier<String> stringSupplier : animatedLore) {
                    specialItem.setLore(stringSupplier.get());
                }
            }
        }.runTaskTimer(plugin, 0, interval);
    }
}

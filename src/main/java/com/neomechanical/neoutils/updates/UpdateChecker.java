package com.neomechanical.neoutils.updates;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import static com.neomechanical.neoutils.updates.IsUpToDate.isUpToDate;

// From: https://www.spigotmc.org/wiki/creating-an-update-checker-that-checks-for-updates
public class UpdateChecker {
    private static Boolean UpToDate;
    private final JavaPlugin plugin;
    private final int resourceId;

    public static Boolean isPluginUpToDate() {
        return UpToDate;
    }

    public void start(int interval) {
        new BukkitRunnable() {
            @Override
            public void run() {
                new UpdateChecker(plugin, 103183).getVersion(version -> UpToDate = isUpToDate(plugin.getDescription().getVersion(), version));
            }
        }.runTaskTimer(plugin, 0, interval);
    }

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}


package com.neomechanical.neoutils;

import com.neomechanical.neoutils.inventory.managers.InventoryFunctionality;
import com.neomechanical.neoutils.items.ItemInteractionListener;
import com.neomechanical.neoutils.version.items.IItemEventHandlerWrapper;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NeoUtils extends JavaPlugin {
    private static Plugin plugin;
    private static NeoUtilities neoUtilities;
    private static boolean checkedRelocation;

    static {
        checkRelocation();
    }

    public static Plugin getInstance() {
        if (plugin == null) {
            checkRelocation();
            try {
                plugin = JavaPlugin.getProvidingPlugin(Class.forName(Thread.currentThread().getStackTrace()[3].getClassName()));
                init(plugin);
            } catch (final IllegalArgumentException | IllegalStateException | ClassNotFoundException exception) {
                List<String> errorLocation = new ArrayList<>();
                String errorLocation2 = "";
                try {
                    final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
                    final PluginDescriptionFile description = getPluginDescriptionFile();
                    final String main = description.getMain();
                    StackTraceElement firstFoundElement = null;
                    for (final StackTraceElement element : elements) {
                        if (element.getClassName().equals(main)) {
                            if (firstFoundElement == null) firstFoundElement = element;
                            final String lineNumber = element.getLineNumber() <= 0 ? "?" : String.valueOf(element.getLineNumber());
                            errorLocation.add(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + lineNumber + ")");
                        }
                    }
                    if (firstFoundElement != null) {
                        final String lineNumber = firstFoundElement.getLineNumber() <= 0 ? "?" : String.valueOf(firstFoundElement.getLineNumber());
                        errorLocation2 = firstFoundElement.getFileName() + " at line " + lineNumber;
                    }
                } catch (final Throwable ignored) {

                }
                Logger logger = Bukkit.getLogger();
                if (errorLocation.isEmpty()) {
                    logger.severe("Oh no! I couldn't find the instance of your plugin!");
                    logger.severe("It seems like you're trying to use NeoUtils before your plugin was enabled by the PluginManager.");
                    logger.severe("");
                    logger.severe("Please either wait until your plugin's onLoad() or onEnable() method was called, or call");
                    logger.severe("\"NeoUtils.init(this)\" in your plugin's constructor or init block.");
                } else {
                    logger.severe("Oh no! You're trying to access one of JeffLib's methods before your plugin was enabled at the following location:");
                    logger.severe("");
                    for (String element : errorLocation) {
                        logger.severe("   " + element);
                    }
                    logger.severe("");
                    logger.severe("Please call \"NeoUtils.init(this)\" before doing whatever you do in " + errorLocation2 + ", or wait until your plugin's onLoad() or onEnable() method was called.");
                }

                throw new IllegalStateException();
            }
        }
        return plugin;
    }

    private static void checkRelocation() {
        if (checkedRelocation) return;
        try {
            final String defaultPackageCom = new String(new byte[]{'c', 'o', 'm', '.'
                    , 'n', 'e', 'o', 'n', 'e', 'c', 'h', 'a', 'n', 'i', 'c', 'a', 'l', '.'
                    , 'n', 'e', 'o', 'u', 't', 'i', 'l', 's'});
            final String examplePackage = new String(new byte[]{'y', 'o', 'u', 'r', '.', 'p', 'a', 'c', 'k', 'a', 'g', 'e'});
            final String packageName = NeoUtils.class.getPackage().getName();
            if (packageName.startsWith(defaultPackageCom) || packageName.startsWith(examplePackage)) {
                final String authors = String.join(", ", getInstance().getDescription().getAuthors());
                final String plugin = getInstance().getName() + " " + getInstance().getDescription().getVersion();
                Bukkit.getLogger().severe("Nag author(s) " + authors + (authors.length() == 0 ? "" : " ") + "of plugin " + plugin + " for failing to properly relocate JeffLib!");
            }
        } catch (final Throwable ignored) {
            return;
        }
        checkedRelocation = true;
    }

    private static PluginDescriptionFile getPluginDescriptionFile() throws NoSuchFieldException, IllegalAccessException {
        URLClassLoader loader = (URLClassLoader) NeoUtils.class.getClassLoader();
        Field descriptionField = loader.getClass().getDeclaredField("description");
        descriptionField.setAccessible(true);
        return (PluginDescriptionFile) descriptionField.get(loader);
    }

    public static NeoUtilities getNeoUtilities() {
        if (plugin == null) {
            getInstance();
            if (neoUtilities == null) {
                throw new RuntimeException("Failed to initialize NeoUtilities");
            }
        }
        return neoUtilities;
    }

    public static void init(Plugin plugin) {
        NeoUtils.plugin = plugin;
        neoUtilities = new NeoUtilities(plugin);
        neoUtilities.init();
        plugin.getServer().getPluginManager().registerEvents(new InventoryFunctionality(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new ItemInteractionListener((IItemEventHandlerWrapper) neoUtilities.getInternalVersions().get("specialItemInteractions")), plugin);
    }

    @Override
    public void onEnable() {
        plugin = this;
        init(plugin);
    }

    @Override
    public void onDisable() {
        BukkitAudiences adventure = getNeoUtilities().getAdventure();
        adventure.close();
        //make sure to unregister the registered channels in case of a reload
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}

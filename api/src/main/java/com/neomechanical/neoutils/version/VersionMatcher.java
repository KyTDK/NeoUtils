package com.neomechanical.neoutils.version;

import com.neomechanical.neoutils.version.items.ItemVersionWrapper;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;

import static com.neomechanical.neoutils.updates.IsUpToDate.isUpToDate;

public class VersionMatcher {
    String serverVersion;

    /***
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    public VersionMatcher() {
        serverVersion = Bukkit.getServer()
                .getClass()
                .getPackage()
                .getName()
                .split("\\.")[3]
                .substring(1);
        if (isLegacy()) {
            serverVersion = "LEGACY";
        }
    }

    public boolean isLegacy() {
        return !isUpToDate(serverVersion, "1_14_R1");
    }

    public ItemVersionWrapper matchItems() {
        try {
            try {
                return (ItemVersionWrapper)
                        Class.forName(getClass().getPackage().getName() + ".items.Wrapper" + serverVersion)
                                .getDeclaredConstructor()
                                .newInstance();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "NeoUtils does not support server version \"" + serverVersion + "\"", exception);
        }
    }
}

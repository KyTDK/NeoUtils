package com.neomechanical.neoutils.versions;

import com.neomechanical.neoutils.version.items.ItemVersionWrapper;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class VersionMatcher {
    String serverVersion;

    /***
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    public VersionMatcher(@Nullable String serverVersion) {
        if (serverVersion == null) {
            this.serverVersion = Bukkit.getServer()
                    .getClass()
                    .getPackage()
                    .getName()
                    .split("\\.")[3]
                    .substring(1);
        } else {
            this.serverVersion = serverVersion;
        }
    }

    public ItemVersionWrapper matchItems() {
        try {
            try {
                return (ItemVersionWrapper) Class.forName(getClass().getPackage().getName() + ".Wrapper" + serverVersion).getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException(
                    "AnvilGUI does not support server version \"" + serverVersion + "\"", exception);
        }
    }
}
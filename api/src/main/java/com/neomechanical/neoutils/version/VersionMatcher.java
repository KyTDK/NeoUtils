package com.neomechanical.neoutils.version;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static com.neomechanical.neoutils.updates.IsUpToDate.isUpToDate;

public class VersionMatcher {
    private final VersionManager versionManager;
    private Predicate<String> legacyFunction = (ver) -> !isUpToDate(ver, "1_14_R1");
    String serverVersion;

    /***
     * @throws IllegalStateException If the version wrapper failed to be instantiated or is unable to be found
     */
    public VersionMatcher(VersionManager versionManager) {
        this.versionManager = versionManager;
        serverVersion = Bukkit.getServer()
                .getClass()
                .getPackage()
                .getName()
                .split("\\.")[3]
                .substring(1);
        if (isLegacy()) {
            serverVersion = "LEGACY";
        } else {
            serverVersion = "NONLEGACY";
        }
    }

    public boolean isLegacy() {
        return legacyFunction.test(serverVersion);
    }

    public VersionMatcher setLegacyFunction(Predicate<String> predicate) {
        this.legacyFunction = predicate;
        return this;
    }

    public Map<String, VersionWrapper> matchAll() {
        Map<String, Versioning> versioningMap = versionManager.getVersioningMap();
        Map<String, VersionWrapper> matched = new HashMap<>();
        for (Versioning versioning : versioningMap.values()) {
            Map<String, VersionWrapper> classes = versioning.getClassMap();
            for (String versioningName : classes.keySet()) {
                VersionWrapper versionWrapper = classes.get(versioningName);
                try {
                    try {
                        matched.put(versioningName, versionWrapper.getClass().getDeclaredConstructor().newInstance());
                    } catch (InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                } catch (IllegalAccessException | InstantiationException exception) {
                    throw new IllegalStateException(
                            "Failed to instantiate version wrapper for version " + serverVersion + " of " + versioning.getVersioningName() + "versioning instance", exception);
                }
            }
        }
        return matched;
    }

    public VersionWrapper match(String versioningName) {
        Map<String, Versioning> versioningMap = versionManager.getVersioningMap();
        try {
            try {
                return versioningMap.get(versioningName).getClassMap().get(serverVersion).getClass().getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion + " of " + versioningName + "versioning instance", exception);
        }
    }
}

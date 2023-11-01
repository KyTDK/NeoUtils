package com.neomechanical.neoutils.version;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.version.versions.Versions;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class VersionMatcher {
    private final VersionManager versionManager;
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
    }

    public Map<String, VersionWrapper> matchAll() {
        Map<String, Versioning> versioningMap = versionManager.getVersioningMap();
        Map<String, VersionWrapper> matched = new HashMap<>();
        for (Versioning versioning : versioningMap.values()) {
            String versioningName = versioning.getVersioningName();
            matched.put(versioningName, match(versioningName));
        }
        return matched;
    }

    public VersionWrapper match(String versioningName) {
        Map<String, Versioning> versioningMap = versionManager.getVersioningMap();
        try {
            try {
                Versioning versioning = versioningMap.get(versioningName);
                String finalVersion = serverVersion;
                if (versioning.getLegacyFunction() != null) {
                    if (versioning.getLegacyFunction().test(serverVersion)) {
                        finalVersion = Versions.vLEGACY.toString();
                    } else {
                        finalVersion = Versions.vNONLEGACY.toString();
                    }
                }
                if (versioning.getClassMap().containsKey(finalVersion)) {
                    return versioning.getClassMap().get(finalVersion).getDeclaredConstructor().newInstance();
                } else {
                    NeoUtils.getNeoUtilities().getFancyLogger().severe("Cannot match \"" + versioningName + "\" for finalVersion: " + finalVersion + " | Versions available: " + versioning.getClassMap().keySet());
                    return null;
                }
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException | InstantiationException exception) {
            throw new IllegalStateException(
                    "Failed to instantiate version wrapper for version " + serverVersion + " of " + versioningName + " versioning instance", exception);
        }
    }
}

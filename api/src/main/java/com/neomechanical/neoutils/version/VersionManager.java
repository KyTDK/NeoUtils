package com.neomechanical.neoutils.version;

import java.util.HashMap;
import java.util.Map;

public class VersionManager {
    private final Map<String, Versioning> versioningMap = new HashMap<>();

    public void addVersioningClass(String versioningName, Versioning versioningClass) {
        versioningMap.put(versioningName, versioningClass);
    }

    public Map<String, Versioning> getVersioningMap() {
        return versioningMap;
    }

    public Versioning getVersioning(String versioningName) {
        return getVersioningMap().get(versioningName);
    }
}

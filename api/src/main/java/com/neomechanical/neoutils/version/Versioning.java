package com.neomechanical.neoutils.version;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.version.versions.Versions;

import java.util.HashMap;
import java.util.Map;

public class Versioning {
    //Required
    private final String versioningName;
    //Optional
    private final Map<String, VersionWrapper> classMap;

    public Versioning(VersioningBuilder versioningBuilder) {
        this.classMap = versioningBuilder.classMap;
        this.versioningName = versioningBuilder.versioningName;
    }

    public static VersionManager getManager() {
        return NeoUtils.getManagers().getVersionManager();
    }

    public String getVersioningName() {
        return versioningName;
    }

    public Map<String, VersionWrapper> getClassMap() {
        return classMap;
    }

    public void register() {
        NeoUtils.getManagers().getVersionManager().addVersioningClass(versioningName, this);
    }

    public static class VersioningBuilder {
        //Required
        private final String versioningName;
        //Optional
        private final Map<String, VersionWrapper> classMap = new HashMap<>();

        public VersioningBuilder(String versioningName) {
            this.versioningName = versioningName;
        }

        public VersioningBuilder addClass(String version, VersionWrapper versionWrapper) {
            if (Versions.fromString(version) == null) {
                throw new IllegalStateException(versioningName + " is not a valid or supported version");
            }
            classMap.put(version, versionWrapper);
            return this;
        }

        public Versioning build() {
            return new Versioning(this);
        }
    }
}

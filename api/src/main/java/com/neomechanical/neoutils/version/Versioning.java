package com.neomechanical.neoutils.version;

import com.neomechanical.neoutils.NeoUtils;
import com.neomechanical.neoutils.version.versions.Versions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class Versioning {
    //Required
    private final String versioningName;
    //Optional
    private final Map<String, VersionWrapper> classMap;
    private final Predicate<String> legacyFunction;

    public Versioning(VersioningBuilder versioningBuilder) {
        this.classMap = versioningBuilder.classMap;
        this.versioningName = versioningBuilder.versioningName;
        this.legacyFunction = versioningBuilder.legacyFunction;
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

    public Predicate<String> getLegacyFunction() {
        return legacyFunction;
    }

    public void register() {
        NeoUtils.getManagers().getVersionManager().addVersioningClass(versioningName, this);
    }

    public static class VersioningBuilder {
        //Required
        private final String versioningName;
        //Optional
        private final Map<String, VersionWrapper> classMap = new HashMap<>();
        private Predicate<String> legacyFunction;

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

        /**
         * Get the inventory GUI from an id.
         *
         * @param legacyFunction the predicate
         * @return the {@link #Versioning} instance
         */
        public VersioningBuilder setLegacyFunction(Predicate<String> legacyFunction) {
            this.legacyFunction = legacyFunction;
            return this;
        }

        public Versioning build() {
            return new Versioning(this);
        }
    }
}

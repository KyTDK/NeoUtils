package com.neomechanical.neoutils.updates;

import org.apache.maven.artifact.versioning.ComparableVersion;

public class IsUpToDate {
    public static boolean isUpToDate(String currentVersion, String latestVersion) {
        ComparableVersion current = new ComparableVersion(currentVersion);
        ComparableVersion latest = new ComparableVersion(latestVersion);
        return current.compareTo(latest) >= 0;
    }
}

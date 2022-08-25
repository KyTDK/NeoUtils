package com.neomechanical.neoutils.versions;

import com.neomechanical.neoutils.version.items.ItemVersionWrapper;

public class VersionManager {
    ItemVersionWrapper itemVersionWrapper;

    public VersionManager() {
        VersionMatcher versionMatcher = new VersionMatcher();
        itemVersionWrapper = versionMatcher.matchItems();
    }

    public ItemVersionWrapper matchItems() {
        return itemVersionWrapper;
    }
}

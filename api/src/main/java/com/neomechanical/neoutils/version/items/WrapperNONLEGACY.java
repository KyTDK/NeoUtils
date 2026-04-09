package com.neomechanical.neoutils.version.items;

import org.bukkit.Material;

public class WrapperNONLEGACY implements ItemVersionWrapper {

    @Override
    public Material stoneButton() {
        return Material.STONE_BUTTON;
    }

    @Override
    public Material oakButton() {
        Material modern = Material.matchMaterial("OAK_BUTTON");
        if (modern != null) {
            return modern;
        }
        Material legacy = Material.matchMaterial("WOOD_BUTTON");
        if (legacy != null) {
            return legacy;
        }
        return Material.STONE_BUTTON;
    }
}

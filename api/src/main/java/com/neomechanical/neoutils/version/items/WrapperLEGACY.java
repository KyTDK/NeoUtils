package com.neomechanical.neoutils.version.items;

import org.bukkit.Material;

public class WrapperLEGACY implements ItemVersionWrapper {

    @Override
    public Material stoneButton() {
        return Material.STONE_BUTTON;
    }

    @Override
    public Material oakButton() {
        return Material.WOOD_BUTTON;
    }
}

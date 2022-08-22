package com.neomechanical.neoutils.inventory.utils;

import org.bukkit.inventory.Inventory;

public class Size {
    public static int amountOfFilledSlots(Inventory inventory) {
        int contents = 0;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                contents++;
            }
        }
        return contents;
    }
}

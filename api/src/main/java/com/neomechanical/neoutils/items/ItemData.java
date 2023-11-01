package com.neomechanical.neoutils.items;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

@Data
public class ItemData {
    private final Map<ItemStack, SpecialItem> specialItems = new HashMap<>();
}

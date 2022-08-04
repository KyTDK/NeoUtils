package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.GUIAction;
import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class InventoryItem {
    private @NotNull final ItemStack item;
    private @Nullable final GUIAction action;
    private @Nullable final InventoryItemType type;
}

package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.GUIAction;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
public class InventoryItem {
    private @NotNull final ItemStack item;
    private @Nullable final String opens;
    private @Nullable final GUIAction action;
}

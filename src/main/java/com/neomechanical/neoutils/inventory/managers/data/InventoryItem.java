package com.neomechanical.neoutils.inventory.managers.data;

import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import lombok.Data;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Data
public class InventoryItem {
    private @NotNull final ItemStack item;
    private @Nullable final Consumer<InventoryClickEvent> action;
    private @Nullable final InventoryItemType type;
}

package com.neomechanical.neoutils.inventory.managers.data;


import com.neomechanical.neoutils.inventory.items.InventoryItemType;
import lombok.Data;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
public class InventoryItem {
    private @NotNull
    final Supplier<ItemStack> item;
    private
    final Map<ClickType[], Consumer<InventoryClickEvent>> action;
    private @Nullable
    final InventoryItemType type;

    public InventoryItem(InventoryItemBuilder inventoryItemBuilder) {
        this.item = inventoryItemBuilder.item;
        this.action = inventoryItemBuilder.action;
        this.type = inventoryItemBuilder.type;
    }

    @NotNull
    public Supplier<ItemStack> getItem() {
        return item;
    }

    @Nullable
    public Consumer<InventoryClickEvent> getAction(ClickType clickType) {
        return action.get(clickType);
    }

    @Nullable
    public InventoryItemType getType() {
        return type;
    }

    public static class InventoryItemBuilder {
        //Required
        private @NotNull Supplier<ItemStack> item;
        //Optional
        private Map<ClickType[], Consumer<InventoryClickEvent>> action;
        private @Nullable InventoryItemType type;

        public InventoryItemBuilder(@NotNull Supplier<ItemStack> item) {
            this.item = item;
        }

        public InventoryItemBuilder setItem(Supplier<ItemStack> item) {
            this.item = item;
            return this;
        }

        public InventoryItemBuilder setAction(Consumer<InventoryClickEvent> action, @Nullable ClickType... clickType) {
            this.action.put(clickType, action);
            return this;
        }

        public InventoryItemBuilder setType(InventoryItemType type) {
            this.type = type;
            return this;
        }

        public InventoryItem build() {
            return new InventoryItem(this);
        }
    }
}

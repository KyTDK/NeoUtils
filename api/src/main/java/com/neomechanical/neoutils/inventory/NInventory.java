package com.neomechanical.neoutils.inventory;


import com.neomechanical.neoutils.inventory.managers.data.InventoryGUI;
import com.neomechanical.neoutils.inventory.managers.data.InventoryItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NInventory {
    /**
     * Returns the size of the inventory
     *
     * @return The size of the inventory
     */
    int getSize();

    /**
     * Returns the ItemStack found in the slot at the given index
     *
     * @param index The index of the Slot's ItemStack to return
     * @return The ItemStack in the slot
     */
    @Nullable
    InventoryItem getItem(int index);

    /**
     * Stores the ItemStack at the given index of the inventory.
     *
     * @param index The index where to put the ItemStack
     * @param item The ItemStack to set
     */
    NInventory setItem(int index, @Nullable InventoryItem item);
    /**
     * Stores the given ItemStacks in the inventory. This will try to fill
     * existing stacks and empty slots as well as it can.
     * <p>
     * The returned HashMap contains what it couldn't store, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all items are stored, it will return
     * an empty HashMap.
     * <p>
     * If you pass in ItemStacks which exceed the maximum stack size for the
     * Material, first they will be added to partial stacks where
     * Material.getMaxStackSize() is not exceeded, up to
     * Material.getMaxStackSize(). When there are no partial stacks left
     * stacks will be split on Inventory.getMaxStackSize() allowing you to
     * exceed the maximum stack size for that material.
     * <p>
     * It is known that in some implementations this method will also set
     * the inputted argument amount to the number of that item not placed in
     * slots.
     *
     * @param items The ItemStacks to add
     * @throws IllegalArgumentException if items or any element in it is null
     */
    NInventory addItem(@NotNull InventoryItem... items) throws IllegalArgumentException;
    /**
     * Removes the given ItemStacks from the inventory.
     * <p>
     * It will try to remove 'as much as possible' from the types and amounts
     * you give as arguments.
     * <p>
     * The returned HashMap contains what it couldn't remove, where the key is
     * the index of the parameter, and the value is the ItemStack at that
     * index of the varargs parameter. If all the given ItemStacks are
     * removed, it will return an empty HashMap.
     * <p>
     * It is known that in some implementations this method will also set the
     * inputted argument amount to the number of that item not removed from
     * slots.
     *
     * @param items The ItemStacks to remove
     * @throws IllegalArgumentException if items is null
     */
    NInventory removeItem(@NotNull InventoryItem... items) throws IllegalArgumentException;

    /**
     * Returns all ItemStacks from the inventory
     *
     * @return An array of ItemStacks from the inventory. Individual items may be null.
     */
    @NotNull
    ItemStack[] getContents();
    /**
     * Completely replaces the inventory's contents. Removes all existing
     * contents and replaces it with the ItemStacks given in the array.
     *
     * @param items A complete replacement for the contents; the length must
     *     be less than or equal to {@link #getSize()}.
     * @throws IllegalArgumentException If the array has more items than the
     *     inventory.
     */
    NInventory setContents(@NotNull InventoryItem[] items) throws IllegalArgumentException;

    /**
     * Set the inventory to open the given inventory when closed
     *
     * @param inventory the inventory to open when closed
     */
    NInventory setOpenOnClose(@Nullable InventoryGUI inventory);

    NInventory setUnregisterOnClose(boolean unregister);

    NInventory setPagesInheritParentSettings(boolean inherit);
}

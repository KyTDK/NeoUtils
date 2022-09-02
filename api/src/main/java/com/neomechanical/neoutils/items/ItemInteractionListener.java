package com.neomechanical.neoutils.items;

import com.neomechanical.neoutils.NeoUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ItemInteractionListener implements Listener {
    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack itemStack = event.getItem();
        Map<ItemStack, SpecialItem> specialItemMap = NeoUtils.getDataHandler().getItemData().getSpecialItems();
        if (specialItemMap.containsKey(itemStack)) {
            for (Consumer<PlayerInteractEvent> action : Objects.requireNonNull(specialItemMap.get(itemStack).getAction(event.getAction()))) {
                action.accept(event);
            }
        }
    }
}

package com.neomechanical.neoutils.version.items;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class ItemEventHandlerNONLEGACY implements IItemEventHandlerWrapper {
    @Override
    public boolean preventDoubleFire(PlayerInteractEvent event) {
        return event.getHand() != EquipmentSlot.HAND;
    }
}

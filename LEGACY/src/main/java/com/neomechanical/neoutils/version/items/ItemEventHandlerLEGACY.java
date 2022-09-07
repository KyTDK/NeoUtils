package com.neomechanical.neoutils.version.items;

import org.bukkit.event.player.PlayerInteractEvent;

public class ItemEventHandlerLEGACY implements IItemEventHandlerWrapper {
    @Override
    public boolean preventDoubleFire(PlayerInteractEvent event) {
        return false;
    }
}

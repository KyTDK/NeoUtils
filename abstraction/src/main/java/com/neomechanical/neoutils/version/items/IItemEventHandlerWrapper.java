package com.neomechanical.neoutils.version.items;

import org.bukkit.event.player.PlayerInteractEvent;

public interface IItemEventHandlerWrapper {
    boolean handleInteractionEvent(PlayerInteractEvent event);
}

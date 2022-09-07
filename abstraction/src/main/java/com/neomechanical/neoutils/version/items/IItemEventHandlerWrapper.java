package com.neomechanical.neoutils.version.items;

import com.neomechanical.neoutils.version.VersionWrapper;
import org.bukkit.event.player.PlayerInteractEvent;

public interface IItemEventHandlerWrapper extends VersionWrapper {
    /**
     * @param event PlayerInteractEvent event
     * @return Returns whether the event was cancelled
     */
    boolean preventDoubleFire(PlayerInteractEvent event);
}

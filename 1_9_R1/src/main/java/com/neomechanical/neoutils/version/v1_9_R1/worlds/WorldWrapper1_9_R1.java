package com.neomechanical.neoutils.version.v1_9_R1.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import org.bukkit.World;

public class WorldWrapper1_9_R1 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        return world.getWorldType().name();
    }
}

package com.neomechanical.neoutils.version.v1_19_R3.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import org.bukkit.World;

public class WorldWrapper1_19_R3 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        return world.getKey().toString();
    }
}

package com.neomechanical.neoutils.version.v1_17_R1.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class WorldWrapper1_17_R1 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        return ((CraftWorld) world).getHandle().getTypeKey().toString();
    }
}

package com.neomechanical.neoutils.version.v1_14_4_R1.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

public class WorldWrapper1_14_4_R1 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        net.minecraft.server.v1_14_R1.World w = ((CraftWorld) world).getHandle();
        return w.P().name();
    }
}

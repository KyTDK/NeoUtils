package com.neomechanical.neoutils.version.v1_16_R2.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;

public class WorldWrapper1_16_R2 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        net.minecraft.server.v1_16_R2.World w = ((CraftWorld) world).getHandle();
        return w.getDimensionKey().a().getKey();
    }
}

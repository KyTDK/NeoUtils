package com.neomechanical.neoutils.version.v1_18_R2.worlds;

import com.neomechanical.neoutils.version.worlds.IWorldNMS;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;

public class WorldWrapper1_18_R2 implements IWorldNMS {
    public String getWorldNamespaceKey(World world) {
        ServerLevel w = ((CraftWorld) world).getHandle();
        return w.getTypeKey().toString();
    }
}

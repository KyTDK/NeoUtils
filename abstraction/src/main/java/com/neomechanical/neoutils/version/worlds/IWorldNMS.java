package com.neomechanical.neoutils.version.worlds;

import com.neomechanical.neoutils.version.VersionWrapper;
import org.bukkit.World;

public interface IWorldNMS extends VersionWrapper {
    String getWorldNamespaceKey(World world);
}

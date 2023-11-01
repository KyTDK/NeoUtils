package com.neomechanical.neoutils.config;


import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public final class ConfigUtil {
    private ConfigUtil() {}

    @NotNull
    public static ConfigurationSection[] getConfigurationSections(@NotNull ConfigurationSection section) {
        return section.getKeys(false).stream()
                .filter(section::isConfigurationSection)
                .map(section::getConfigurationSection)
                .toArray(ConfigurationSection[]::new);
    }
}

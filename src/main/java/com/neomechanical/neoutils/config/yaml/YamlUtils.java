package com.neomechanical.neoutils.config.yaml;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class YamlUtils {
    public static boolean isConfigurationSection(Map<String, Object> data, String key) {
        for (Object value : data.values()) {
            if (value instanceof Map) {
                if (data.get(key) == value) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isConfigurationSection(Object value) {
        return value instanceof Map;
    }

    public static @Nullable ArrayList<YamlConfSection> getConfigurationSections(Map<String, Object> data) {
        ArrayList<YamlConfSection> sections = new ArrayList<>();
        Set<String> keys = YamlKeys.getKeys(false, data);
        if (keys == null) {
            return null;
        }
        for (String key : keys) {
            Object value = data.get(key);
            if (isConfigurationSection(value)) {
                sections.add(getConfigurationSection(data, key));
            }
        }
        return sections;
    }

    public static @Nullable YamlConfSection getConfigurationSection(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (isConfigurationSection(value)) {
            return new YamlConfSection(key, (Map<String, Object>) value);
        }
        return null;
    }

    public static void save(@NotNull Yaml yaml, File file, Map<String, Object> dataRaw) throws IOException {
        Preconditions.checkArgument(file != null, "File cannot be null");

        try (Writer writer = new OutputStreamWriter(java.nio.file.Files.newOutputStream(file.toPath()), Charsets.UTF_8)) {
            String data = dataRaw.toString();
            writer.write(data);
        }
    }
}

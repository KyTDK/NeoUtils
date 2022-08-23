package com.neomechanical.neoutils.config.yaml;

import com.google.common.base.Preconditions;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void save(File file, Map<String, Object> dataRaw) throws IOException {
        Preconditions.checkArgument(file != null, "File cannot be null");
        //Create an accurate yaml container by converting the map to YAML style
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        final Yaml yaml = new Yaml(options);
        final FileWriter writer = new FileWriter(file);
        yaml.dump(dataRaw, writer);
    }
}

package com.neomechanical.neoutils.config.yaml;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String, Object> getConfigurationSection(Map<String, Object> data, String key) {
        for (Object value : data.values()) {
            if (value instanceof Map) {
                if (data.get(key) == value) {
                    Map<String, Object> map = new HashMap<>();
                    for (Field field : value.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        try {
                            map.put(field.getName(), field.get(value));
                        } catch (Exception ignored) {
                        }
                    }
                    return map;
                }
            }
        }
        return null;
    }
}

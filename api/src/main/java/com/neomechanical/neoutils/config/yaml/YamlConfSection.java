package com.neomechanical.neoutils.config.yaml;


import lombok.Data;

import java.util.Map;

@Data
public class YamlConfSection {
    public String name;
    public Map<String, Object> data;

    public YamlConfSection(String name, Map<String, Object> data) {
        this.name = name;
        this.data = data;
    }
}

package com.neomechanical.neoutils.version.versions;

public enum Versions {
    //Special versions
    vLEGACY("LEGACY"),
    vNONLEGACY("NONLEGACY"),
    v1_7_R4("1_7_R4"),
    v1_8_R1("1_8_R1"),
    v1_8_R2("1_8_R2"),
    v1_8_R3("1_8_R3"),
    v1_9_R1("1_9_R1"),
    v1_9_R2("1_9_R2"),
    v1_10_R1("1_10_R1"),
    v1_11_R1("1_11_R1"),
    v1_12_R1("1_12_R1"),
    v1_13_R1("1_13_R1"),
    v1_13_R2("1_13_R2"),
    v1_14_4_R1("1_14_4_R1"),
    v1_14_R1("1_14_R1"),
    v1_15_R1("1_15_R1"),
    v1_16_R1("1_16_R1"),
    v1_16_R2("1_16_R2"),
    v1_16_R3("1_16_R3"),
    v1_17_1_R1("1_17_1_R1"),
    v1_17_R1("1_17_R1"),
    v1_18_R1("1_18_R1"),
    v1_18_R2("1_18_R2"),
    v1_19_R1("1_19_R1"),
    v1_19_1_R1("1_19_1_R1"),
    v1_19_2_R1("1_19_2_R1");

    private final String text;

    Versions(final String text) {
        this.text = text;
    }

    public static Versions fromString(String text) {
        for (Versions v : Versions.values()) {
            if (v.text.equalsIgnoreCase(text)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}

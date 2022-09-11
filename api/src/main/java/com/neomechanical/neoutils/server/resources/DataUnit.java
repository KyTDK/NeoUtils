package com.neomechanical.neoutils.server.resources;

public enum DataUnit {

    /**
     * Bytes, represented by suffix {@code B}.
     */
    BYTES("B", Memory.ofBytes(1)),

    /**
     * Kilobytes, represented by suffix {@code KB}.
     */
    KILOBYTES("KB", Memory.ofKilobytes(1)),

    /**
     * Megabytes, represented by suffix {@code MB}.
     */
    MEGABYTES("MB", Memory.ofMegabytes(1)),

    /**
     * Gigabytes, represented by suffix {@code GB}.
     */
    GIGABYTES("GB", Memory.ofGigabytes(1)),

    /**
     * Terabytes, represented by suffix {@code TB}.
     */
    TERABYTES("TB", Memory.ofTerabytes(1));


    private final String suffix;

    private final Memory size;


    DataUnit(String suffix, Memory size) {
        this.suffix = suffix;
        this.size = size;
    }

    /**
     * Return the {@link DataUnit} matching the specified {@code suffix}.
     *
     * @param suffix one of the standard suffixes
     * @return the {@link DataUnit} matching the specified {@code suffix}
     * @throws IllegalArgumentException if the suffix does not match the suffix
     *                                  of any of this enum's constants
     */
    public static DataUnit fromSuffix(String suffix) {
        for (DataUnit candidate : values()) {
            if (candidate.suffix.equals(suffix)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("Unknown data unit suffix '" + suffix + "'");
    }

    Memory size() {
        return this.size;
    }

}
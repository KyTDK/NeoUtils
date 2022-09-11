package com.neomechanical.neoutils.server.resources;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Memory implements Comparable<Memory>, Serializable {

    /**
     * Bytes per Kilobyte.
     */
    private static final long BYTES_PER_KB = 1024;

    /**
     * Bytes per Megabyte.
     */
    private static final long BYTES_PER_MB = BYTES_PER_KB * 1024;

    /**
     * Bytes per Gigabyte.
     */
    private static final long BYTES_PER_GB = BYTES_PER_MB * 1024;

    /**
     * Bytes per Terabyte.
     */
    private static final long BYTES_PER_TB = BYTES_PER_GB * 1024;


    private final long bytes;


    private Memory(long bytes) {
        this.bytes = bytes;
    }


    /**
     * Obtain a {@link Memory} representing the specified number of bytes.
     *
     * @param bytes the number of bytes, positive or negative
     * @return a {@link Memory}
     */
    public static Memory ofBytes(long bytes) {
        return new Memory(bytes);
    }

    /**
     * Obtain a {@link Memory} representing the specified number of kilobytes.
     *
     * @param kilobytes the number of kilobytes, positive or negative
     * @return a {@link Memory}
     */
    public static Memory ofKilobytes(long kilobytes) {
        return new Memory(Math.multiplyExact(kilobytes, BYTES_PER_KB));
    }

    /**
     * Obtain a {@link Memory} representing the specified number of megabytes.
     *
     * @param megabytes the number of megabytes, positive or negative
     * @return a {@link Memory}
     */
    public static Memory ofMegabytes(long megabytes) {
        return new Memory(Math.multiplyExact(megabytes, BYTES_PER_MB));
    }

    /**
     * Obtain a {@link Memory} representing the specified number of gigabytes.
     *
     * @param gigabytes the number of gigabytes, positive or negative
     * @return a {@link Memory}
     */
    public static Memory ofGigabytes(long gigabytes) {
        return new Memory(Math.multiplyExact(gigabytes, BYTES_PER_GB));
    }

    /**
     * Obtain a {@link Memory} representing the specified number of terabytes.
     *
     * @param terabytes the number of terabytes, positive or negative
     * @return a {@link Memory}
     */
    public static Memory ofTerabytes(long terabytes) {
        return new Memory(Math.multiplyExact(terabytes, BYTES_PER_TB));
    }

    /**
     * Obtain a {@link Memory} representing an amount in the specified {@link DataUnit}.
     *
     * @param amount the amount of the size, measured in terms of the unit,
     *               positive or negative
     * @return a corresponding {@link Memory}
     */
    public static Memory of(long amount, DataUnit unit) {
        assert unit != null;
        return new Memory(Math.multiplyExact(amount, unit.size().toBytes()));
    }

    /**
     * Obtain a {@link Memory} from a text string such as {@code 12MB} using
     * {@link DataUnit#BYTES} if no unit is specified.
     * <p>
     * Examples:
     * <pre>
     * "12KB" -- parses as "12 kilobytes"
     * "5MB"  -- parses as "5 megabytes"
     * "20"   -- parses as "20 bytes"
     * </pre>
     *
     * @param text the text to parse
     * @return the parsed {@link Memory}
     * @see #parse(CharSequence, DataUnit)
     */
    public static Memory parse(CharSequence text) {
        return parse(text, null);
    }

    /**
     * Obtain a {@link Memory} from a text string such as {@code 12MB} using
     * the specified default {@link DataUnit} if no unit is specified.
     * <p>
     * The string starts with a number followed optionally by a unit matching one of the
     * supported {@linkplain DataUnit suffixes}.
     * <p>
     * Examples:
     * <pre>
     * "12KB" -- parses as "12 kilobytes"
     * "5MB"  -- parses as "5 megabytes"
     * "20"   -- parses as "20 kilobytes" (where the {@code defaultUnit} is {@link DataUnit#KILOBYTES})
     * </pre>
     *
     * @param text the text to parse
     * @return the parsed {@link Memory}
     */
    public static Memory parse(CharSequence text, @Nullable DataUnit defaultUnit) {
        assert text != null;
        try {
            Matcher matcher = DataSizeUtils.PATTERN.matcher(trimAllWhitespace(text));
            DataUnit unit = DataSizeUtils.determineDataUnit(matcher.group(2), defaultUnit);
            long amount = Long.parseLong(matcher.group(1));
            return Memory.of(amount, unit);
        } catch (Exception ex) {
            throw new IllegalArgumentException("'" + text + "' is not a valid data size", ex);
        }
    }

    public static boolean hasLength(@Nullable CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static CharSequence trimAllWhitespace(CharSequence text) {
        if (!hasLength(text)) {
            return text;
        }

        int len = text.length();
        StringBuilder sb = new StringBuilder(text.length());
        for (int i = 0; i < len; i++) {
            char c = text.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Checks if this size is negative, excluding zero.
     *
     * @return true if this size has a size less than zero bytes
     */
    public boolean isNegative() {
        return this.bytes < 0;
    }

    /**
     * Return the number of bytes in this instance.
     *
     * @return the number of bytes
     */
    public long toBytes() {
        return this.bytes;
    }

    /**
     * Return the number of kilobytes in this instance.
     *
     * @return the number of kilobytes
     */
    public long toKilobytes() {
        return this.bytes / BYTES_PER_KB;
    }

    /**
     * Return the number of megabytes in this instance.
     *
     * @return the number of megabytes
     */
    public long toMegabytes() {
        return this.bytes / BYTES_PER_MB;
    }

    /**
     * Return the number of gigabytes in this instance.
     *
     * @return the number of gigabytes
     */
    public long toGigabytes() {
        return this.bytes / BYTES_PER_GB;
    }

    /**
     * Return the number of terabytes in this instance.
     *
     * @return the number of terabytes
     */
    public long toTerabytes() {
        return this.bytes / BYTES_PER_TB;
    }

    @Override
    public int compareTo(Memory other) {
        return Long.compare(this.bytes, other.bytes);
    }

    @Override
    public String toString() {
        return String.format("%dB", this.bytes);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Memory otherSize = (Memory) other;
        return (this.bytes == otherSize.bytes);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.bytes);
    }

    /**
     * Static nested class to support lazy loading of the {@link #PATTERN}.
     *
     * @since 5.3.21
     */
    private static class DataSizeUtils {

        /**
         * The pattern for parsing.
         */
        private static final Pattern PATTERN = Pattern.compile("^([+\\-]?\\d+)([a-zA-Z]{0,2})$");

        private static DataUnit determineDataUnit(String suffix, @Nullable DataUnit defaultUnit) {
            DataUnit defaultUnitToUse = (defaultUnit != null ? defaultUnit : DataUnit.BYTES);
            return (hasLength(suffix) ? DataUnit.fromSuffix(suffix) : defaultUnitToUse);
        }

    }

}
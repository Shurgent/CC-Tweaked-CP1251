// SPDX-FileCopyrightText: 2020 The CC: Tweaked Developers
//
// SPDX-License-Identifier: MPL-2.0

package dan200.computercraft.api.lua;

import org.jspecify.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Map;

/**
 * Various utility functions for operating with Lua values.
 *
 * @see IArguments
 */
public final class LuaValues {
    private static final char[] CP1251 = new char[256];

    static {
        for (var i = 0; i < 128; i++) CP1251[i] = (char) i;

        CP1251[0x80] = '\u0402';
        CP1251[0x81] = '\u0403';
        CP1251[0x82] = '\u201a';
        CP1251[0x83] = '\u0453';
        CP1251[0x84] = '\u201e';
        CP1251[0x85] = '\u2026';
        CP1251[0x86] = '\u2020';
        CP1251[0x87] = '\u2021';
        CP1251[0x88] = '\u20ac';
        CP1251[0x89] = '\u2030';
        CP1251[0x8a] = '\u0409';
        CP1251[0x8b] = '\u2039';
        CP1251[0x8c] = '\u040a';
        CP1251[0x8d] = '\u040c';
        CP1251[0x8e] = '\u040b';
        CP1251[0x8f] = '\u040f';
        CP1251[0x90] = '\u0452';
        CP1251[0x91] = '\u2018';
        CP1251[0x92] = '\u2019';
        CP1251[0x93] = '\u201c';
        CP1251[0x94] = '\u201d';
        CP1251[0x95] = '\u2022';
        CP1251[0x96] = '\u2013';
        CP1251[0x97] = '\u2014';
        CP1251[0x98] = '\u0098';
        CP1251[0x99] = '\u2122';
        CP1251[0x9a] = '\u0459';
        CP1251[0x9b] = '\u203a';
        CP1251[0x9c] = '\u045a';
        CP1251[0x9d] = '\u045c';
        CP1251[0x9e] = '\u045b';
        CP1251[0x9f] = '\u045f';
        CP1251[0xa0] = '\u00a0';
        CP1251[0xa1] = '\u040e';
        CP1251[0xa2] = '\u045e';
        CP1251[0xa3] = '\u0408';
        CP1251[0xa4] = '\u00a4';
        CP1251[0xa5] = '\u0490';
        CP1251[0xa6] = '\u00a6';
        CP1251[0xa7] = '\u00a7';
        CP1251[0xa8] = '\u0401';
        CP1251[0xa9] = '\u00a9';
        CP1251[0xaa] = '\u0404';
        CP1251[0xab] = '\u00ab';
        CP1251[0xac] = '\u00ac';
        CP1251[0xad] = '\u00ad';
        CP1251[0xae] = '\u00ae';
        CP1251[0xaf] = '\u0407';
        CP1251[0xb0] = '\u00b0';
        CP1251[0xb1] = '\u00b1';
        CP1251[0xb2] = '\u0406';
        CP1251[0xb3] = '\u0456';
        CP1251[0xb4] = '\u0491';
        CP1251[0xb5] = '\u00b5';
        CP1251[0xb6] = '\u00b6';
        CP1251[0xb7] = '\u00b7';
        CP1251[0xb8] = '\u0451';
        CP1251[0xb9] = '\u2116';
        CP1251[0xba] = '\u0454';
        CP1251[0xbb] = '\u00bb';
        CP1251[0xbc] = '\u0458';
        CP1251[0xbd] = '\u0405';
        CP1251[0xbe] = '\u0455';
        CP1251[0xbf] = '\u0457';

        for (var i = 0; i < 32; i++) {
            CP1251[0xc0 + i] = (char) ('\u0410' + i);
            CP1251[0xe0 + i] = (char) ('\u0430' + i);
        }
    }

    private LuaValues() {
    }

    /**
     * Encode a Lua string into a read-only {@link ByteBuffer}.
     *
     * @param string The string to encode.
     * @return The encoded string.
     */
    public static ByteBuffer encode(String string) {
        return ByteBuffer.wrap(encodeBytes(string)).asReadOnlyBuffer();
    }

    /**
     * Encode a Java string into bytes using ComputerCraft's terminal code page.
     *
     * @param string The string to encode.
     * @return The encoded string.
     */
    public static byte[] encodeBytes(String string) {
        var bytes = new byte[string.length()];
        for (var i = 0; i < bytes.length; i++) bytes[i] = (byte) encodeChar(string.charAt(i));
        return bytes;
    }

    /**
     * Encode a single character using ComputerCraft's terminal code page.
     *
     * @param chr The character to encode.
     * @return The encoded character, or {@code '?'} if it is not representable.
     */
    public static int encodeChar(int chr) {
        var value = tryEncodeChar(chr);
        return value < 0 ? '?' : value;
    }

    /**
     * Attempt to encode a single character using ComputerCraft's terminal code page.
     *
     * @param chr The character to encode.
     * @return The encoded character, or {@code -1} if it is not representable.
     */
    public static int tryEncodeChar(int chr) {
        if (chr < 0) return -1;
        if (chr < 128) return chr;
        if (chr >= '\u0410' && chr <= '\u042f') return 0xc0 + chr - '\u0410';
        if (chr >= '\u0430' && chr <= '\u044f') return 0xe0 + chr - '\u0430';

        return switch (chr) {
            case '\u0402' -> 0x80;
            case '\u0403' -> 0x81;
            case '\u201a' -> 0x82;
            case '\u0453' -> 0x83;
            case '\u201e' -> 0x84;
            case '\u2026' -> 0x85;
            case '\u2020' -> 0x86;
            case '\u2021' -> 0x87;
            case '\u20ac' -> 0x88;
            case '\u2030' -> 0x89;
            case '\u0409' -> 0x8a;
            case '\u2039' -> 0x8b;
            case '\u040a' -> 0x8c;
            case '\u040c' -> 0x8d;
            case '\u040b' -> 0x8e;
            case '\u040f' -> 0x8f;
            case '\u0452' -> 0x90;
            case '\u2018' -> 0x91;
            case '\u2019' -> 0x92;
            case '\u201c' -> 0x93;
            case '\u201d' -> 0x94;
            case '\u2022' -> 0x95;
            case '\u2013' -> 0x96;
            case '\u2014' -> 0x97;
            case '\u0098' -> 0x98;
            case '\u2122' -> 0x99;
            case '\u0459' -> 0x9a;
            case '\u203a' -> 0x9b;
            case '\u045a' -> 0x9c;
            case '\u045c' -> 0x9d;
            case '\u045b' -> 0x9e;
            case '\u045f' -> 0x9f;
            case '\u00a0' -> 0xa0;
            case '\u040e' -> 0xa1;
            case '\u045e' -> 0xa2;
            case '\u0408' -> 0xa3;
            case '\u00a4' -> 0xa4;
            case '\u0490' -> 0xa5;
            case '\u00a6' -> 0xa6;
            case '\u00a7' -> 0xa7;
            case '\u0401' -> 0xa8;
            case '\u00a9' -> 0xa9;
            case '\u0404' -> 0xaa;
            case '\u00ab' -> 0xab;
            case '\u00ac' -> 0xac;
            case '\u00ad' -> 0xad;
            case '\u00ae' -> 0xae;
            case '\u0407' -> 0xaf;
            case '\u00b0' -> 0xb0;
            case '\u00b1' -> 0xb1;
            case '\u0406' -> 0xb2;
            case '\u0456' -> 0xb3;
            case '\u0491' -> 0xb4;
            case '\u00b5' -> 0xb5;
            case '\u00b6' -> 0xb6;
            case '\u00b7' -> 0xb7;
            case '\u0451' -> 0xb8;
            case '\u2116' -> 0xb9;
            case '\u0454' -> 0xba;
            case '\u00bb' -> 0xbb;
            case '\u0458' -> 0xbc;
            case '\u0405' -> 0xbd;
            case '\u0455' -> 0xbe;
            case '\u0457' -> 0xbf;
            default -> -1;
        };
    }

    /**
     * Decode one byte using ComputerCraft's terminal code page.
     *
     * @param value The unsigned byte value.
     * @return The decoded character.
     */
    public static char decodeByte(int value) {
        return CP1251[value & 0xFF];
    }

    /**
     * Decode a byte array using ComputerCraft's terminal code page.
     *
     * @param bytes  The bytes to decode.
     * @param offset The start offset.
     * @param length The number of bytes to decode.
     * @return The decoded string.
     */
    public static String decode(byte[] bytes, int offset, int length) {
        var chars = new char[length];
        for (var i = 0; i < length; i++) chars[i] = decodeByte(bytes[offset + i]);
        return new String(chars);
    }

    /**
     * Decode a byte buffer using ComputerCraft's terminal code page.
     *
     * @param bytes The bytes to decode.
     * @return The decoded string.
     */
    public static String decode(ByteBuffer bytes) {
        var copy = bytes.slice();
        var chars = new char[copy.remaining()];
        for (var i = 0; i < chars.length; i++) chars[i] = decodeByte(copy.get());
        return new String(chars);
    }

    /**
     * Returns a more detailed representation of this number's type. If this is finite, it will just return "number",
     * otherwise it returns whether it is infinite or NaN.
     *
     * @param value The value to extract the type for.
     * @return This value's numeric type.
     */
    public static String getNumericType(double value) {
        if (Double.isNaN(value)) return "nan";
        if (value == Double.POSITIVE_INFINITY) return "inf";
        if (value == Double.NEGATIVE_INFINITY) return "-inf";
        return "number";
    }

    /**
     * Get a string representation of the given value's type.
     *
     * @param value The value whose type we are trying to compute.
     * @return A string representation of the given value's type, in a similar format to that provided by Lua's
     * {@code type} function.
     */
    public static String getType(@Nullable Object value) {
        if (value == null) return "nil";
        if (value instanceof String) return "string";
        if (value instanceof Boolean) return "boolean";
        if (value instanceof Number) return "number";
        if (value instanceof Map) return "table";
        return "userdata";
    }

    /**
     * Construct a "bad argument" exception, from an {@link IArguments} argument and an expected type.
     *
     * @param arguments The current arguments.
     * @param index     The argument number, starting from 0.
     * @param expected  The expected type for this argument.
     * @return The constructed exception, which should be thrown immediately.
     */
    public static LuaException badArgumentOf(IArguments arguments, int index, String expected) {
        return badArgument(index, expected, arguments.getType(index));
    }

    /**
     * Construct a "bad argument" exception, from an expected and actual type.
     *
     * @param index    The argument number, starting from 0.
     * @param expected The expected type for this argument.
     * @param actual   The provided type for this argument.
     * @return The constructed exception, which should be thrown immediately.
     */
    public static LuaException badArgument(int index, String expected, String actual) {
        return new LuaException("bad argument #" + (index + 1) + " (" + expected + " expected, got " + actual + ")");
    }

    /**
     * Construct a table item exception, from an expected and actual type.
     *
     * @param index    The index into the table, starting from 1.
     * @param expected The expected type for this table item.
     * @param actual   The provided type for this table item.
     * @return The constructed exception, which should be thrown immediately.
     */
    public static LuaException badTableItem(int index, String expected, String actual) {
        return new LuaException("bad item #" + index + " (" + expected + " expected, got " + actual + ")");
    }

    /**
     * Construct a field exception, from an expected and actual type.
     *
     * @param key      The name of the field.
     * @param expected The expected type for this table item.
     * @param actual   The provided type for this table item.
     * @return The constructed exception, which should be thrown immediately.
     */
    public static LuaException badField(String key, String expected, String actual) {
        return new LuaException("bad field '" + key + "' (" + expected + " expected, got " + actual + ")");
    }

    /**
     * Ensure a numeric argument is finite (i.e. not infinite or {@link Double#NaN}.
     *
     * @param index The argument index to check.
     * @param value The value to check.
     * @return The input {@code value}.
     * @throws LuaException If this is not a finite number.
     */
    public static Number checkFiniteNum(int index, Number value) throws LuaException {
        checkFinite(index, value.doubleValue());
        return value;
    }

    /**
     * Ensure a numeric argument is finite (i.e. not infinite or {@link Double#NaN}.
     *
     * @param index The argument index to check.
     * @param value The value to check.
     * @return The input {@code value}.
     * @throws LuaException If this is not a finite number.
     */
    public static double checkFinite(int index, double value) throws LuaException {
        if (!Double.isFinite(value)) throw badArgument(index, "number", getNumericType(value));
        return value;
    }

    static double checkFiniteIndex(int index, double value) throws LuaException {
        if (!Double.isFinite(value)) throw badTableItem(index, "number", getNumericType(value));
        return value;
    }

    static double checkFiniteField(String key, double value) throws LuaException {
        if (!Double.isFinite(value)) throw badField(key, "number", getNumericType(value));
        return value;
    }

    /**
     * Ensure a string is a valid enum value.
     *
     * @param index The argument index to check.
     * @param klass The class of the enum instance.
     * @param value The value to extract.
     * @param <T>   The type of enum we are extracting.
     * @return The parsed enum value.
     * @throws LuaException If this is not a known enum value.
     */
    public static <T extends Enum<T>> T checkEnum(int index, Class<T> klass, String value) throws LuaException {
        for (var possibility : klass.getEnumConstants()) {
            if (possibility.name().equalsIgnoreCase(value)) return possibility;
        }

        throw new LuaException("bad argument #" + (index + 1) + " (unknown option " + value + ")");
    }
}

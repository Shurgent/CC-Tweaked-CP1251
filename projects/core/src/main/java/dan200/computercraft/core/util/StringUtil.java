// SPDX-FileCopyrightText: 2017 The CC: Tweaked Developers
//
// SPDX-License-Identifier: MPL-2.0

package dan200.computercraft.core.util;

import dan200.computercraft.api.lua.LuaValues;
import dan200.computercraft.core.input.ComputerInput;

import java.nio.ByteBuffer;

public final class StringUtil {
    public static final int MAX_PASTE_LENGTH = 512;

    private StringUtil() {
    }

    /**
     * Convert a Unicode character to a terminal one.
     *
     * @param chr The Unicode character.
     * @return The terminal character. This is either in the range [0, 255] (if a valid character) or {@code -1} if
     * it cannot be mapped to CC's charset.
     */
    public static int unicodeToTerminal(int chr) {
        return LuaValues.tryEncodeChar(chr);
    }

    /**
     * Check if a character is capable of being input and passed to a {@linkplain ComputerInput#charTyped(byte) "char"
     * event}.
     *
     * @param chr The character to check.
     * @return Whether this character can be typed.
     */
    public static boolean isTypableChar(byte chr) {
        return isTypableChar(chr & 0xFF);
    }

    /**
     * Check if a character is capable of being input and passed to a {@linkplain ComputerInput#charTyped(byte) "char"
     * * event}.
     *
     * @param chr The character to check.
     * @return Whether this character can be typed.
     */
    public static boolean isTypableChar(int chr) {
        return chr >= 0 && chr <= 255 && chr != 0 && chr != '\r' && chr != '\n';
    }

    private static boolean isAllowedInLabel(char c) {
        return c >= ' ' && c != 127 && c != 167 && LuaValues.tryEncodeChar(c) >= 0;
    }

    public static String normaliseLabel(String text) {
        var length = Math.min(32, text.length());
        var builder = new StringBuilder(length);
        for (var i = 0; i < length; i++) {
            var c = text.charAt(i);
            builder.append(isAllowedInLabel(c) ? c : '?');
        }
        return builder.toString();
    }

    public static String normaliseText(String text) {
        var builder = new StringBuilder(text.length());
        for (var i = 0; i < text.length(); i++) {
            var c = text.charAt(i);
            builder.append(isAllowedInText(c) ? c : '?');
        }
        return builder.toString();
    }

    public static char toTerminalChar(char chr) {
        if (chr <= 255) return chr;

        var terminal = unicodeToTerminal(chr);
        return terminal < 0 ? '?' : (char) terminal;
    }

    private static boolean isAllowedInText(char c) {
        return c >= ' ' && c != 127 && LuaValues.tryEncodeChar(c) >= 0;
    }

    /**
     * Convert a Java string to a Lua one (using the terminal charset), suitable for pasting into a computer.
     * <p>
     * This removes special characters and strips to the first line of text.
     *
     * @param clipboard The text from the clipboard.
     * @return The encoded clipboard text.
     */
    public static ByteBuffer getClipboardString(String clipboard) {
        var output = new byte[Math.min(MAX_PASTE_LENGTH, clipboard.length())];
        var idx = 0;

        var iterator = clipboard.codePoints().iterator();
        while (iterator.hasNext() && idx < output.length) {
            var chr = unicodeToTerminal(iterator.next());
            if (chr < 0) continue; // Strip out unconvertible characters
            if (!isTypableChar(chr)) break; // Stop at untypable ones.
            output[idx++] = (byte) chr;
        }

        return ByteBuffer.wrap(output, 0, idx).asReadOnlyBuffer();
    }
}

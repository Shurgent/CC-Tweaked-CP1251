// Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
//
// SPDX-License-Identifier: LicenseRef-CCPL

package dan200.computercraft.shared.integration.cccbridge;

import dan200.computercraft.api.lua.LuaValues;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.core.util.StringUtil;

public final class CccBridgeText {
    private CccBridgeText() {
    }

    public static String toComputerText(String text) {
        return StringUtil.normaliseText(text);
    }

    public static String fromTerminalText(String text) {
        var builder = new StringBuilder(text.length());
        for (var i = 0; i < text.length(); i++) {
            var chr = text.charAt(i);
            builder.append(chr <= 0xff ? LuaValues.decodeByte(chr) : chr);
        }

        return StringUtil.normaliseText(builder.toString());
    }

    public static String fromTerminalBuffer(TextBuffer text) {
        var builder = new StringBuilder(text.length());
        for (var i = 0; i < text.length(); i++) {
            builder.append(LuaValues.decodeByte(text.charAt(i)));
        }

        return builder.toString();
    }
}

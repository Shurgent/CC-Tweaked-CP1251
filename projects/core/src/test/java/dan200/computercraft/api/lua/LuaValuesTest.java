// SPDX-FileCopyrightText: 2020 The CC: Tweaked Developers
//
// SPDX-License-Identifier: MPL-2.0

package dan200.computercraft.api.lua;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LuaValuesTest {
    @Test
    void testEncodeCp1251Cyrillic() {
        assertArrayEquals(
            new byte[]{
                (byte) 0xc0, (byte) 0xc1, (byte) 0xc2, (byte) 0xc3,
                (byte) 0xa5, (byte) 0xaa, (byte) 0xb2, (byte) 0xaf,
                (byte) 0xe0, (byte) 0xe1, (byte) 0xe2, (byte) 0xe3,
                (byte) 0xb4, (byte) 0xba, (byte) 0xb3, (byte) 0xbf,
            },
            LuaValues.encodeBytes("\u0410\u0411\u0412\u0413\u0490\u0404\u0406\u0407\u0430\u0431\u0432\u0433\u0491\u0454\u0456\u0457")
        );
    }

    @Test
    void testDecodeCp1251Cyrillic() {
        assertEquals(
            "\u041f\u0440\u0438\u0432\u0456\u0442, \u0423\u043a\u0440\u0430\u0457\u043d\u0430",
            LuaValues.decode(new byte[]{
                (byte) 0xcf, (byte) 0xf0, (byte) 0xe8, (byte) 0xe2, (byte) 0xb3, (byte) 0xf2,
                (byte) 0x2c, (byte) 0x20,
                (byte) 0xd3, (byte) 0xea, (byte) 0xf0, (byte) 0xe0, (byte) 0xbf, (byte) 0xed, (byte) 0xe0,
            }, 0, 15)
        );
    }

    @Test
    void testUnsupportedCharactersBecomeQuestionMarks() {
        assertArrayEquals(new byte[]{ '?' }, LuaValues.encodeBytes("\u2603"));
    }
}

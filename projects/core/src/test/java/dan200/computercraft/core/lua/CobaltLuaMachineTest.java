// Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
//
// SPDX-License-Identifier: LicenseRef-CCPL

package dan200.computercraft.core.lua;

import org.junit.jupiter.api.Test;
import org.squiddev.cobalt.ValueFactory;

import java.util.IdentityHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CobaltLuaMachineTest {
    @Test
    void testLuaStringsDecodeAsCp1251() {
        assertEquals(
            "\u041f\u0440\u0438\u0432\u0435\u0442",
            CobaltLuaMachine.toObject(
                ValueFactory.valueOf(new byte[]{ (byte) 0xcf, (byte) 0xf0, (byte) 0xe8, (byte) 0xe2, (byte) 0xe5, (byte) 0xf2 }),
                new IdentityHashMap<>()
            )
        );
    }
}

/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.integration.cccbridge;

import dan200.computercraft.api.lua.LuaValues;
import dan200.computercraft.core.terminal.TextBuffer;
import dan200.computercraft.shared.util.StringUtil;

public final class CccBridgeText
{
    private CccBridgeText()
    {
    }

    public static String toComputerText( String text )
    {
        return StringUtil.normaliseText( text );
    }

    public static String fromTerminalText( String text )
    {
        StringBuilder builder = new StringBuilder( text.length() );
        for( int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt( i );
            builder.append( c <= 0xff ? LuaValues.decodeByte( c ) : c );
        }

        return StringUtil.normaliseText( builder.toString() );
    }

    public static String fromTerminalBuffer( TextBuffer text )
    {
        StringBuilder builder = new StringBuilder( text.length() );
        for( int i = 0; i < text.length(); i++ )
        {
            builder.append( LuaValues.decodeByte( text.charAt( i ) ) );
        }

        return builder.toString();
    }
}

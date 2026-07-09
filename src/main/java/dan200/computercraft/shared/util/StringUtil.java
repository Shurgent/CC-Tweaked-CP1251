/*
 * This file is part of ComputerCraft - http://www.computercraft.info
 * Copyright Daniel Ratcliffe, 2011-2022. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */
package dan200.computercraft.shared.util;

import dan200.computercraft.api.lua.LuaValues;

import javax.annotation.Nullable;
import java.nio.charset.Charset;

public final class StringUtil
{
    public static final Charset TERMINAL_CHARSET = Charset.forName( "windows-1251" );

    private StringUtil() {}

    public static String normaliseLabel( String label )
    {
        if( label == null ) return null;

        int length = Math.min( 32, label.length() );
        StringBuilder builder = new StringBuilder( length );
        for( int i = 0; i < length; i++ )
        {
            char c = label.charAt( i );
            if( isTypableChar( c ) )
            {
                builder.append( c );
            }
            else
            {
                builder.append( '?' );
            }
        }

        return builder.toString();
    }

    public static boolean isTypableChar( char c )
    {
        return c >= ' ' && c != 127 && LuaValues.tryEncodeChar( c ) >= 0;
    }

    public static String normaliseText( String text )
    {
        StringBuilder builder = new StringBuilder( text.length() );
        for( int i = 0; i < text.length(); i++ )
        {
            char c = text.charAt( i );
            builder.append( isTypableChar( c ) ? c : '?' );
        }

        return builder.toString();
    }

    public static char toTerminalChar( char c )
    {
        if( c <= 255 ) return c;

        int encoded = LuaValues.tryEncodeChar( c );
        return encoded < 0 ? '?' : (char) encoded;
    }

    public static String toString( @Nullable Object value )
    {
        return value == null ? "" : value.toString();
    }
}

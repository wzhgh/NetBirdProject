package com.netbirdtech.android.app.jiaoyiquan.utils;

/**
 *
 */
public class StringUtils {
    public static boolean isEmpty( String input )
    {
        if ( input == null || "".equals( input ) || "null".equals(input) )
            return true;

        for ( int i = 0; i < input.length(); i++ )
        {
            char c = input.charAt( i );
            if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String s) {
        return (s != null && s.trim().length() > 0);
    }
}

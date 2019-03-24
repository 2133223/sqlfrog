
package com.gudusoft.sqlfrog.util;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.util.keywordChecker;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ObjectNameUtils
{

	public static boolean checkSQLName( TObjectName identifier )
	{
		String name = trimObjectName( identifier.getEndToken( ).astext );
		// the variable could not be null or empty and ""
		if ( name == null || name.length( ) == 0 || name.trim( ) == "" )
			return false;

		// check the first character
		char first = name.charAt( 0 );
		if ( !isFirstChar( first ) )
		{
			return false;
		}

		// check the content of the name after the first character
		for ( int i = 1; i < name.length( ); i++ )
		{
			char c = name.charAt( i );
			if ( ( !Character.isLetterOrDigit( c ) ) && ( c != '_' ) )
				return false;
		}

		EDbVendor vendor = identifier.getEndToken( ).getDbvendor( );
		if ( isKeyword( name, vendor ) )
			return false;
		return true;
	}

	private static String trimObjectName( String string )
	{
		if ( string == null )
			return string;

		if ( string.startsWith( "\"" ) && string.endsWith( "\"" ) )
			return string.substring( 1, string.length( ) - 1 );

		if ( string.startsWith( "[" ) && string.endsWith( "]" ) )
			return string.substring( 1, string.length( ) - 1 );

		return string;
	}

	private static boolean isKeyword( String name, EDbVendor vendor )
	{
		List<String> versions = keywordChecker.getAvailableDbVersions( vendor );
		if ( versions == null || versions.isEmpty( ) )
		{
			versions = keywordChecker.getAvailableDbVersions( EDbVendor.dbvansi );
			vendor = EDbVendor.dbvansi;
		}
		return keywordChecker.isKeyword( name,
				vendor,
				versions.get( versions.size( ) - 1 ),
				true );
	}

	private static boolean isFirstChar( char c )
	{
		switch ( c )
		{
			case 'A' :
				return true;
			case 'B' :
				return true;
			case 'C' :
				return true;
			case 'D' :
				return true;
			case 'E' :
				return true;
			case 'F' :
				return true;
			case 'G' :
				return true;
			case 'H' :
				return true;
			case 'I' :
				return true;
			case 'J' :
				return true;
			case 'K' :
				return true;
			case 'L' :
				return true;
			case 'M' :
				return true;
			case 'N' :
				return true;
			case 'O' :
				return true;
			case 'P' :
				return true;
			case 'Q' :
				return true;
			case 'R' :
				return true;
			case 'S' :
				return true;
			case 'T' :
				return true;
			case 'U' :
				return true;
			case 'V' :
				return true;
			case 'W' :
				return true;
			case 'X' :
				return true;
			case 'Y' :
				return true;
			case 'Z' :
				return true;
			case 'a' :
				return true;
			case 'b' :
				return true;
			case 'c' :
				return true;
			case 'd' :
				return true;
			case 'e' :
				return true;
			case 'f' :
				return true;
			case 'g' :
				return true;
			case 'h' :
				return true;
			case 'i' :
				return true;
			case 'j' :
				return true;
			case 'k' :
				return true;
			case 'l' :
				return true;
			case 'm' :
				return true;
			case 'n' :
				return true;
			case 'o' :
				return true;
			case 'p' :
				return true;
			case 'q' :
				return true;
			case 'r' :
				return true;
			case 's' :
				return true;
			case 't' :
				return true;
			case 'u' :
				return true;
			case 'v' :
				return true;
			case 'w' :
				return true;
			case 'x' :
				return true;
			case 'y' :
				return true;
			case 'z' :
				return true;
			case '_' :
				return true;
			case '$' :
				return true;
		}
		return false;
	}

	private final static Map<String, String> objectNameMap = new ConcurrentHashMap<String, String>( );
	private final static List<Character> chars = Arrays.asList( new Character[]{
			'A',
			'B',
			'C',
			'D',
			'E',
			'F',
			'G',
			'H',
			'I',
			'J',
			'K',
			'L',
			'M',
			'N',
			'O',
			'P',
			'Q',
			'R',
			'S',
			'T',
			'U',
			'V',
			'W',
			'X',
			'Y',
			'Z',
			'a',
			'b',
			'c',
			'd',
			'e',
			'f',
			'g',
			'h',
			'i',
			'j',
			'k',
			'l',
			'm',
			'n',
			'o',
			'p',
			'q',
			'r',
			's',
			't',
			'u',
			'v',
			'w',
			'x',
			'y',
			'z'
	} );

	public static String cleanVariableName( TObjectName identifier )
	{
		String name = trimObjectName( identifier.getEndToken( ).astext );
		if ( objectNameMap.containsKey( name ) )
			return objectNameMap.get( name );

		if ( name == null || name.length( ) == 0 || name.trim( ) == "" )
			return null;

		// check the first character
		char first = name.charAt( 0 );
		if ( !isFirstChar( first ) )
		{
			name = name.replace( first,
					chars.get( new Random( ).nextInt( chars.size( ) ) ) );
		}

		for ( int i = 1; i < name.length( ); i++ )
		{
			char c = name.charAt( i );
			if ( ( !Character.isLetterOrDigit( c ) ) && ( c != '_' ) )
			{
				if ( c == ' ' )
				{
					name = name.replace( c, '_' );
				}
				else
				{
					name = name.replace( c,
							chars.get( new Random( ).nextInt( chars.size( ) ) ) );
				}
			}
		}

		EDbVendor vendor = identifier.getEndToken( ).getDbvendor( );
		if ( isKeyword( name, vendor ) )
		{
			name = chars.get( new Random( ).nextInt( chars.size( ) ) ) + name;
		}

		objectNameMap.put( trimObjectName( identifier.getEndToken( ).astext ),
				name );
		return name;
	}

	public static void clearCache( )
	{
		objectNameMap.clear( );
	}
}


package com.gudusoft.sqlfrog.converter.identifier;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.TSourceTokenList;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Identifier;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerIdentifierConverter extends AbstractIndentifierConverter
{

	protected ConvertInfo convert( Identifier identifier,
			EDbVendor targetVendor, boolean convert ) throws ConvertException
	{
		if ( identifier == null || identifier.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Identifier should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		EDbVendor sourceVendor = identifier.getVender( );
		if ( !EDbVendor.dbvmssql.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "identifier should match the sql server vendor, but "
					+ SQLUtil.getVendorName( sourceVendor ) );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		int startTokenIndex = identifier.getElement( ).getStartToken( ).posinlist;
		int endTokenIndex = identifier.getElement( ).getEndToken( ).posinlist;
		TSourceTokenList tokens = identifier.getElement( ).getStartToken( ).container;

		ConvertInfo info = generateConvertInfo( identifier.getElement( ),
				targetVendor );
		info.setNeedConvert( false );

		for ( int i = startTokenIndex; i <= endTokenIndex; i++ )
		{
			TSourceToken token = tokens.get( i );
			String tokenString = token.astext;
			if ( tokenString.startsWith( "[" ) && tokenString.endsWith( "]" ) )
			{
				if ( targetVendor == EDbVendor.dbvmysql )
				{
					if ( convert )
					{
						token.astext = "`"
								+ tokenString.substring( 1,
										tokenString.length( ) - 1 )
								+ "`";
					}
					info.setNeedConvert( true );
				}
				else if ( targetVendor != EDbVendor.dbvmssql )
				{
					if ( convert )
					{
						token.astext = "\""
								+ tokenString.substring( 1,
										tokenString.length( ) - 1 )
								+ "\"";
					}
					info.setNeedConvert( true );
				}
			}
		}

		return info;
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		return true;
	}

}

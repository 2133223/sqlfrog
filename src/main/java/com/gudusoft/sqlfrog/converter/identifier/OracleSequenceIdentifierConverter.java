
package com.gudusoft.sqlfrog.converter.identifier;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TFunctionCall;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TParseTreeNodeList;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Identifier;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleSequenceIdentifierConverter extends
		AbstractIndentifierConverter
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
		if ( !EDbVendor.dbvoracle.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "identifier should match the oracle vendor, but "
					+ SQLUtil.getVendorName( sourceVendor ) );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		TObjectName objectName = identifier.getElement( );

		if ( !enableConvert( targetVendor ) )
		{
			throw generateConvertException( objectName, targetVendor );
		}

		ConvertInfo info = generateConvertInfo( objectName,
				targetVendor,
				convert );
		info.setNeedConvert( false );

		if ( targetVendor == EDbVendor.dbvpostgresql )
		{
			String partString = objectName.getPartString( ).toUpperCase( );
			if ( partString.equals( "CURRVAL" )
					|| partString.equals( "NEXTVAL" ) )
			{
				if ( convert )
				{
					String convertSql = objectName.getPartString( )
							+ "("
							+ objectName.toString( ).replace( "."
									+ objectName.getPartString( ),
									"" )
							+ ")";

					TExpression expression = getExpression( objectName,
							targetVendor );
					expression.setExpressionType( EExpressionType.function_t );
					TFunctionCall functionCall = expression.getGsqlparser( )
							.parseFunctionCall( convertSql );
					expression.setFunctionCall( functionCall );
				}
				info.setNeedConvert( true );
			}
		}
		return info;
	}

	private TExpression getExpression( TObjectName objectName,
			EDbVendor targetVendor )
	{
		TParseTreeNodeList list = objectName.getStartToken( )
				.getNodesStartFromThisToken( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( list.getElement( i ) instanceof TExpression )
			{
				return (TExpression) list.getElement( i );
			}
		}
		throw generateConvertException( objectName, targetVendor );
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		switch ( targetVendor )
		{
			case dbvpostgresql :
				return true;
			default :
				return false;
		}
	}

}

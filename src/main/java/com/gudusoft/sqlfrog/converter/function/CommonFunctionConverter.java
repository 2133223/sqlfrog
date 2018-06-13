
package com.gudusoft.sqlfrog.converter.function;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TFunctionCall;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Function;
import com.gudusoft.sqlfrog.model.Tuple;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class CommonFunctionConverter extends AbstractFunctionConverter
{

	protected ConvertInfo convert( Function function, EDbVendor targetVendor,
			boolean convert ) throws ConvertException
	{
		if ( function == null || function.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Function should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		TFunctionCall functionCall = function.getElement( );
		String functionName = functionCall.getFunctionName( )
				.toString( )
				.toUpperCase( );
		if ( !convert )
		{
			if ( "SUBSTRING".equals( functionName )
					|| "SUBSTR".equals( functionName )
					|| "SUBSTRB".equals( functionName )
					|| "SUBSTRC".equals( functionName )
					|| "SUBSTR2".equals( functionName )
					|| "SUBSTR4".equals( functionName ) )
			{
				ConvertInfo info = new ConvertInfo( );
				info.setInfo( "E021-06, substring." );
				info.setPosition( new Tuple<Long>( functionCall.getFunctionName( )
						.getLineNo( ),
						functionCall.getFunctionName( ).getColumnNo( ) ) );
				String filePath = functionCall.getFunctionName( )
						.getStartToken( ).container.getGsqlparser( )
						.getSqlfilename( );
				if ( !SQLUtil.isEmpty( filePath ) )
				{
					info.setFilePath( filePath );
				}
				return info;
			}
			if ( "TRIM".equals( functionName )
					|| "LTRIM".equals( functionName )
					|| "RTRIM".equals( functionName ) )
			{
				ConvertInfo info = new ConvertInfo( );
				info.setInfo( "E021-06, trim." );
				info.setPosition( new Tuple<Long>( functionCall.getFunctionName( )
						.getLineNo( ),
						functionCall.getFunctionName( ).getColumnNo( ) ) );
				String filePath = functionCall.getFunctionName( )
						.getStartToken( ).container.getGsqlparser( )
						.getSqlfilename( );
				if ( !SQLUtil.isEmpty( filePath ) )
				{
					info.setFilePath( filePath );
				}
				return info;
			}
		}

		return null;
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		return true;
	}

}

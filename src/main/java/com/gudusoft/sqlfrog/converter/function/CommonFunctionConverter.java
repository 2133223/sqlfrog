
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
		EDbVendor vendor = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getDbVendor( );

		if ( !convert )
		{
			if ( isSubStringFunction( functionCall ) )
			{
				if ( getFunctionName( functionCall ).equals( "SUBSTRING" )
						&& vendor == EDbVendor.dbvoracle )
				{
					return handleFunctionConvertPoint( functionCall,
							"E021-06, SUBSTRING function: use SUBSTR function instead." );
				}
				else
				{
					return handleFunctionConvertPoint( functionCall,
							"E021-06, substring." );
				}
			}
			if ( isTrimFunction( functionCall ) )
			{
				return handleFunctionConvertPoint( functionCall,
						"E021-09, trim." );
			}

			if ( isLengthFunction( functionCall ) )
			{
				if ( getFunctionName( functionCall ).equals( "CHARACTER_LENGTH" )
						&& vendor == EDbVendor.dbvoracle )
				{
					return handleFunctionConvertPoint( functionCall,
							"E021-04, CHARACTER_LENGTH function: use LENGTH function instead." );
				}
				else
				{
					return handleFunctionConvertPoint( functionCall,
							"E021-04, character_length." );
				}
			}

			if ( isConcatFunction( functionCall ) )
			{
				return handleFunctionConvertPoint( functionCall,
						"E021-07, concatenation." );
			}

			if ( isLocalTimestampFunction( functionCall ) )
			{
				return handleFunctionConvertPoint( functionCall,
						"F051-08, localtimestamp." );
			}

			if ( getFunctionName( functionCall ).equals( "OCTET_LENGTH" )
					&& vendor == EDbVendor.dbvoracle )
			{
				return handleFunctionConvertPoint( functionCall,
						"E021-05, OCTET_LENGTH function: use LENGTHB function instead." );
			}

			if ( getFunctionName( functionCall ).equals( "POSITION" )
					&& vendor == EDbVendor.dbvoracle )
			{
				return handleFunctionConvertPoint( functionCall,
						"E021-11, POSITION function: use INSTR function instead." );
			}
		}

		return null;
	}

	private String getFunctionName( TFunctionCall functionCall )
	{
		return functionCall.getFunctionName( ).toString( ).toUpperCase( );
	}

	private ConvertInfo handleFunctionConvertPoint( TFunctionCall functionCall,
			String message )
	{
		ConvertInfo info = new ConvertInfo( );
		info.setInfo( message );
		info.setPosition( new Tuple<Long>( functionCall.getFunctionName( )
				.getLineNo( ), functionCall.getFunctionName( ).getColumnNo( ) ) );
		String filePath = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getSqlfilename( );
		if ( !SQLUtil.isEmpty( filePath ) )
		{
			info.setFilePath( filePath );
		}
		return info;
	}

	private boolean isLocalTimestampFunction( TFunctionCall functionCall )
	{
		return false;
	}

	private boolean isConcatFunction( TFunctionCall functionCall )
	{
		String functionName = getFunctionName( functionCall );
		EDbVendor vendor = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getDbVendor( );

		if ( "CONCAT".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvmysql )
			{
				return true;
			}
		}
		return false;
	}

	private boolean isSubStringFunction( TFunctionCall functionCall )
	{
		String functionName = getFunctionName( functionCall );
		EDbVendor vendor = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getDbVendor( );

		if ( "SUBSTRING".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvpostgresql
					|| vendor == EDbVendor.dbvdb2
					|| vendor == EDbVendor.dbvmssql
					|| vendor == EDbVendor.dbvmysql )
			{
				return true;
			}
		}

		if ( "SUBSTR".equals( functionName )
				|| "SUBSTRB".equals( functionName )
				|| "SUBSTRC".equals( functionName )
				|| "SUBSTR2".equals( functionName )
				|| "SUBSTR4".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvoracle )
			{
				return true;
			}
		}

		return false;
	}

	private boolean isTrimFunction( TFunctionCall functionCall )
	{
		String functionName = getFunctionName( functionCall );
		EDbVendor vendor = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getDbVendor( );
		if ( "LTRIM".equals( functionName ) || "RTRIM".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvdb2 || vendor == EDbVendor.dbvmssql )
			{
				return true;
			}
		}
		return false;
	}

	private boolean isLengthFunction( TFunctionCall functionCall )
	{
		String functionName = getFunctionName( functionCall );
		EDbVendor vendor = functionCall.getFunctionName( ).getStartToken( ).container.getGsqlparser( )
				.getDbVendor( );
		if ( "CHARACTER_LENGTH".equals( functionName )
				|| "LENGTH".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvdb2 )
			{
				return true;
			}
		}

		if ( "LEN".equals( functionName ) || "DATALENGTH".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvmssql )
			{
				return true;
			}
		}

		if ( "CHAR_LENGTH".equals( functionName )
				|| "LENGTH".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvmysql )
			{
				return true;
			}
		}

		if ( "LENGTH".equals( functionName ) )
		{
			if ( vendor == EDbVendor.dbvoracle )
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		return true;
	}

}

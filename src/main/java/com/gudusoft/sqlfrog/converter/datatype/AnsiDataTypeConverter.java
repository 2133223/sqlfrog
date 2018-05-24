
package com.gudusoft.sqlfrog.converter.datatype;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TTypeName;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class AnsiDataTypeConverter extends AbstractDataTypeConverter
{

	protected ConvertInfo convert( DataType dataType, EDbVendor targetVendor,
			boolean convert ) throws ConvertException
	{
		if ( dataType == null || dataType.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "DataType should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		EDbVendor sourceVendor = dataType.getElement( )
				.getGsqlparser( )
				.getDbVendor( );
		if ( !EDbVendor.dbvansi.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "DataType should match the ASNI vendor, but "
					+ SQLUtil.getVendorName( sourceVendor ) );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		try
		{
			TTypeName typeName = dataType.getElement( );

			if ( !enableConvert( targetVendor ) )
			{
				throw generateConvertException( typeName, targetVendor );
			}

			ConvertInfo info = generateConvertInfo( typeName, targetVendor );
			switch ( typeName.getDataType( ) )
			{
				case character_t :
				case char_t :
					convertCharacter( typeName, targetVendor, convert );
					break;
				case varchar_t :
					convertVaryingCharacter( typeName, targetVendor, convert );
					break;
				case ncharacter_t :
				case nchar_t :
					convertNationalCharacter( typeName, targetVendor, convert );
					break;
				case nvarchar_t :
					convertVaryingNationalCharacter( typeName,
							targetVendor,
							convert );
					break;
				case numeric_t :
					convertNumeric( typeName, targetVendor, convert );
					break;
				case float_t :
					convertFloat( typeName, targetVendor, convert );
					break;
				case real_t :
					convertReal( typeName, targetVendor, convert );
					break;
				case double_precision_t :
					convertDoublePrecision( typeName, targetVendor, convert );
					break;
				case decimal_t :
					convertDecimal( typeName, targetVendor, convert );
					break;
				case integer_t :
				case int_t :
					convertInt( typeName, targetVendor, convert );
					break;
				case smallint_t :
					convertSmallInt( typeName, targetVendor, convert );
					break;
				default :
					throw generateConvertException( typeName, targetVendor );
			}
			return info;
		}
		catch ( ConvertException e )
		{
			throw e;
		}

	}

	private void convertSmallInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertDecimal( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertDoublePrecision( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

	}

	private void convertReal( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertFloat( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertNumeric( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{

	}

	private void convertVaryingNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

	}

	private void convertNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

	}

	private void convertVaryingCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

	}

	protected void convertCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		return false;
	}

}

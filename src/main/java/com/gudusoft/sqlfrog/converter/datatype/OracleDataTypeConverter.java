
package com.gudusoft.sqlfrog.converter.datatype;

import gudusoft.gsqlparser.EDataType;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ELiteralType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TConstant;
import gudusoft.gsqlparser.nodes.TTypeName;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleDataTypeConverter extends AbstractDataTypeConverter
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
		if ( !EDbVendor.dbvoracle.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "DataType should match the oracle vendor, but "
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

			ConvertInfo info = generateConvertInfo( typeName,
					targetVendor,
					convert );
			switch ( typeName.getDataType( ) )
			{
				case character_t :
				case char_t :
					convertCharacter( typeName, targetVendor, convert );
					break;
				case varchar2_t :
					convertVaryingCharacter( typeName, targetVendor, convert );
					break;
				case ncharacter_t :
				case nchar_t :
					convertNationalCharacter( typeName, targetVendor, convert );
				case nvarchar2_t :
					convertVaryingNationalCharacter( typeName,
							targetVendor,
							convert );
					break;
				case number_t :
					convertNumber( typeName, targetVendor, convert );
					break;
				case float_t :
					convertFloat( typeName, targetVendor, convert );
					break;
				case binary_float_t :
					convertBinaryFloat( typeName, targetVendor, convert );
					break;
				case binary_double_t :
					convertBinaryDouble( typeName, targetVendor, convert );
					break;
				case date_t :
					convertDate( typeName, targetVendor, convert );
					break;
				case timestamp_t :
					convertTimestamp( typeName, targetVendor, convert );
					break;
				case timestamp_with_time_zone_t :
					convertTimestampWithTimeZone( typeName,
							targetVendor,
							convert );
					break;
				case timestamp_with_local_time_zone_t :
					convertTimestampWithLocalTimeZone( typeName,
							targetVendor,
							convert );
					break;
				case long_t :
					convertLong( typeName, targetVendor, convert );
					break;
				case long_raw_t :
					convertLongRaw( typeName, targetVendor, convert );
					break;
				case raw_t :
					convertRaw( typeName, targetVendor, convert );
					break;
				case blob_t :
					convertBlob( typeName, targetVendor, convert );
					break;
				case clob_t :
					convertClob( typeName, targetVendor, convert );
					break;
				case nclob_t :
					convertNationalClob( typeName, targetVendor, convert );
					break;
				case bfile_t :
					convertBinaryFile( typeName, targetVendor, convert );
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

	private void convertBinaryFile( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varbinary_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "max" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etString );
					typeName.setLength( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNationalClob( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.text_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertClob( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varchar_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "max" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etString );
					typeName.setLength( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBlob( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.image_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertRaw( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varbinary_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertLongRaw( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.image_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertLong( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varchar_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "max" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etString );
					typeName.setLength( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertTimestampWithLocalTimeZone( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varchar_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "37" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etString );
					typeName.setLength( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertTimestampWithTimeZone( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varchar_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "37" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etString );
					typeName.setLength( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertTimestamp( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.datetime_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertDate( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.datetime_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBinaryDouble( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.float_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "53" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBinaryFloat( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.float_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "24" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertFloat( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		TConstant precisionValue = typeName.getPrecision( );
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					if ( precisionValue == null )
					{
						typeName.setDataType( EDataType.float_t );
						break;
					}
					else
					{
						int precision = Integer.valueOf( precisionValue.getValue( ) );
						if ( precision > 53 )
						{
							typeName.setPrecision( null );
						}
						break;
					}
				}
				else
				{
					break;
				}
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNumber( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		TConstant precisionValue = typeName.getPrecision( );
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					if ( precisionValue == null )
					{

						typeName.setDataType( EDataType.float_t );

						break;
					}
					else
					{
						typeName.setDataType( EDataType.numeric_t );
						TConstant scaleValue = typeName.getScale( );
						if ( scaleValue != null )
						{
							typeName.setDataType( EDataType.numeric_t );
							int precision = Integer.valueOf( precisionValue.getValue( ) );
							int scale = Integer.valueOf( scaleValue.getValue( ) );
							if ( scale > precision )
							{
								precisionValue.setString( String.valueOf( scale ) );
								precisionValue.getvalueToken( )
										.setString( String.valueOf( scale ) );
							}
						}
						break;
					}
				}
				else
				{
					break;
				}
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertVaryingNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.nvarchar_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.nchar_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertVaryingCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.varchar_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertCharacter( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					typeName.setDataType( EDataType.char_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				return true;
			default :
				return false;
		}
	}
}

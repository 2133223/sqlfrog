
package com.gudusoft.sqlfrog.converter.datatype;

import gudusoft.gsqlparser.EDataType;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.ELiteralType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TConstant;
import gudusoft.gsqlparser.nodes.TTypeName;

import java.lang.reflect.Field;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerDataTypeConverter extends AbstractDataTypeConverter
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
		if ( !EDbVendor.dbvmssql.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "DataType should match the mssql vendor, but "
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
				case text_t :
					convertText( typeName, targetVendor, convert );
					break;
				case ntext_t :
					convertNationalText( typeName, targetVendor, convert );
					break;
				case binary_t :
					convertBinary( typeName, targetVendor, convert );
					break;
				case varbinary_t :
					convertVaryingBinary( typeName, targetVendor, convert );
					break;
				case image_t :
					convertImage( typeName, targetVendor, convert );
					break;
				case bigint_t :
					convertBigInt( typeName, targetVendor, convert );
					break;
				case int_t :
					convertInt( typeName, targetVendor, convert );
					break;
				case smallint_t :
					convertSmallInt( typeName, targetVendor, convert );
					break;
				case tinyint_t :
					convertTinyInt( typeName, targetVendor, convert );
					break;
				case bit_t :
					convertBit( typeName, targetVendor, convert );
					break;
				case numeric_t :
				case decimal_t :
					convertNumeric( typeName, targetVendor, convert );
					break;
				case money_t :
					convertMoney( typeName, targetVendor, convert );
					break;
				case smallmoney_t :
					convertSmallMoney( typeName, targetVendor, convert );
					break;
				case float_t :
					convertFloat( typeName, targetVendor, convert );
					break;
				case real_t :
					convertReal( typeName, targetVendor, convert );
					break;
				case date_t :
					convertDate( typeName, targetVendor, convert );
					break;
				case datetime_t :
					convertDateTime( typeName, targetVendor, convert );
					break;
				case datetime2_t :
					convertDateTime2( typeName, targetVendor, convert );
					break;
				case datetimeoffset_t :
					convertDateTimeOffset( typeName, targetVendor, convert );
					break;
				case smalldatetime_t :
					convertSmallDateTime( typeName, targetVendor, convert );
					break;
				case time_t :
					convertTime( typeName, targetVendor, convert );
					break;
				case rowversion_t :
					convertRowVersion( typeName, targetVendor, convert );
					break;
				case generic_t :
					if ( "ntext".equals( typeName.toString( ).toLowerCase( ) ) )
					{
						convertNationalText( typeName, targetVendor, convert );
						break;
					}
					else if ( "image".equals( typeName.toString( )
							.toLowerCase( ) ) )
					{
						convertImage( typeName, targetVendor, convert );
						break;
					}
					else if ( "datetime2".equals( typeName.toString( )
							.toLowerCase( ) ) )
					{
						convertDateTime2( typeName, targetVendor, convert );
						break;
					}
					else if ( "datetimeoffset".equals( typeName.toString( )
							.toLowerCase( ) ) )
					{
						convertDateTimeOffset( typeName, targetVendor, convert );
						break;
					}
					else if ( "smalldatetime".equals( typeName.toString( )
							.toLowerCase( ) ) )
					{
						convertSmallDateTime( typeName, targetVendor, convert );
						break;
					}
					else if ( "smallmoney".equals( typeName.toString( )
							.toLowerCase( ) ) )
					{
						convertSmallMoney( typeName, targetVendor, convert );
						break;
					}
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

	private void convertDate( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.date_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertFloat( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "49" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertRowVersion( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				throw generateConvertException( typeName, targetVendor );
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertTime( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.timestamp_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertSmallDateTime( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.date_t );
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "date" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertDateTimeOffset( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.time_with_time_zone_t );
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "timestamp with time zone" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertDateTime2( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.timestamp_t );
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "timestamp" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertDateTime( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.date_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertReal( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.float_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "23" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertSmallMoney( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "number" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}

					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "10" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
					TConstant length1 = new TConstant( );
					TSourceToken st1 = new TSourceToken( "4" );
					length1.setValueToken( st1 );
					length1.setLiteralType( ELiteralType.etNumber );
					typeName.setScale( length1 );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertMoney( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "19" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
					TConstant length1 = new TConstant( );
					TSourceToken st1 = new TSourceToken( "4" );
					length1.setValueToken( st1 );
					length1.setLiteralType( ELiteralType.etNumber );
					typeName.setScale( length1 );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNumeric( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBit( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "3" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertTinyInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "3" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertSmallInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "5" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "10" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBigInt( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.number_t );
					TConstant length = new TConstant( );
					TSourceToken st = new TSourceToken( "19" );
					length.setValueToken( st );
					length.setLiteralType( ELiteralType.etNumber );
					typeName.setPrecision( length );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertImage( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.blob_t );
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "blob" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertVaryingBinary( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		TConstant lengthValue = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( lengthValue == null )
				{
					if ( convert )
					{
						typeName.setDataType( EDataType.raw_t );
						TConstant length = new TConstant( );
						TSourceToken st = new TSourceToken( "1" );
						length.setValueToken( st );
						length.setLiteralType( ELiteralType.etNumber );
						typeName.setLength( length );
					}
					break;
				}
				else if ( lengthValue.getValue( ).toLowerCase( ).equals( "max" ) )
				{
					if ( convert )
					{
						typeName.setDataType( EDataType.blob_t );
						typeName.setLength( null );
					}
					break;
				}
				else if ( lengthValue.getLiteralType( ) == ELiteralType.etNumber )
				{
					int length = Integer.parseInt( lengthValue.getValue( ) );
					if ( length >= 1 && length <= 2000 )
					{
						if ( convert )
						{
							typeName.setDataType( EDataType.raw_t );
						}
						break;
					}
					else if ( length > 2000 )
					{
						if ( convert )
						{
							typeName.setDataType( EDataType.blob_t );
							typeName.setLength( null );
						}
						break;
					}
					else
					{
						throw generateConvertException( typeName, targetVendor );
					}
				}
				else
				{
					throw generateConvertException( typeName, targetVendor );
				}
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertBinary( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		TConstant lengthValue = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :

				if ( lengthValue == null )
				{
					if ( convert )
					{
						typeName.setDataType( EDataType.raw_t );
						TConstant length = new TConstant( );
						TSourceToken st = new TSourceToken( "1" );
						length.setValueToken( st );
						length.setLiteralType( ELiteralType.etNumber );
						typeName.setLength( length );
					}
				}
				else if ( lengthValue.getValue( ).toLowerCase( ).equals( "max" ) )
				{
					if ( convert )
					{
						typeName.setDataType( EDataType.blob_t );
						typeName.setLength( null );
					}
				}
				else if ( lengthValue.getLiteralType( ) == ELiteralType.etNumber )
				{
					int length = Integer.parseInt( lengthValue.getValue( ) );
					if ( length >= 1 && length <= 2000 )
					{
						if ( convert )
						{
							typeName.setDataType( EDataType.raw_t );
						}
						break;
					}
					else if ( length > 2000 )
					{
						if ( convert )
						{
							typeName.setDataType( EDataType.blob_t );
							typeName.setLength( null );
						}
						break;
					}
					else
					{
						throw generateConvertException( typeName, targetVendor );
					}
				}
				else
				{
					throw generateConvertException( typeName, targetVendor );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNationalText( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.nclob_t );
					try
					{
						Field dataTypeName = typeName.getClass( )
								.getDeclaredField( "dataTypeName" );
						dataTypeName.setAccessible( true );
						dataTypeName.set( typeName, "nclob" );
					}
					catch ( Exception e )
					{
						throw generateConvertException( typeName,
								targetVendor,
								e );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertText( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					typeName.setDataType( EDataType.clob_t );
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertVaryingNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{
		TConstant length = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					if ( length != null
							&& ( length.getValue( )
									.toLowerCase( )
									.equals( "max" ) || ( length.getLiteralType( ) == ELiteralType.etNumber && Integer.parseInt( length.getValue( ) ) > 4000 ) ) )
					{
						typeName.setDataType( EDataType.nclob_t );
						typeName.setLength( null );
					}
					else
					{
						typeName.setDataType( EDataType.nvarchar2_t );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertNationalCharacter( TTypeName typeName,
			EDbVendor targetVendor, boolean convert )
	{

		TConstant lengthValue = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					if ( lengthValue != null
							&& lengthValue.getLiteralType( ) == ELiteralType.etNumber )
					{
						int length = Integer.parseInt( lengthValue.getValue( ) );
						if ( length < 1 || length > 2000 )
						{
							typeName.setDataType( EDataType.nclob_t );
							typeName.setLength( null );
							break;
						}
					}
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
		TConstant length = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					if ( length != null
							&& ( length.getValue( )
									.toLowerCase( )
									.equals( "max" ) || ( length.getLiteralType( ) == ELiteralType.etNumber && Integer.parseInt( length.getValue( ) ) > 4000 ) ) )
					{
						typeName.setDataType( EDataType.clob_t );
						typeName.setLength( null );
					}
					else
					{
						typeName.setDataType( EDataType.varchar2_t );
					}
				}
				break;
			default :
				throw generateConvertException( typeName, targetVendor );
		}
	}

	private void convertCharacter( TTypeName typeName, EDbVendor targetVendor,
			boolean convert )
	{
		TConstant lengthValue = typeName.getLength( );
		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					if ( lengthValue != null
							&& lengthValue.getLiteralType( ) == ELiteralType.etNumber )
					{
						int length = Integer.parseInt( lengthValue.getValue( ) );
						if ( length < 1 || length > 2000 )
						{
							typeName.setDataType( EDataType.clob_t );
							typeName.setLength( null );
							break;
						}
					}
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
			case dbvoracle :
				return true;
			default :
				return false;
		}
	}
}

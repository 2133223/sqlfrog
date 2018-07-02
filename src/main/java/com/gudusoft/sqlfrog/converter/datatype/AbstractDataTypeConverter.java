
package com.gudusoft.sqlfrog.converter.datatype;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TTypeName;

import com.gudusoft.sqlfrog.converter.DataTypeConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.model.Tuple;
import com.gudusoft.sqlfrog.util.SQLUtil;

public abstract class AbstractDataTypeConverter implements DataTypeConverter
{

	@Override
	public ConvertInfo scan( DataType dataType, EDbVendor targetVendor )
			throws ConvertException
	{
		return convert( dataType, targetVendor, false );
	}

	@Override
	public void convert( DataType dataType, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( dataType, targetVendor, true );
	}

	protected abstract ConvertInfo convert( DataType dataType,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;

	protected ConvertInfo generateConvertInfo( TTypeName dataType,
			EDbVendor targetVendor, boolean convert )
	{
		ConvertInfo info = new ConvertInfo( );
		if ( convert )
		{
			info.setInfo( "Can convert the "
					+ SQLUtil.getVendorName( dataType.getStartToken( )
							.getDbvendor( ) )
					+ " data type "
					+ dataType.toString( )
					+ " to "
					+ SQLUtil.getVendorName( targetVendor )
					+ " automatically." );
			info.setPosition( new Tuple<Long>( dataType.getLineNo( ),
					dataType.getColumnNo( ) ) );
			String filePath = dataType.getStartToken( ).container.getGsqlparser( )
					.getSqlfilename( );
			if ( !SQLUtil.isEmpty( filePath ) )
			{
				info.setFilePath( filePath );
			}
		}
		return info;
	}

	protected ConvertException generateConvertException( TTypeName dataType,
			EDbVendor targetVendor )
	{
		return new ConvertException( "Can't convert the "
				+ SQLUtil.getVendorName( dataType.getStartToken( )
						.getDbvendor( ) )
				+ " data type "
				+ dataType.toString( )
				+ " to "
				+ SQLUtil.getVendorName( targetVendor )
				+ ", line:"
				+ dataType.getLineNo( )
				+ ", column:"
				+ dataType.getColumnNo( )
				+ "." );
	}

	protected ConvertException generateConvertException( TTypeName dataType,
			EDbVendor targetVendor, Exception e )
	{
		return new ConvertException( "Can't convert the "
				+ SQLUtil.getVendorName( dataType.getStartToken( )
						.getDbvendor( ) )
				+ " data type "
				+ dataType.toString( )
				+ " to "
				+ SQLUtil.getVendorName( targetVendor )
				+ ", line:"
				+ dataType.getLineNo( )
				+ ", column:"
				+ dataType.getColumnNo( )
				+ ".", e );
	}
}

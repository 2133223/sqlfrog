
package com.gudusoft.sqlfrog.converter.function;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TFunctionCall;

import com.gudusoft.sqlfrog.converter.FunctionConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Function;
import com.gudusoft.sqlfrog.util.SQLUtil;

public abstract class AbstractFunctionConverter implements FunctionConverter
{

	@Override
	public ConvertInfo scan( Function function, EDbVendor targetVendor )
			throws ConvertException
	{
		return convert( function, targetVendor, false );
	}

	@Override
	public void convert( Function function, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( function, targetVendor, true );
	}

	protected abstract ConvertInfo convert( Function function,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;

	protected ConvertException generateConvertException(
			TFunctionCall function, EDbVendor targetVendor )
	{
		return new ConvertException( "Can't convert the "
				+ SQLUtil.getVendorName( function.getStartToken( )
						.getDbvendor( ) )
				+ " function "
				+ function.toString( )
				+ " to "
				+ SQLUtil.getVendorName( targetVendor )
				+ ", line:"
				+ function.getLineNo( )
				+ ", column:"
				+ function.getColumnNo( )
				+ "." );
	}
//
//	protected ConvertInfo generateScanInfo( TFunctionCall function,
//			EDbVendor targetVendor )
//	{
//		ConvertInfo info = new ConvertInfo( );
//		info.setInfo( "Can convert the "
//				+ SQLUtil.getVendorName( function.getStartToken( )
//						.getDbvendor( ) )
//				+ " function "
//				+ function.toString( )
//				+ " to "
//				+ SQLUtil.getVendorName( targetVendor )
//				+ " automatically." );
//		info.setPosition( new Tuple<Long>( function.getLineNo( ),
//				function.getColumnNo( ) ) );
//		String filePath = function.getStartToken( ).container.getGsqlparser( )
//				.getSqlfilename( );
//		if ( !SQLUtil.isEmpty( filePath ) )
//		{
//			info.setFilePath( filePath );
//		}
//		return info;
//	}
}

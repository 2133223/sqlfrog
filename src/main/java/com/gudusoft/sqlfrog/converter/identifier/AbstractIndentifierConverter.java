
package com.gudusoft.sqlfrog.converter.identifier;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TObjectName;

import com.gudusoft.sqlfrog.converter.IdentifierConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Identifier;
import com.gudusoft.sqlfrog.model.Tuple;
import com.gudusoft.sqlfrog.util.SQLUtil;

public abstract class AbstractIndentifierConverter implements
		IdentifierConverter
{

	@Override
	public ConvertInfo scan( Identifier node, EDbVendor targetVendor )
			throws ConvertException
	{
		return convert( node, targetVendor, false );
	}

	@Override
	public void convert( Identifier node, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( node, targetVendor, true );
	}

	protected abstract ConvertInfo convert( Identifier identifier,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;

	protected ConvertInfo generateConvertInfo( TObjectName identifier,
			EDbVendor targetVendor, boolean convert )
	{
		ConvertInfo info = new ConvertInfo( );
		if ( convert )
		{
			info.setInfo( "Can convert the "
					+ SQLUtil.getVendorName( identifier.getStartToken( )
							.getDbvendor( ) )
					+ " identifier "
					+ identifier.toString( )
					+ " to "
					+ SQLUtil.getVendorName( targetVendor )
					+ " automatically." );
			info.setPosition( new Tuple<Long>( identifier.getLineNo( ),
					identifier.getColumnNo( ) ) );
			String filePath = identifier.getStartToken( ).container.getGsqlparser( )
					.getSqlfilename( );
			if ( !SQLUtil.isEmpty( filePath ) )
			{
				info.setFilePath( filePath );
			}
		}
		return info;
	}

	protected ConvertException generateConvertException(
			TObjectName identifier, EDbVendor targetVendor )
	{
		return new ConvertException( "Can't convert the "
				+ SQLUtil.getVendorName( identifier.getStartToken( )
						.getDbvendor( ) )
				+ " identifier "
				+ identifier.toString( )
				+ " to "
				+ SQLUtil.getVendorName( targetVendor )
				+ ", line:"
				+ identifier.getLineNo( )
				+ ", column:"
				+ identifier.getColumnNo( )
				+ "." );
	}
}

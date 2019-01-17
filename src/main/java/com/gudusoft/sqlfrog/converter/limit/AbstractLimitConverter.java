
package com.gudusoft.sqlfrog.converter.limit;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TParseTreeNode;

import com.gudusoft.sqlfrog.converter.LimitConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.LimitResultSet;
import com.gudusoft.sqlfrog.util.SQLUtil;

public abstract class AbstractLimitConverter implements LimitConverter
{

	@Override
	public ConvertInfo scan( LimitResultSet limitResultSet,
			EDbVendor targetVendor ) throws ConvertException
	{
		return convert( limitResultSet, targetVendor, false );
	}

	@Override
	public void convert( LimitResultSet limitResultSet, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( limitResultSet, targetVendor, true );
	}

	protected abstract ConvertInfo convert( LimitResultSet limitResultSet,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;

	protected ConvertException generateConvertException(
			TParseTreeNode limitClause, EDbVendor targetVendor )
	{
		return new ConvertException( "Can't convert the "
				+ SQLUtil.getVendorName( limitClause.getStartToken( )
						.getDbvendor( ) )
				+ " LimitResultSet "
				+ limitClause.toString( )
				+ " to "
				+ SQLUtil.getVendorName( targetVendor )
				+ ", line:"
				+ limitClause.getLineNo( )
				+ ", column:"
				+ limitClause.getColumnNo( )
				+ "." );
	}

}

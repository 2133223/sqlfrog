
package com.gudusoft.sqlfrog.converter.join;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TExpression;

import com.gudusoft.sqlfrog.converter.JoinConditionConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.JoinCondition;
import com.gudusoft.sqlfrog.model.Tuple;
import com.gudusoft.sqlfrog.util.SQLUtil;

public abstract class AbstractJoinConditionConverter implements
		JoinConditionConverter
{

	@Override
	public ConvertInfo scan( JoinCondition joinCondition, EDbVendor targetVendor )
			throws ConvertException
	{
		return convert( joinCondition, targetVendor, false );
	}

	@Override
	public void convert( JoinCondition joinCondition, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( joinCondition, targetVendor, true );
	}

	protected abstract ConvertInfo convert( JoinCondition joinCondition,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;

	protected ConvertInfo generateConvertInfo( TExpression joinCondition,
			EDbVendor targetVendor, boolean convert )
	{
		ConvertInfo info = new ConvertInfo( );
		if ( convert )
		{
			info.setInfo( "Can convert the "
					+ SQLUtil.getVendorName( joinCondition.getStartToken( )
							.getDbvendor( ) )
					+ " join "
					+ joinCondition.toString( )
					+ " to "
					+ SQLUtil.getVendorName( targetVendor )
					+ " automatically." );
			info.setPosition( new Tuple<Long>( joinCondition.getLineNo( ),
					joinCondition.getColumnNo( ) ) );
			String filePath = joinCondition.getStartToken( ).container.getGsqlparser( )
					.getSqlfilename( );
			if ( !SQLUtil.isEmpty( filePath ) )
			{
				info.setFilePath( filePath );
			}
		}
		return info;
	}
}
